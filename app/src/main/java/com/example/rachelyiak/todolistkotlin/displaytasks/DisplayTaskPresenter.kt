package com.example.rachelyiak.todolistkotlin.displaytasks

import com.example.rachelyiak.todolistkotlin.ToastConstants
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepositoryImpl
import com.example.rachelyiak.todolistkotlin.tasks.Task
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DefaultSubscriber

//TODO edit this out (refer to addedittaskpresenter)
abstract class Presenter(val dataView: DisplayTaskView) {

    //fun loadTasks()
    //fun markTask(task: Task)
    //fun deleteTask(task: Task)
}

class DisplayTaskPresenter(dataView2: DisplayTaskView) : Presenter(dataView2) {

    fun loadTasks() {
        dataView.onLoadTaskDisplay(TaskRepositoryImpl().retrieveAllTask())
    }

    fun markTask(task : Task) {
        val markTaskObservable : Observable<Task>
        try {
            markTaskObservable = TaskRepositoryImpl().markTask(task)
            markTaskObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        MarkTaskSubscriber(dataView).onNext(it)
                    }
        } catch (e: Error) {
            dataView.onUpdateTaskFail(ToastConstants.ERROR_TASK_MARK_FAIL)
        }
    }

    fun deleteTask(task: Task) {
        val deleteTaskObservable: Observable<Boolean>
        try {
            deleteTaskObservable = TaskRepositoryImpl().deleteTask(task)
            deleteTaskObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        DeleteTaskSubscriber(dataView, task).onNext(it)
                    }
        } catch (e: Error) {
            dataView.onUpdateTaskFail(ToastConstants.ERROR_TASK_DELETE_FAIL)
        }
    }


    private class MarkTaskSubscriber(val dataView: DisplayTaskView) : DefaultSubscriber<Task?>() {
        override fun onNext(task: Task?) {
            if (task != null) {
                dataView.onTaskUpdated(ToastConstants.INFO_TASK_MARKED, task)
                DisplayTaskPresenter(dataView).loadTasks()
            } else onError(Error())
        }

        override fun onComplete() {
        }

        override fun onError(t: Throwable?) {
            dataView.onUpdateTaskFail(ToastConstants.ERROR_TASK_MARK_FAIL)
        }
    }

    private class DeleteTaskSubscriber(val dataView: DisplayTaskView, val task: Task) : DefaultSubscriber<Boolean>() {
        override fun onNext(t: Boolean) {
            dataView.onTaskUpdated(ToastConstants.INFO_TASK_DELETED, task)
            DisplayTaskPresenter(dataView).loadTasks()
        }

        override fun onComplete() {
        }

        override fun onError(t: Throwable?) {
            dataView.onUpdateTaskFail(ToastConstants.ERROR_TASK_DELETE_FAIL)
        }
    }

}