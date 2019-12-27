package com.itsamirrezah.livescore.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.bottomappbar.BottomAppBar
import com.itsamirrezah.livescore.R
import com.itsamirrezah.livescore.data.services.FootbalDataApiImp
import com.itsamirrezah.livescore.ui.items.CompetitionItem
import com.itsamirrezah.livescore.ui.items.DateItem
import com.itsamirrezah.livescore.ui.items.MatchItem
import com.itsamirrezah.livescore.ui.model.CompetitionModel
import com.itsamirrezah.livescore.ui.model.DateModel
import com.itsamirrezah.livescore.ui.model.ItemModel
import com.itsamirrezah.livescore.ui.model.MatchModel
import com.itsamirrezah.livescore.util.EndlessScrollListener
import com.itsamirrezah.livescore.util.Utils
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter
import com.mikepenz.fastadapter.ui.items.ProgressItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    //views
    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView
    @BindView(R.id.bottomAppBar)
    lateinit var bottomAppBar: BottomAppBar
    @BindView(R.id.coordinator)
    lateinit var coordinator: CoordinatorLayout
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    //data
    private val itemAdapter = ModelAdapter { item: ItemModel ->
        when (item) {
            is MatchModel -> return@ModelAdapter MatchItem(item)
            is DateModel -> return@ModelAdapter DateItem(item)
            is CompetitionModel -> return@ModelAdapter CompetitionItem((item))
            else -> throw IllegalArgumentException()
        }
    }
    private lateinit var fastAdapter: GenericFastAdapter
    private var footerAdapter = GenericItemAdapter()
    private var headerAdapter = GenericItemAdapter()
    private var compositeDisposable = CompositeDisposable()
    private var loadToTop = false

    /**
     * LifeCycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        AndroidThreeTen.init(application)
        setupBottomAppBar()
        setupRecyclerView()
        setupBottomSheets()
        requestMatches(Utils.getDates())
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


    /**
     * Methods
     */

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        fastAdapter = FastAdapter.with(listOf(headerAdapter, itemAdapter, footerAdapter))
        recyclerView.adapter = fastAdapter

        val endlessScroll = object : EndlessScrollListener() {
            override fun onLoadMore(fromTop: Boolean) {
                loadToTop = fromTop
                val dateArg: Pair<String, String>

                if (!loadToTop) {
                    footerAdapter.clear()
                    footerAdapter.add(ProgressItem())
                    dateArg = Utils.getDates(itemAdapter.models.last().shortDate, false)
                } else {
                    headerAdapter.clear()
                    headerAdapter.add(ProgressItem())
                    dateArg = Utils.getDates(itemAdapter.models.first().shortDate, true)
                }
                requestMatches(dateArg)
            }
        }
        recyclerView.addOnScrollListener(endlessScroll)
    }

    private fun setupBottomSheets() {
        val bottomSheet = coordinator.findViewById(R.id.bottomSheet) as View
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        requestCompetitions()
    }

    private fun setupBottomAppBar() {
        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.itemToday -> recyclerView.scrollToPosition(getTodayPosition())
            }
            true
        }
    }

    private fun requestMatches(arg: Pair<String, String>) {
        val requestMatches = FootbalDataApiImp.getApi()
            .getMatches(arg.first, arg.second)
            //returning matches one by one from matchResponse.matches
            .flatMap { Observable.fromIterable(it.matches) }
            //change api model to ui model
            .map {
                MatchModel(
                    it.homeTeam.name,
                    it.score.fullTime.homeTeam.toString(),
                    it.awayTeam.name,
                    it.score.fullTime.awayTeam.toString(),
                    it.utcDate,
                    it.status,
                    it.competition,
                    it.matchday.toString()
                ) as ItemModel
            } //group emissions base on their match dates
            .groupBy { it.shortDate.date }
            //group emissions base on their competition.id
            .switchMap { it.groupBy { it.competition!!.id } }
            .flatMapSingle { it.toList() }
            .groupBy { it.first().shortDate.date }
            .flatMapSingle { it.toList() }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)

        compositeDisposable.add(requestMatches)
    }

    //add ui element (DateItem and CompetitionItem) to recycler view based on result
    private fun onResponse(matchItems: List<List<MutableList<ItemModel>>>) {
        val matchesObservable = Observable.fromIterable(matchItems)
            .flatMapSingle {
                // returning List<Item> (these items have the same competition and date) one by one from List<List<Item>
                Observable.fromIterable(it)
                    //add CompetitionModel to start of the list
                    .map {
                        val match = it.first() as MatchModel
                        it.add(0, CompetitionModel(match.utcDate, match.competition, match.matchday))
                        it
                    } // wait until all emissions done, then return data (List<List<ItemModel>>>)
                    .toList()
            } //add DateModel to start of the list
            .map {
                it.first().add(0, DateModel(it.first().first().utcDate))
                //returning single list of all elements from all lists in the given list. (List<List<Item>> => List<Item>)
                it.flatten()
            } // wait until all emissions done, then return data (List<List<List<ItemModel>>>>)
            .toList()
            .subscribe { data -> updateRecyclerView(data) }
        compositeDisposable.add(matchesObservable)

    }

    private fun onError(throwable: Throwable) {
        print("test")
    }

    private fun updateRecyclerView(items: MutableList<List<ItemModel>>) {
        items.sortBy { it.first().shortDate }
        if (loadToTop) {
            headerAdapter.clear()
            itemAdapter.add(0, items.flatten())
        } else {
            footerAdapter.clear()
            itemAdapter.add(items.flatten())
        }
    }

    private fun getTodayPosition(): Int {
        return itemAdapter.models.indexOf(itemAdapter.models.find {
            if (it is DateModel)
                return@find it.dayOfWeek.toLowerCase().equals("today")
            return@find false
        })
    }
}
