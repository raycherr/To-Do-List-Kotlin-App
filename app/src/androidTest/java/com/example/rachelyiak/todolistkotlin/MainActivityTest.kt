package com.example.rachelyiak.todolistkotlin

import org.junit.After
import org.junit.Before
import org.junit.Test

import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.view.View
import android.widget.FrameLayout
import com.example.rachelyiak.todolistkotlin.addedittasks.AddEditTaskFragment
import com.example.rachelyiak.todolistkotlin.displaytasks.DisplayTaskPresenter
import com.example.rachelyiak.todolistkotlin.displaytasks.DisplayTaskView
import com.example.rachelyiak.todolistkotlin.tasks.Task
import com.example.rachelyiak.todolistkotlin.tasks.TaskRepository
import io.reactivex.Observable
import io.realm.Realm
import junit.framework.Assert.assertNotNull

import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class MainActivityTest {

    @Rule @JvmField
    val mActivityRule = ActivityTestRule(MainActivity::class.java)

    @Rule @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private var taskRepository : TaskRepository = Mockito.mock(TaskRepository::class.java)

    @Mock
    private var displayTaskView: DisplayTaskView = Mockito.mock(DisplayTaskView::class.java)

    private var task = Task()

    private var presenter : DisplayTaskPresenter? = null

    private var mActivity : MainActivity? = null

    @Before
    fun setUp() {
        this.mActivity = mActivityRule.activity
        Realm.init(mActivity!!.get())

        presenter = DisplayTaskPresenter(displayTaskView, taskRepository)
    }

    @Test //name needs to start with "test"
    fun testLaunch() {

        val container : FrameLayout? = mActivity?.findViewById(R.id.contentFrame)
        assertNotNull(container)

        val contentView : View? = mActivity?.findViewById(R.id.toolbar)
        assertNotNull(contentView)

        val buttonView : View? = mActivity?.findViewById(R.id.btn_add_task)
        assertNotNull(buttonView)

        onView(withId(R.id.btn_add_task)).perform(click())

        val fragment = AddEditTaskFragment()

        val containerId : Int = container!!.id

        mActivity?.supportFragmentManager?.beginTransaction()?.replace(containerId, fragment ,FragmentConstants.EDIT)?.commitAllowingStateLoss()

        val addTaskView : View? = mActivity?.findViewById(R.id.taskDescription)
        assertNotNull(addTaskView)
    }

    @Test
    fun testShouldCompleteMarkTask() {

        `when`(taskRepository.markTask(task)).thenReturn(Observable.just(task))

        presenter?.markTask(task)

        Thread.sleep(1000) // sleep for as codes are multithreaded, else if take longer to return then task will fail

        verify(displayTaskView).onTaskUpdated(ToastConstants.INFO_TASK_MARKED, task)
        verify(displayTaskView).onLoadTaskDisplay(taskRepository.retrieveAllTask())
    }

    @After
    fun tearDown() {
        mActivity = null
        presenter = null
    }
}