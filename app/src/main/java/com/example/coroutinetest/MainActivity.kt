package com.example.coroutinetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    // https://developer.android.com/kotlin/coroutines-adv
    // https://www.raywenderlich.com/1423941-kotlin-coroutines-tutorial-for-android-getting-started
    // Dispatchers.Main, Dispatchers.IO & Dispatchers.Default

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)
    private val scrollCoScope = CoroutineScope(Dispatchers.Main + parentJob)

    private lateinit var repeatJob: Job
    private lateinit var repeatJobTwo: Job
    private lateinit var autoScrollJob: Job

    private var num: Int = 0
    private var numTwo = 0
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStart.setOnClickListener {
            repeatJob = repeatTask()
            repeatJobTwo = repeatTaskTwo()
            autoScrollJob = autoScroll()
        }

        buttonStop.setOnClickListener {
            repeatJob.cancel()
            repeatJobTwo.cancel()
            autoScrollJob.cancel()
        }

        buttonClear.setOnClickListener {
            textViewOutput.text = ""
        }

        buttonAdd.setOnClickListener {
            count++
            textViewOutput.append("Count: $count\n")
        }

    }

    override fun onResume() {
        super.onResume()
        num = 0
        numTwo = 1000
    }

    override fun onPause() {
        super.onPause()
        repeatJob.cancel()
        repeatJobTwo.cancel()
    }

    private fun repeatTask() : Job {
        return coroutineScope.launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    textViewOutput.append("Num: $num\n")
                }
                num++
                delay(500)
            }
        }
    }

    private fun repeatTaskTwo() : Job {
        return coroutineScope.launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    textViewOutput.append("NumTwo: $numTwo\n")
                }
                numTwo++
                delay(1000)
            }
        }
    }

    private fun autoScroll() : Job {
        return scrollCoScope.launch {
            while (true) {
                scrollViewMain.fullScroll(ScrollView.FOCUS_DOWN)
                delay(1000)
            }
        }
    }

}