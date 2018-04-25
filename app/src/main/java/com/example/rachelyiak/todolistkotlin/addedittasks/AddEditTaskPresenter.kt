package com.example.rachelyiak.todolistkotlin.addedittasks

import android.content.Context
import android.util.Log
import com.example.rachelyiak.todolistkotlin.ToastConstants
import com.example.rachelyiak.todolistkotlin.tasks.Task
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepositoryImpl
import io.reactivex.subscribers.DefaultSubscriber

abstract class Presenter(val useCase : AddTaskUseCase) {
    //fun saveTask(taskName : String, taskDescription: String, context: Context?, taskId : Long? = null)
}

class AddEditTaskPresenter constructor(private val dataView : AddEditTaskView)
    : Presenter(useCase = AddTaskUseCase(TaskRepositoryImpl(), Task())){

    fun saveTask(task: Task) {
        do {
            try {
                //useCase.saveTask(SaveTaskSubscriber(dataView))
                TaskRepositoryImpl().saveTask(task)
                // this task already has id, just whether is *default 0 for new task* or *an old id for editing task*
            } catch (e: Error) {
                dataView.onAddTaskFail(ToastConstants.ERROR_TASK_NOT_SAVED_TO_DATABASE)
                break
            }
            dataView.onAddTaskSuccess()
        } while (false)
    }

    fun haveTaskName(taskName: String) : Boolean {
        do {
            if (taskName.isEmpty()) {
                dataView.onAddTaskFail(ToastConstants.ERROR_TASK_NAME_EMPTY)
                break
            }
            return true
        } while (false)

        return false
    }


//
//    private class SaveTaskSubscriber(private val dataView: AddEditTaskView) : DefaultSubscriber<Void>() {
//
//        override fun onNext(t: Void?) {
//            dataView.onAddTaskSuccess()
//        }
//
//        override fun onError(t: Throwable?) {
//            dataView.onAddTaskFail(ToastConstants.ERROR_TASK_NOT_SAVED_TO_DATABASE)
//        }
//
//        override fun onComplete() {
//
//        }
//    }
}