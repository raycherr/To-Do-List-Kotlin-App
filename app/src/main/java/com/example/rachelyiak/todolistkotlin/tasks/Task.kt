package com.example.rachelyiak.todolistkotlin.tasks

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Task(@PrimaryKey var id : Long = 0, var name: String = "", var description : String = "") : RealmObject() {
//    @PrimaryKey
    //var id : Long = 0
    //var name: String = ""
    //var description : String = ""
    var completed : Boolean = false
}