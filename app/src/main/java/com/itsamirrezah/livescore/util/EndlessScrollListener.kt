package com.itsamirrezah.livescore.util

import android.view.View
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener : RecyclerView.OnScrollListener() {

    private var enabled = true
    private var mPreviousTotal = 0
    private var mLoading = false
    private var mVisibleThreshold = -1
    var firstVisibleItem: Int = 0
        private set
    var lastVisibleItem: Int = 0
        private set
    var visibleItemCount: Int = 0
        private set
    var totalItemCount: Int = 0
        private set

    private var mIsOrientationHelperVertical: Boolean = false
    private var mOrientationHelper: OrientationHelper? = null

    var currentPage = 0
        private set

    lateinit var layoutManager: RecyclerView.LayoutManager
        private set

    private fun findFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(0, layoutManager.childCount, false, true)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
            child
        )
    }

    private fun findLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val child = findOneVisibleChild(recyclerView.childCount - 1, -1, false, true)
        return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
            child
        )
    }

    private fun findOneVisibleChild(
        fromIndex: Int, toIndex: Int, completelyVisible: Boolean,
        acceptPartiallyVisible: Boolean
    ): View? {
        if (layoutManager.canScrollVertically() != mIsOrientationHelperVertical || mOrientationHelper == null) {
            mIsOrientationHelperVertical = layoutManager.canScrollVertically()
            mOrientationHelper = if (mIsOrientationHelperVertical)
                OrientationHelper.createVerticalHelper(layoutManager)
            else
                OrientationHelper.createHorizontalHelper(layoutManager)
        }

        val mOrientationHelper = this.mOrientationHelper ?: return null

        val start = mOrientationHelper.startAfterPadding
        val end = mOrientationHelper.endAfterPadding
        val next = if (toIndex > fromIndex) 1 else -1
        var partiallyVisible: View? = null
        var i = fromIndex
        while (i != toIndex) {
            val child = layoutManager.getChildAt(i)
            if (child != null) {
                val childStart = mOrientationHelper.getDecoratedStart(child)
                val childEnd = mOrientationHelper.getDecoratedEnd(child)
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child
                        }
                    } else {
                        return child
                    }
                }
            }
            i += next
        }
        return partiallyVisible
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (enabled) {
            if (!::layoutManager.isInitialized)
                layoutManager = recyclerView.layoutManager
                    ?: throw RuntimeException("A LayoutManager is required")


            if (mVisibleThreshold == -1)
                mVisibleThreshold =
                    findLastVisibleItemPosition(recyclerView) - findFirstVisibleItemPosition(
                        recyclerView
                    )

            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = findFirstVisibleItemPosition(recyclerView)
            lastVisibleItem = findLastVisibleItemPosition(recyclerView)

            if (mLoading) {
                if (dy > 0 && lastVisibleItem != totalItemCount) mLoading = false
                else if (dy < 0 && firstVisibleItem != 0) mLoading = false
            }

            if (!mLoading && lastVisibleItem == totalItemCount - 1 && dy > 0) {
                currentPage++
                mLoading = true
                onLoadMore(false)

            } else if (!mLoading && firstVisibleItem == 0 && dy < 0) {
                mLoading = true
                onLoadMore(true)
            }
        }
    }

    fun enable(): EndlessScrollListener {
        enabled = true
        return this
    }

    fun disable(): EndlessScrollListener {
        enabled = false
        return this
    }

    abstract fun onLoadMore(fromTop: Boolean)
}