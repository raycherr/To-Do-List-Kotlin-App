package com.example.rachelyiak.todolistkotlin.tasks

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class Task(@PrimaryKey var id : Long = 0,
                var name: String = "",
                var description : String = "") : RealmObject() {

    var completed : Boolean = false
    var highlight: Boolean = false
    var colorTag : String? = null
    var orderId: Long = 0
}