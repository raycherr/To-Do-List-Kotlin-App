package com.example.rachelyiak.todolistkotlin.addedittasks

import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.rachelyiak.todolistkotlin.MainActivity
import com.example.rachelyiak.todolistkotlin.R
import com.example.rachelyiak.todolistkotlin.R.layout.fragment_add_task
import com.example.rachelyiak.todolistkotlin.tasks.KeyConstants
import com.example.rachelyiak.todolistkotlin.tasks.Task
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_task.view.*

class AddEditTaskFragment : Fragment(), AddEditTaskView {

    lateinit var taskName : TextView
    lateinit var taskDescription : TextView
    private lateinit var saveBtn : Button
    private var taskId : Long? = null
    private var taskOrderId : Long? = null
    lateinit var taskHighlight : Switch

//    lateinit var taskColorTagGroup : RadioGroup
//    private var taskColorTagButton : RadioButton? = null
//    private var taskColorTag : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val root : View = inflater.inflate(fragment_add_task, container, false)
        taskName = root.taskNameInput
        taskDescription = root.taskDescriptionInput
        taskHighlight = root.taskHighlightSwitch
        saveBtn = root.btn_save_task

        /* uncomment if want new feature of adding different color tags, also should change to RxJava supported too
        taskColorTagGroup = root.findViewById(R.id.radioGroupColor)
        taskColorTagGroup.setOnCheckedChangeListener { _, checkedId ->
            taskColorTagButton = root.findViewById(checkedId) ?: null
            taskColorTag = taskColorTagButton?.tag.toString()
            if (taskColorTagButton == null)
                taskColorTag = null
             An issue is that un-checking is not supported right now
        }*/

        val bundle = this.arguments

        if (bundle!=null) {
            taskId = bundle.getLong(KeyConstants.KEY_ID)
            taskName.text = bundle.getString(KeyConstants.KEY_NAME)
            taskDescription.text = bundle.getString(KeyConstants.KEY_DESCRIPTION)
            taskHighlight.isChecked = bundle.getBoolean(KeyConstants.KEY_HIGHLIGHT)
            taskOrderId = bundle.getLong(KeyConstants.KEY_ORDER_ID)
        }

        val saveTaskObservable = saveTaskButtonClickObservable(taskId)

        saveTaskObservable
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io()) // placed repo search off the main thread
                .subscribe {
                    AddEditTaskPresenter(this).saveTask(it)
                }

        return root
    }

    override fun onAddTaskSuccess() {
        (activity as MainActivity).onAddTaskSuccess()
    }

    override fun onAddTaskFail(errorName : String) {
        (activity as MainActivity).showToast(errorName)
    }

    private fun saveTaskButtonClickObservable(taskId : Long?): Observable<Task> {
        return Observable.create { emitter ->
            saveBtn.setOnClickListener{
                if (AddEditTaskPresenter(this).haveTaskName(taskName.text.toString())) {

                    val taskTemp = Task(name = taskName.text.toString(), description = taskDescription.text.toString())
                    taskTemp.id = taskId ?: 0
                    taskTemp.highlight = taskHighlight.isChecked
                    taskTemp.orderId = taskOrderId ?: 0
                    emitter.onNext(taskTemp)
                }
            }
            emitter.setCancellable {
                saveBtn.setOnClickListener(null)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_delete_all).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }
}
