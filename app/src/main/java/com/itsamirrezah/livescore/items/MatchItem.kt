package com.itsamirrezah.livescore.items

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.IItemAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class MatchItem(): AbstractItem<FastAdapter.ViewHolder<MatchItem>>() {

    override val layoutRes: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val type: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getViewHolder(v: View): FastAdapter.ViewHolder<MatchItem> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class MatchViewHolder(view: View): FastAdapter.ViewHolder<MatchItem>(view){

        override fun bindView(item: MatchItem, payloads: MutableList<Any>) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun unbindView(item: MatchItem) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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