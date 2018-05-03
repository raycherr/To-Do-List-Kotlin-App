package com.example.rachelyiak.todolistkotlin.displaytasks.itemTouchHelper

import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper


class SimpleItemTouchHelperCallback(private val mAdapter: ItemTouchHelperAdapter) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder,
                        target: ViewHolder): Boolean {
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition, viewHolder)
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition, viewHolder)
    }

}