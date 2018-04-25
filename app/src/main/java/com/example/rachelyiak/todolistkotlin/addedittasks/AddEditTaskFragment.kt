package com.example.rachelyiak.todolistkotlin.addedittasks

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.rachelyiak.todolistkotlin.MainActivity
import com.example.rachelyiak.todolistkotlin.R
import com.example.rachelyiak.todolistkotlin.tasks.KeyConstants
import com.example.rachelyiak.todolistkotlin.tasks.Task
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.*

class AddEditTaskFragment : Fragment(), AddEditTaskView {

    lateinit var taskName : TextView
    lateinit var taskDescription : TextView
    private lateinit var saveBtn : Button
    var taskId : Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root : View = inflater.inflate(R.layout.fragment_add_task, container, false)
        taskName = root.findViewById(R.id.taskNameInput)
        taskDescription = root.findViewById(R.id.taskDescriptionInput)
        saveBtn = root.findViewById(R.id.btn_save_task)

        val bundle = this.arguments

        if (bundle!=null) {
            taskId = bundle.getLong(KeyConstants.KEY_ID)
            taskName.text = bundle.getString(KeyConstants.KEY_NAME)
            taskDescription.text = bundle.getString(KeyConstants.KEY_DESCRIPTION)
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
                    emitter.onNext(taskTemp)
                }
            }
            emitter.setCancellable {
                saveBtn.setOnClickListener(null)
            }
        }
    }
}
