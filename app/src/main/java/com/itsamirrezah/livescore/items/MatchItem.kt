package com.itsamirrezah.livescore.items

import android.view.View
import android.widget.TextView
import com.itsamirrezah.livescore.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class MatchItem(
    val homeTeam: String,
    val homeTeamScore: String,
    val awayTeam: String,
    val awayTeamScore: String,
    val utcDate: String,
    val status: String
): AbstractItem<FastAdapter.ViewHolder<MatchItem>>() {

    override val layoutRes: Int
        get() = R.layout.match_item
    override val type: Int
        get() = R.id.FastAdapterItem

    override fun getViewHolder(v: View): FastAdapter.ViewHolder<MatchItem> {
        return MatchViewHolder(v)
    }

    class MatchViewHolder(view: View): FastAdapter.ViewHolder<MatchItem>(view){

        lateinit var tvHomeTeam: TextView
        lateinit var tvAwayTeam: TextView
        lateinit var tvUtcDate: TextView
        lateinit var lytMatchSchedule: View
        lateinit var lytMatchScore: View
        lateinit var tvHomeTeamScore: TextView
        lateinit var tvAwayTeamScore: TextView
        lateinit var tvMatchStatus: TextView

        override fun bindView(matchItem: MatchItem, payloads: MutableList<Any>) {
            lytMatchSchedule = itemView.findViewById(R.id.lytMatchSchedule)
            lytMatchScore = itemView.findViewById(R.id.lytMatchScore)
            tvHomeTeamScore = itemView.findViewById(R.id.tvHomeTeamScore)
            tvAwayTeamScore = itemView.findViewById(R.id.tvAwayTeamScore)
            tvMatchStatus = itemView.findViewById(R.id.tvMatchStatus)
            tvHomeTeam = itemView.findViewById(R.id.tvHomeTeam)
            tvAwayTeam = itemView.findViewById(R.id.tvAwayTeam)
            tvUtcDate = itemView.findViewById(R.id.tvUtcDate)

            if (matchItem.status == "SCHEDULED"){
                lytMatchSchedule.visibility = View.VISIBLE
                lytMatchScore.visibility = View.GONE
            } else{
                lytMatchSchedule.visibility = View.GONE
                lytMatchScore.visibility = View.VISIBLE

            }

            tvHomeTeam.setText(matchItem.homeTeam)
            tvAwayTeam.setText(matchItem.awayTeam)
            tvUtcDate.setText(matchItem.utcDate)
            tvMatchStatus.setText(matchItem.status.toLowerCase())
            tvHomeTeamScore.setText(matchItem.homeTeamScore)
            tvAwayTeamScore.setText(matchItem.awayTeamScore)
        }

        override fun unbindView(item: MatchItem){
            tvHomeTeam.setText("")
            tvAwayTeam.setText("")
            tvUtcDate.setText("")
            tvMatchStatus.setText("")
            tvHomeTeamScore.setText("")
            tvAwayTeamScore.setText("")
            lytMatchSchedule.visibility = View.GONE
            lytMatchSchedule.visibility = View.GONE

        }

    }

    class MatchDateViewHolder(view: View): FastAdapter.ViewHolder<MatchItem>(view){

        override fun bindView(item: MatchItem, payloads: MutableList<Any>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun unbindView(item: MatchItem) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class MatchCompetitionViewHolder(view: View): FastAdapter.ViewHolder<MatchItem>(view){

        override fun bindView(item: MatchItem, payloads: MutableList<Any>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun unbindView(item: MatchItem) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}