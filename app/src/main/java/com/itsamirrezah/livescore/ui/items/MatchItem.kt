package com.itsamirrezah.livescore.ui.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itsamirrezah.livescore.R
import com.itsamirrezah.livescore.ui.model.MatchModel
import com.mikepenz.fastadapter.items.ModelAbstractItem

class MatchItem(
    val match: MatchModel
) : ModelAbstractItem<MatchModel, MatchItem.MatchViewHolder>(match) {

    override val layoutRes: Int
        get() = R.layout.match_item
    override val type: Int
        get() = R.id.FastAdapterMatchItem

    override fun getViewHolder(v: View): MatchViewHolder {
        return MatchViewHolder(v)
    }

    override fun bindView(holder: MatchViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        if (match.status == "SCHEDULED") {
            holder.lytMatchSchedule.visibility = View.VISIBLE
            holder.lytMatchScore.visibility = View.GONE
        } else {
            holder.lytMatchSchedule.visibility = View.GONE
            holder.lytMatchScore.visibility = View.VISIBLE
        }

        holder.tvHomeTeam.setText(match.homeTeam)
        holder.tvAwayTeam.setText(match.awayTeam)
        holder.tvUtcDate.setText(match.shortTime)
        holder.tvMatchStatus.setText(match.status.toLowerCase())
        holder.tvHomeTeamScore.setText(match.homeTeamScore)
        holder.tvAwayTeamScore.setText(match.awayTeamScore)
    }

    override fun unbindView(holder: MatchViewHolder) {
        super.unbindView(holder)

        holder.tvHomeTeam.setText("")
        holder.tvAwayTeam.setText("")
        holder.tvUtcDate.setText("")
        holder.tvMatchStatus.setText("")
        holder.tvHomeTeamScore.setText("")
        holder.tvAwayTeamScore.setText("")
        holder.lytMatchSchedule.visibility = View.GONE
        holder.lytMatchSchedule.visibility = View.GONE
    }

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var tvHomeTeam: TextView = view.findViewById(R.id.tvHomeTeam)
        var tvAwayTeam: TextView = view.findViewById(R.id.tvAwayTeam)
        var tvUtcDate: TextView = view.findViewById(R.id.tvUtcDate)
        var lytMatchSchedule: View = view.findViewById(R.id.lytMatchSchedule)
        var lytMatchScore: View = view.findViewById(R.id.lytMatchScore)
        var tvHomeTeamScore: TextView = view.findViewById(R.id.tvHomeTeamScore)
        var tvAwayTeamScore: TextView = view.findViewById(R.id.tvAwayTeamScore)
        var tvMatchStatus: TextView = view.findViewById(R.id.tvMatchStatus)
    }
}