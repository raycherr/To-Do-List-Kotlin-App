package com.example.rachelyiak.todolistkotlin.tasks

import android.util.Log
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableError
import io.realm.Realm
import org.reactivestreams.Subscriber

class TaskRepositoryImpl : TaskRepository {

    var realm : Realm? = Realm.getDefaultInstance()

    override fun saveTask(task : Task) {

        if (task.id == 0.toLong()) {
            //id == 0 means default new item coming in
            var nextId = 1
            if (realm?.where(Task::class.java)?.max(KeyConstants.KEY_ID) != null) {
                nextId = realm?.where(Task::class.java)?.max(KeyConstants.KEY_ID).toString().toInt() +1
            }
            task.id = nextId.toLong()
        }

        realm?.executeTransaction({
            realm?.copyToRealmOrUpdate(task)
        })
    }


    override fun retrieveAllTask(): List<Task> {
        val taskRealmResults = realm?.where(Task::class.java)?.findAll()?.sort(KeyConstants.KEY_ID)
        Log.d("HELLO", taskRealmResults.toString())
        return realm?.copyFromRealm(taskRealmResults!!) as List<Task>
    }

    override fun markTask(task: Task) : Observable<Task> {
        val taskRealmObj = realm?.where(Task::class.java)?.equalTo(KeyConstants.KEY_ID,task.id)?.findFirst()


        //TODO is it okay to leave observable like this? i think need to catch exception like in deleteTask
        realm?.executeTransaction({
            taskRealmObj?.completed = !taskRealmObj!!.completed
            realm?.copyToRealmOrUpdate(taskRealmObj)
        })

        return Observable.just(taskRealmObj)
    }

    override fun deleteTask(task: Task) : Observable<Boolean> {
        val taskRealmObj = realm?.where(Task::class.java)?.equalTo(KeyConstants.KEY_ID,task.id)?.findFirst()

        try {
            realm?.executeTransaction {
                taskRealmObj?.deleteFromRealm()
            }
        } catch (e : Error) {
            return Observable.just(false)
        }

        return Observable.just(true)
    }
}