package com.example.rachelyiak.todolistkotlin.displaytasks

import com.example.rachelyiak.todolistkotlin.tasks.Task

interface DisplayTaskView {
    fun onLoadTaskDisplay(list : List<Task>)

    fun onUpdateTaskFail(errorName : String)

    fun onTaskUpdated(infoName: String, task: Task?)
}