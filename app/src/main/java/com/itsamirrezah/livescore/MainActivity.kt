package com.itsamirrezah.livescore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.itsamirrezah.livescore.data.services.FootbalDataApiImp
import com.itsamirrezah.livescore.items.MatchItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    @BindView(R.id.recyclerView)
    lateinit var recyclerView: RecyclerView


    private var itemAdapter = ItemAdapter<MatchItem>()
    private lateinit var fastAdapter: FastAdapter<MatchItem>
    private var compositeDisposable = CompositeDisposable()

    /**
     * LifeCycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        setupRecyclerView()
        requestMatches()

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
        fastAdapter = FastAdapter.with(itemAdapter)
        recyclerView.adapter = fastAdapter
    }


    private fun requestMatches() {
        val requestMatches = FootbalDataApiImp.getApi()
            .getMatches(getDate(0), getDate(5))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onResponse, this::onError)

        compositeDisposable.add(requestMatches)
    }


    private fun onResponse(MatchesResponse: List<MatchResponse>) {
        print("test")
    }

    private fun onError(throwable: Throwable) {
        print("test")
    }


    //step:0 => today, step:1 => tomorrow, ...
    private fun getDate(step: Int): String {
        val today = Calendar.getInstance()
        today.add(Calendar.DAY_OF_MONTH, step)
        return SimpleDateFormat("yyyy-MM-dd").format(today.time)

    }


}
