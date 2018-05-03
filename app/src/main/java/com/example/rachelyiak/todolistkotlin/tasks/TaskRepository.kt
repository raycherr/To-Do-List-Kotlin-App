package com.example.rachelyiak.todolistkotlin.tasks

import io.reactivex.Observable


interface TaskRepository {
    fun saveTask(task : Task)

    fun retrieveAllTask() : List<Task>

    fun markTask(task : Task) : Observable<Task>

    fun deleteTask(task: Task) : Observable<Boolean>

    fun deleteAllTask() : Observable<Boolean>

    fun swapPosition(fromPositionTask: Task, toPositionTask: Task)
}