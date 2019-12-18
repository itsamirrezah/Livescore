package com.itsamirrezah.livescore.ui.items

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.itsamirrezah.livescore.R
import com.itsamirrezah.livescore.ui.model.DateModel
import com.mikepenz.fastadapter.items.ModelAbstractItem

class DateItem(
    val dateModel: DateModel
) : ModelAbstractItem<DateModel, DateItem.DateViewHolder>(dateModel) {

    override val layoutRes: Int
        get() = R.layout.match_date_header_item
    override val type: Int
        get() = R.id.FastAdapterDateItem

    override fun getViewHolder(v: View): DateViewHolder {
        return DateViewHolder(v)
    }

    override fun bindView(holder: DateViewHolder, payloads: MutableList<Any>) {
        super.bindView(holder, payloads)
        holder.tvDayOfWeek.setText(dateModel.dayOfWeek)
        holder.tvDate.setText(dateModel.dateOfMonth)
    }

    override fun unbindView(holder: DateViewHolder) {
        super.unbindView(holder)
        holder.tvDayOfWeek.setText("")
        holder.tvDate.setText("")
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
    }

}