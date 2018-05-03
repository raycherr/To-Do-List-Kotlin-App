package com.example.rachelyiak.todolistkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.rachelyiak.todolistkotlin.R.id.contentFrame
import com.example.rachelyiak.todolistkotlin.R.layout.activity_main
import com.example.rachelyiak.todolistkotlin.addedittasks.AddEditTaskFragment
import com.example.rachelyiak.todolistkotlin.displaytasks.DisplayTaskFragment
import com.example.rachelyiak.todolistkotlin.tasks.KeyConstants
import com.example.rachelyiak.todolistkotlin.tasks.Task
import io.realm.Realm

object ToastConstants{
    const val ERROR_TASK_NAME_EMPTY = "task name empty"
    const val ERROR_TASK_NOT_SAVED_TO_DATABASE = "task not saved to database"
    const val ERROR_TASK_UPDATE_FAIL = "task mark fail"
    const val ERROR_TASK_DELETE_FAIL = "task delete fail"
    const val INFO_TASK_MARKED = "task marked"
    const val INFO_TASK_DELETED = "task deleted"
}
object FragmentConstants{
    const val DISPLAY = "display"
    const val ADD = "add"
    const val EDIT = "edit"
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        replaceWithFrag() // default fragment

        Realm.init(this)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.contentFrame)
        Log.d("HELLO",frag.tag)
        if (frag.tag == FragmentConstants.DISPLAY) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    fun replaceWithFrag(fragmentName : String? = null, task : Task? = null) {
        val transaction = supportFragmentManager.beginTransaction()

        when (fragmentName) {
            FragmentConstants.ADD -> {
                val fragment = AddEditTaskFragment()

                transaction.replace(contentFrame,fragment,FragmentConstants.ADD)
                transaction.addToBackStack(FragmentConstants.ADD)
            }
            FragmentConstants.EDIT -> {
                val bundle = Bundle()
                bundle.putLong(KeyConstants.KEY_ID,task!!.id)
                bundle.putString(KeyConstants.KEY_NAME,task.name)
                bundle.putString(KeyConstants.KEY_DESCRIPTION,task.description)
                bundle.putBoolean(KeyConstants.KEY_HIGHLIGHT, task.highlight)
                //bundle.putString(KeyConstants.KEY_COLOR_TAG, task.colorTag)

                val fragment = AddEditTaskFragment()
                fragment.arguments = bundle

                transaction.replace(contentFrame,fragment,FragmentConstants.EDIT)
                transaction.addToBackStack(FragmentConstants.EDIT)
            }
            else -> { //default display task frag is shown
                transaction.replace(contentFrame, DisplayTaskFragment(),FragmentConstants.DISPLAY)
                transaction.addToBackStack(FragmentConstants.DISPLAY)
            }
        }

        transaction.commit()
    }

    fun onAddTaskSuccess() {
        Log.d("Back on main activity", "add task success, going back to default fragment")
        replaceWithFrag()
    }

    fun showToast(toastSummary : String, task : Task? = null) {
        when (toastSummary) {
            ToastConstants.ERROR_TASK_NAME_EMPTY -> Toast.makeText(this, getString(R.string.task_name_cannot_be_empty), Toast.LENGTH_SHORT).show()
            ToastConstants.ERROR_TASK_NOT_SAVED_TO_DATABASE -> Toast.makeText(this, getString(R.string.task_not_saved_try_again), Toast.LENGTH_SHORT).show()
            ToastConstants.ERROR_TASK_UPDATE_FAIL -> Toast.makeText(this, getString(R.string.task_not_updated_try_again), Toast.LENGTH_SHORT).show()
            ToastConstants.ERROR_TASK_DELETE_FAIL -> Toast.makeText(this, getString(R.string.task_not_deleted_try_again), Toast.LENGTH_SHORT).show()
            ToastConstants.INFO_TASK_DELETED -> {
                if (task != null)
                    Toast.makeText(this, getString(R.string.task_deleted, task.name), Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, getString(R.string.all_task_deleted), Toast.LENGTH_SHORT).show()
            }
            ToastConstants.INFO_TASK_MARKED -> {
                if (task!!.completed) Toast.makeText(this, getString(R.string.task_completed, task.name),Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, getString(R.string.task_ongoing, task.name),Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(this, getString(R.string.other_error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { //for settings
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //TODO delete to only appear when long click happened?
        // or should i just keep it there so it is easier to delete?
        // and show crosses on all of the tasks, while making floating action button disappear?
        return when (item.itemId) {
            R.id.action_delete_all -> {
                false
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}
