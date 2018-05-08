package com.example.rachelyiak.todolistkotlin.displaytasks

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.*
import com.example.rachelyiak.todolistkotlin.FragmentConstants
import com.example.rachelyiak.todolistkotlin.MainActivity
import com.example.rachelyiak.todolistkotlin.R
import com.example.rachelyiak.todolistkotlin.displaytasks.itemTouchHelper.ItemTouchHelperAdapter
import com.example.rachelyiak.todolistkotlin.tasks.Task
import kotlinx.android.synthetic.main.task_item_row.view.*
import java.util.*
import com.example.rachelyiak.todolistkotlin.displaytasks.itemTouchHelper.OnStartDragListener


class DisplayTaskAdapter(data: ArrayList<Task>, dragStartListener: OnStartDragListener) : RecyclerView.Adapter<CustomViewHolder>(), ItemTouchHelperAdapter {

    private var mData: ArrayList<Task> = data
    private var mDragStartListener: OnStartDragListener = dragStartListener

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getItem(position: Int): Task {
        return mData[position]
    }

    fun setItems(list: List<Task>) {
        this.mData = list as ArrayList<Task>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowTask = layoutInflater.inflate(R.layout.task_item_row, parent, false)
        return CustomViewHolder(rowTask)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.view.task_title.text = mData[position].name

        holder.view.task_checkbox.isChecked = mData[position].completed
        if (mData[position].completed) {
            val taskTitleStrikethrough = SpannableString(mData[position].name)
            taskTitleStrikethrough.setSpan(StrikethroughSpan(), 0, taskTitleStrikethrough.length, 0)
            holder.view.task_title.text = taskTitleStrikethrough
            holder.view.task_title.setTextColor(Color.parseColor("#B0B0B0"))
        } else {
            holder.view.task_title.setTextColor(Color.parseColor("#757575"))
        }

        if (mData[position].highlight) {
            holder.view.task_highlight_tag.setBackgroundColor(Color.parseColor("#e8d52c"))
        } else {
            holder.view.task_highlight_tag.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.task = mData[position]

        holder.dragListener = mDragStartListener
    }

//    private fun convertTagToColor(colorTag: String?) : Int {
//        return when (colorTag) {
//            "red" ->  Color.parseColor("#F0E68C")//Color.parseColor("#ff4444")
//            "orange" -> Color.parseColor("#3CB371") //Color.parseColor("#ff8800")
//            "blue" -> Color.parseColor("#448aff")
//            "purple" -> Color.parseColor("#aa66cc")
//            else -> Color.TRANSPARENT
//        }
//    }

    override fun onItemMove(fromPosition: Int, toPosition: Int, viewHolder: RecyclerView.ViewHolder) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }
        DisplayTaskFragment().swapPosition(mData[fromPosition], mData[toPosition], viewHolder.itemView) //swapping using tasks instead of position as position != orderId exactly
        //TODO reload view after moving (else when editing after tap, task will still have old orderId)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, viewHolder: RecyclerView.ViewHolder) {
        DisplayTaskFragment().deleteTask(mData[position], viewHolder.itemView)
        notifyItemRemoved(position);
    }
}

class CustomViewHolder(val view: View, var task: Task? = null, var dragListener: OnStartDragListener? = null) : RecyclerView.ViewHolder(view) {

    init {
        val context = view.context
        view.setOnClickListener {
            if (!task!!.completed) {
                (context as MainActivity).replaceWithFrag(FragmentConstants.EDIT, task)
            }
        }

        view.task_checkbox.setOnClickListener {
            DisplayTaskFragment().completeTask(task!!, view)
        }

        view.button_reorder.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN)
               dragListener?.onStartDrag(this)
            false
        }
    }
}