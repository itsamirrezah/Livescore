package com.itsamirrezah.livescore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.itsamirrezah.livescore.data.models.MatchResponse
import com.itsamirrezah.livescore.data.services.FootbalDataApiImp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var compositeDisposable = CompositeDisposable()

    /**
     * LifeCycle
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestMatches()

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }


    /**
     * Methods
     */

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

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")

        return dateFormat.format(today.time)


    }


}
