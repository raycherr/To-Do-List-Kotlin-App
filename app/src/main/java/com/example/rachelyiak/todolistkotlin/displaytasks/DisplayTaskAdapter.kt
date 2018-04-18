package com.example.rachelyiak.todolistkotlin.displaytasks

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.*
import com.example.rachelyiak.todolistkotlin.FragmentConstants
import com.example.rachelyiak.todolistkotlin.MainActivity
import com.example.rachelyiak.todolistkotlin.R
import com.example.rachelyiak.todolistkotlin.ToastConstants
import com.example.rachelyiak.todolistkotlin.tasks.Task
import kotlinx.android.synthetic.main.task_item_row.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import java.util.ArrayList

class DisplayTaskAdapter(data : ArrayList<Task>) : RecyclerView.Adapter<CustomViewHolder>() {

    private val mData : ArrayList<Task> = data

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getItem(position : Int) : Task {
        return mData[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val rowTask = layoutInflater.inflate(R.layout.task_item_row, parent, false)
        return CustomViewHolder(rowTask)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.view.task_title.text = mData[position].name

        if (mData[position].completed) {
            holder.view.task_checkbox.isChecked = mData[position].completed
            val taskTitleStrikethrough = SpannableString(mData[position].name)
            taskTitleStrikethrough.setSpan(StrikethroughSpan(), 0, taskTitleStrikethrough.length, 0)
            holder.view.task_title.text = taskTitleStrikethrough
            holder.view.task_title.setTextColor(Color.parseColor("#B0B0B0"))
        }

        holder.task = mData[position]
    }
}

class CustomViewHolder(val view : View, var task : Task? = null) : RecyclerView.ViewHolder(view) {

    init {
        val context = view.context
        view.setOnClickListener {
            if (!task!!.completed) {
                (context as MainActivity).replaceWithFrag(FragmentConstants.EDIT, task)
            }
        }
        view.setOnLongClickListener {
            Log.d("Hello",task!!.name)

            val visibility = view.button_delete.visibility

            if (visibility == View.VISIBLE) {
                view.button_delete.visibility = View.GONE
            } else view.button_delete.visibility = View.VISIBLE
            true
        }
        view.button_delete.setOnClickListener {
            DisplayTaskFragment().deleteTask(task!!, view)
        }

        view.task_checkbox.setOnClickListener({
            DisplayTaskFragment().completeTask(task!!, view)
        })
    }

}