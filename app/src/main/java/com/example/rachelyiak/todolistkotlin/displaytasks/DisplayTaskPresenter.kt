package com.example.rachelyiak.todolistkotlin.displaytasks

import com.example.rachelyiak.todolistkotlin.ToastConstants
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepositoryImpl
import com.example.rachelyiak.todolistkotlin.tasks.Task
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DefaultSubscriber

class DisplayTaskPresenter(private val dataView: DisplayTaskView) {

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

    fun deleteAllTask() {
        val deleteAllTaskObservable: Observable<Boolean>
        try {
            deleteAllTaskObservable = TaskRepositoryImpl().deleteAllTask()
            deleteAllTaskObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        DeleteTaskSubscriber(dataView, null).onNext(it)
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

    private class DeleteTaskSubscriber(val dataView: DisplayTaskView, val task: Task?) : DefaultSubscriber<Boolean>() {
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