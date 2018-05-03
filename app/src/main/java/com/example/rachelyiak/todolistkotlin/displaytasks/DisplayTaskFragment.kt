package com.example.rachelyiak.todolistkotlin.displaytasks

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import com.example.rachelyiak.todolistkotlin.tasks.Task
import android.view.*
import com.example.rachelyiak.todolistkotlin.FragmentConstants
import com.example.rachelyiak.todolistkotlin.MainActivity
import com.example.rachelyiak.todolistkotlin.R
import com.example.rachelyiak.todolistkotlin.R.layout.fragment_display_task_recycler_view
import com.example.rachelyiak.todolistkotlin.displaytasks.itemTouchHelper.ItemTouchHelperAdapter
import com.example.rachelyiak.todolistkotlin.displaytasks.itemTouchHelper.OnStartDragListener
import com.example.rachelyiak.todolistkotlin.displaytasks.itemTouchHelper.SimpleItemTouchHelperCallback
import kotlinx.android.synthetic.main.fragment_display_task_recycler_view.view.*
import java.util.ArrayList

class DisplayTaskFragment : Fragment(), DisplayTaskView, OnStartDragListener {

    private lateinit var taskRecyclerView : RecyclerView
    private lateinit var addTaskButton : FloatingActionButton
    var data : ArrayList<Task> = arrayListOf()
    private lateinit var mTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(fragment_display_task_recycler_view, container, false)

        setHasOptionsMenu(true)

        addTaskButton = root.btn_add_task
        addTaskButton.setOnClickListener {
            (activity as MainActivity).replaceWithFrag(FragmentConstants.ADD)
        }

        loadTaskList()
        taskRecyclerView = root.findViewById(android.R.id.list)
        taskRecyclerView.layoutManager = LinearLayoutManager((activity as MainActivity))
        taskRecyclerView.adapter = DisplayTaskAdapter(data, this)

        val callback = SimpleItemTouchHelperCallback(taskRecyclerView.adapter as ItemTouchHelperAdapter)
        mTouchHelper = ItemTouchHelper(callback)
        mTouchHelper.attachToRecyclerView(taskRecyclerView)

        return root
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        mTouchHelper.startDrag(viewHolder)
    }

    private fun loadTaskList() {
        DisplayTaskPresenter(this).loadTasks()
    }

    override fun onLoadTaskDisplay(list: List<Task>) {
        data = ArrayList(list)

        if (::taskRecyclerView.isInitialized) {
            (taskRecyclerView.adapter as DisplayTaskAdapter).setItems(list)
            taskRecyclerView.adapter.notifyDataSetChanged()
        }
    }

    override fun onUpdateTaskFail(errorName: String) {
        (taskRecyclerView.context as MainActivity).showToast(errorName)
    }

    override fun onTaskUpdated(infoName: String, task: Task?) {
        (taskRecyclerView.context as MainActivity).showToast(infoName, task)
    }

    fun completeTask(task: Task, view: View){
        taskRecyclerView = view.parent as RecyclerView
        DisplayTaskPresenter(this).markTask(task)
    }

    fun swapPosition(fromPositionTask: Task, toPositionTask: Task, view: View) {
        taskRecyclerView = view.parent as RecyclerView
        DisplayTaskPresenter(this).swapPosition(fromPositionTask, toPositionTask)
    }

    fun deleteTask(task: Task, view: View){
        taskRecyclerView = view.parent as RecyclerView
        DisplayTaskPresenter(this).deleteTask(task)
    }

    private fun deleteAllTask() {
        taskRecyclerView = view?.findViewById(android.R.id.list) as RecyclerView
        DisplayTaskPresenter(this).deleteAllTask()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_all -> {
                deleteAllTask()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}

