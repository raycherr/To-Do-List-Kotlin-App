package com.example.rachelyiak.todolistkotlin.addedittasks

interface AddEditTaskView {
    fun onAddTaskSuccess()

    fun onAddTaskFail(errorName : String)
}