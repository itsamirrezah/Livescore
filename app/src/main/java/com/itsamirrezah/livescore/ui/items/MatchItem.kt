package com.itsamirrezah.livescore.ui.items

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itsamirrezah.livescore.R
import com.itsamirrezah.livescore.ui.model.MatchModel
import com.itsamirrezah.livescore.util.svg.GlideApp
import com.mikepenz.fastadapter.items.ModelAbstractItem

class MatchItem(
    val match: MatchModel,
    private val onTeamInfo: OnTeamInfo
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

        onTeamInfo.getTeamsFlag(match.homeTeam.id, match.awayTeam.id, object : OnResult {
            override fun onSuccess(flags: Pair<String?, String?>) {
                match.homeTeam.flag = flags.first
                match.awayTeam.flag = flags.second

                GlideApp
                    .with(holder.itemView)
                    .load(match.homeTeam.flag)
                    .centerInside()
                    .into(holder.ivHomeTeam)

                GlideApp
                    .with(holder.itemView)
                    .load(match.awayTeam.flag)
                    .centerInside()
                    .into(holder.ivAwayTeam)
            }
        })

        holder.tvHomeTeam.text = match.homeTeam.name
        holder.tvAwayTeam.text = match.awayTeam.name
        holder.tvUtcDate.text = match.shortTime
        holder.tvMatchStatus.text = match.status.toLowerCase()
        holder.tvHomeTeamScore.text = match.homeTeamScore
        holder.tvAwayTeamScore.text = match.awayTeamScore
    }

    override fun unbindView(holder: MatchViewHolder) {
        super.unbindView(holder)

        holder.tvHomeTeam.text = ""
        holder.tvAwayTeam.text = ""
        holder.tvUtcDate.text = ""
        holder.tvMatchStatus.text = ""
        holder.tvHomeTeamScore.text = ""
        holder.tvAwayTeamScore.text = ""
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
        var ivHomeTeam: ImageView = view.findViewById(R.id.ivHomeTeam)
        var ivAwayTeam: ImageView = view.findViewById(R.id.ivAwayTeam)

    }
}

interface OnTeamInfo {
    fun getTeamsFlag(homeTeamId: Int, awayTeamId: Int, onResult: OnResult)
}

interface OnResult {
    fun onSuccess(flags: Pair<String?, String?>)
}