package com.example.rachelyiak.todolistkotlin.addedittasks

import android.util.Log
import com.example.rachelyiak.todolistkotlin.tasks.Task
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepository
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepositoryImpl
import io.reactivex.subscribers.DefaultSubscriber

class AddTaskUseCase(private val repositoryImpl: TaskRepositoryImpl, private val task: Task) {

    lateinit var taskRepository : TaskRepository

    //TODO figure out how to get tasks etc to use here

    fun saveTask(subscriber: DefaultSubscriber<Void>) {

    }
}