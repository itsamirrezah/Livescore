package com.itsamirrezah.livescore.ui.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itsamirrezah.livescore.R
import com.itsamirrezah.livescore.ui.model.CompetitionModel
import com.mikepenz.fastadapter.items.ModelAbstractItem

class CompetitionItem(
    var competition: CompetitionModel
) : ModelAbstractItem<CompetitionModel, CompetitionItem.CompetitionViewHolder>(competition) {

    override val layoutRes: Int
        get() = R.layout.competition_item
    override val type: Int
        get() = R.id.FastAdapterCompetitionItem

    override fun getViewHolder(v: View): CompetitionViewHolder {
        return CompetitionViewHolder(v)
    }

    override fun bindView(holder: CompetitionViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)

        holder.tvCompName.text = competition.competitionName
        holder.tvMatchday.text = "Matchday #" + competition.matchday
    }

    override fun unbindView(holder: CompetitionViewHolder) {
        super.unbindView(holder)
        holder.tvCompName.text = ""
        holder.tvMatchday.text = ""
    }

    class CompetitionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCompName: TextView = view.findViewById(R.id.tvCompetitionName)
        val tvMatchday: TextView = view.findViewById(R.id.tvMatchday)
    }

}