package com.example.rachelyiak.todolistkotlin.displaytasks

import android.util.Log
import com.example.rachelyiak.todolistkotlin.tasks.Task
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepositoryImpl
import io.reactivex.Observable
import io.reactivex.subscribers.DefaultSubscriber

//TODO delete if not using
interface DisplayTaskUseCase {

    fun markTask(task: Task) : Observable<Task> {

        val temp = TaskRepositoryImpl().markTask(task)
        Log.d("Use Case", temp.`as` { Task() }.name)

        return temp
    }
}