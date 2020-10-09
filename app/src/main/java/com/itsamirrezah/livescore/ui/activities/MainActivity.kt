package com.itsamirrezah.livescore.ui.activities

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.itsamirrezah.livescore.R
import com.itsamirrezah.livescore.data.db.LivescoreDb
import com.itsamirrezah.livescore.data.models.Team
import com.itsamirrezah.livescore.data.services.FootbalDataApiImp
import com.itsamirrezah.livescore.ui.items.*
import com.itsamirrezah.livescore.ui.model.*
import com.itsamirrezah.livescore.util.EndlessScrollListener
import com.itsamirrezah.livescore.util.SharedPreferencesUtil
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
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    //views
    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.bottomAppBar)
    lateinit var bottomAppBar: BottomAppBar

    @BindView(R.id.coordinator)
    lateinit var coordinator: CoordinatorLayout
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    @BindView(R.id.chipGroupCompetition)
    lateinit var chipGroupCompetition: ChipGroup

    @BindView(R.id.chipGroupStatus)
    lateinit var chipGroupStatus: ChipGroup

    //data
    private val itemAdapter = ModelAdapter { item: ItemModel ->
        when (item) {
            is MatchModel -> MatchItem(item, onTeamInfo, this)
            is DateModel -> DateItem(item)
            is CompetitionModel -> CompetitionItem((item))
            else -> throw IllegalArgumentException()
        }
    }
    private lateinit var fastAdapter: GenericFastAdapter
    private var footerAdapter = GenericItemAdapter()
    private var headerAdapter = GenericItemAdapter()
    private var compositeDisposable = CompositeDisposable()
    private var loadToTop = false
    private lateinit var endlessScroll: EndlessScrollListener
    private lateinit var preferences: SharedPreferencesUtil

    /**
     * LifeCycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        AndroidThreeTen.init(application)
        setupSharedPreference()
        setupBottomAppBar()
        setupRecyclerView()
        setupBottomSheets()
        requestMatches()
        if (preferences.serverCompetitions.isEmpty())
            requestCompetitions()
        else if (preferences.localCompetitions!!.size < preferences.serverCompetitions.size)
            requestTeams()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    @OnClick(R.id.btnFilterDone)
    fun filterDone() {
        val competitionsSelected = mutableSetOf<String>()
        for (i in 0 until chipGroupCompetition.childCount) {
            val chip = chipGroupCompetition.getChildAt(i) as Chip
            if (chip.isChecked)
                competitionsSelected.add(chip.tag.toString())
        }
        setPreferences(
            findViewById<Chip>(chipGroupStatus.checkedChipId).tag.toString(),
            competitionsSelected
        )
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        itemAdapter.clear()
        requestMatches()
    }

    /**
     * Methods
     */

    private fun setupSharedPreference() {
        preferences = SharedPreferencesUtil.getInstance(this)
    }

    private fun setPreferences(status: String, compIds: Set<String>) {
        preferences.statusSelected = status
        preferences.competitionsSelected = compIds
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        fastAdapter = FastAdapter.with(listOf(headerAdapter, itemAdapter, footerAdapter))
        recyclerView.adapter = fastAdapter

        endlessScroll = object : EndlessScrollListener() {
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
    }

    private fun setupBottomAppBar() {
        val itemNight = bottomAppBar.menu.findItem(R.id.itemNight)
        itemNight.icon = if (preferences.isNightMode) {
            ContextCompat.getDrawable(this, R.drawable.ic_sun_black_24dp)
        } else {
            ContextCompat.getDrawable(this, R.drawable.ic_moon_black_24dp)
        }

        bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.itemToday -> recyclerView.scrollToPosition(getTodayPosition())
                R.id.itemPreferences -> expandBottomSheet()
                R.id.itemNight -> {
                    val nightMode = !preferences.isNightMode
                    Handler().postDelayed({
                        if (nightMode)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        else
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                        preferences.isNightMode = nightMode
                    }, 300)
                }
            }
            true
        }
    }

    private fun expandBottomSheet() {
        addCompetitionChips()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        //set view base on shared preferences values
        val selectedStatus = preferences.statusSelected
        val selectedComps = preferences.competitionsSelected
        chipGroupStatus.findViewWithTag<Chip>(selectedStatus).isChecked = true
        for (i in 0 until selectedComps!!.count())
            chipGroupCompetition.findViewWithTag<Chip>(selectedComps.toList()[i].toInt())
                .isChecked = true
    }

    //get available competitions from api
    private fun requestCompetitions() {

        lifecycleScope.launch(Dispatchers.IO) {
            catchIOException {
                val availableCompetitions = FootbalDataApiImp.getApi().getCompetitions()
                    //convert from data model to ui model
                    .competitions.flatMap { listOf(CompetitionUi(it.id, it.name)) }
                //put data on shared preferences
                preferences.serverCompetitions = availableCompetitions
            }
            requestTeams()
        }
    }

    private suspend fun catchIOException(block: suspend () -> Unit) {
        try {
            block()
        } catch (e: SocketTimeoutException) {
            Toast.makeText(this, "Socket Timeout Exception", Toast.LENGTH_LONG).show()
        } catch (e: HttpException) {
            Toast.makeText(this, "Http Exception", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.d(com.itsamirrezah.livescore.util.TAG, "catchIOException: ${e.message}")
        }
    }

    //fetch teams info from api and put them on shared preferences & local database
    private fun requestTeams() {
        val comps = preferences.serverCompetitions
            //convert List<CompetitionUi> to List<String>
            .map { it.id.toString() }
            //returns a list containing all elements of the serverCompetitions which aren't available in localCompetitions
            .minus(preferences.localCompetitions!!.toList())


        comps.map { id ->
            lifecycleScope.launch(Dispatchers.IO) {
                catchIOException {
                    //gets teams information from api
                    val teams = FootbalDataApiImp.getApi().getTeamsByCompetition(id.toInt()).teams
                        .also {
                            //put teams info on local database
                            LivescoreDb.getInstance(this@MainActivity).teamsDao().insertTeams(it)
                        }
                        //convert data model to ui model
                        .map { team -> CompetitionUi(team.id, team.name) }
                    //put retrieved competitions on shared preferences
                    val localComps = preferences.localCompetitions!!.toMutableList()
                    for (team in teams)
                        localComps.add(team.id.toString())
                    preferences.localCompetitions = localComps.toSet()
                }
            }
        }
    }

    private fun addCompetitionChips() {
        val competitions: List<CompetitionUi> = preferences.serverCompetitions
        chipGroupCompetition.removeAllViews()
        //add chips programmatically
        for (comp in competitions) {
            val chip = Chip(chipGroupCompetition.context)
            val chipDrawable = ChipDrawable.createFromAttributes(
                chipGroupCompetition.context,
                null,
                0,
                R.style.Chips
            )
            chip.setChipDrawable(chipDrawable)
            chip.text = comp.name
            chip.tag = comp.id
            chipGroupCompetition.addView(chip)
        }
    }

    private fun requestMatches(date: Pair<String, String>? = null) {

        val compsArgs = preferences.competitionsSelected!!.toList().joinToString(",")
        val statusArg = preferences.statusSelected
        val datesArg = date ?: Utils.getDates()

        endlessScroll.disable()

        lifecycleScope.launch {
            catchIOException {
                FootbalDataApiImp.getApi()
                    .getMatches(datesArg.first, datesArg.second, statusArg!!, compsArgs).matches
                    .flatMap {
                        listOf(
                            //change api model to ui model
                            MatchModel(
                                mapTeamToUiModel(it.homeTeam),
                                it.score.fullTime.homeTeam.toString(),
                                mapTeamToUiModel(it.awayTeam),
                                it.score.fullTime.awayTeam.toString(),
                                it.utcDate,
                                it.status,
                                CompetitionUi(it.competition.id, it.competition.name),
                                it.matchday.toString()
                            )
                        )
                        //group matches based on their dates
                    }.groupBy { match -> match.shortDate.date }
                    //map-> each item have a list of matches with the same date
                    .map { map ->
                        //group each item based on their competition.id
                        map.value.groupBy { match -> match.competition!!.id }
                    }
                    //type(it): List<Map<Int, List<MatchModel>>>
                    .also { onResponse(it) }
            }
        }
    }

    private fun mapTeamToUiModel(team: Team): TeamModel {
        return TeamModel(team.id, team.name)
    }

    //create ui elements (DateItem and CompetitionItem) for recyclerView based on the result
    private suspend fun onResponse(matchesByDate: List<Map<Int, List<MatchModel>>>) {
        var items: MutableList<ItemModel> = mutableListOf()

        matchesByDate.forEach { date ->
            //add DateModel on start of the each list
            items.add(DateModel(date.values.first().first().utcDate))
            date.values.forEach { competitions ->
                val first = competitions.first()
                //add CompetitionModel on start of the each sub list
                items.add(
                    CompetitionModel(first.utcDate, first.competition!!, first.matchday)
                )
                //add MatchModels
                competitions.forEach { match -> items.add(match) }
            }
        }
        //update recyclerView with the new list
        withContext(Dispatchers.Main) {
            updateRecyclerView(items)
        }
    }

    private fun updateRecyclerView(items: MutableList<ItemModel>) {
        endlessScroll.enable()
        items.sortBy { it.shortDate }
        if (loadToTop) {
            headerAdapter.clear()
            itemAdapter.add(0, items)
        } else {
            footerAdapter.clear()
            itemAdapter.add(items)
        }
    }

    private fun getTodayPosition(): Int {
        return itemAdapter.models.indexOf(itemAdapter.models.find {
            if (it is DateModel)
                return@find it.dayOfWeek.toLowerCase() == "today"
            return@find false
        })
    }

    private val onTeamInfo: OnTeamInfo = object : OnTeamInfo {

        var matchFlags: Disposable? = null
        override fun getTeamsFlag(homeTeamId: Int, awayTeamId: Int, onResult: OnResult) {

            //get team flag urls from database
            //combine the emission of two observables via the BiFunction
            matchFlags = Observable.zip(
                LivescoreDb.getInstance(this@MainActivity).teamsDao().getTeamById(homeTeamId),
                LivescoreDb.getInstance(this@MainActivity).teamsDao().getTeamById(awayTeamId),
                BiFunction { homeTeam: Team, awayTeam: Team ->
                    //map data model to ui model
                    Pair(
                        TeamModel(homeTeam.id, homeTeam.name, homeTeam.crestUrl),
                        TeamModel(awayTeam.id, awayTeam.name, awayTeam.crestUrl)
                    )
                }
                //get flags drawable
            ).map {
                val homeDrawable = try {
                    Glide.with(this@MainActivity).load(it.first.flag).submit().get()
                } catch (e: Exception) {
                    null
                }
                val awayDrawable = try {
                    Glide.with(this@MainActivity).load(it.second.flag).submit().get()
                } catch (e: Exception) {
                    null
                }

                it.first.flagDrawable = homeDrawable
                it.second.flagDrawable = awayDrawable
                it
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    //todo: replace if condition with match.id
                    if (data.first.flagDrawable != null || data.second.flagDrawable != null)
                        itemAdapter.models
                            .asSequence()
                            .filterIsInstance<MatchModel>()
                            .find {
                                //todo: use match.id
                                return@find it.homeTeam.id == data.first.id && it.awayTeam.id == data.second.id
                            }
                            .apply {
                                //update recyclerView with new flags
                                this!!.homeTeam.flagDrawable = data.first.flagDrawable
                                this.awayTeam.flagDrawable = data.second.flagDrawable
                                fastAdapter.notifyAdapterItemChanged(
                                    itemAdapter.models.indexOf(
                                        this
                                    )
                                )
                                onResult.onSuccess()
                            }
                }, {
                    print(it.message)
                })
        }
    }
}