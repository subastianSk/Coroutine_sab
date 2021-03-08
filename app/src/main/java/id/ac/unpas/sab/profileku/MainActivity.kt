package id.ac.unpas.sab.profileku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


class MainActivity : AppCompatActivity() {
    lateinit var scope: CoroutineScope
    private val JOB_TIMES1 = 3000
    private val JOB_TIMES2 = 4000
    private val PROGRRESS_START = 0
    private val PROGRRESS_MAX = 100
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar1.scaleY=5f
        progressBar2.scaleY=5f

        btn_start.setOnClickListener{
            scope = CoroutineScope(Dispatchers.Main)
            startTask()
        }
    }
    private fun startTask(){
        scope.launch {
            var time = measureTimeMillis {
                val result1:Deferred<Strings> = async {
                    println("Debug async 1: ${Thread.currentThread().name}")
                    getDataFromNetwork1()
                }
                val result2:Deferred<Strings> = async {
                    println("Debug async 2: ${Thread.currentThread().name}")
                    getDataFromNetwork2()
            }
                updateUI(result1.await())
                updateUI(result2.await())
        }
            println("Debug Total Ealpsed Time ${time}")
    }
    private suspend fun getDataFromNetwork1():String{
        withContext(Dispatchers.IO){
            for (i in PROGRRESS_START..PROGRRESS_MAX){
                delay((JOB_TIMES1/PROGRRESS_MAX).toLong())
                println("Debug get data from network1 :${i}: ${Thread.currentThread().name}")
                showProgressBar1(i)
            }
        }
        return "Progress Bar#1 Completed"
    }
        private suspend fun showProgressBar1(i:Int){
            withContext(Dispatchers.Main){
                progressBar1.progess=i
            }
        }
        private suspend fun getDataFromNetwork2():String{
            withContext(Dispatchers.IO){
                for (i in PROGRRESS_START..PROGRRESS_MAX){
                    delay((JOB_TIMES2/PROGRRESS_MAX).toLong())
                    println("Debug get data from network2 :${i}: ${Thread.currentThread().name}")
                    showProgressBar2(i)
                }
            }
            return "Progress Bar#2 Completed"
        }
        private suspend fun showProgressBar2(i:Int){
            withContext(Dispatchers.Main){
                progressBar1.progess=i
            }
        }
        private suspend fun updateUI(message: String){
            withContext(Dispatchers.Main){
                println("Debug update ui ${Thread.currentThread().name}")
                textResult.text=textResult.text.toString() + "\n"+message
            }
        }
}