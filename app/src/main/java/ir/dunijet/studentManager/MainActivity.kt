package ir.dunijet.studentManager

import android.annotation.SuppressLint
import android.content.Intent
import android.os.BaseBundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import ir.duniijet.api1.databinding.ActivityMainBinding


import ir.dunijet.studentManager.net.ApiService
import ir.dunijet.studentManager.recycler.Student
import ir.dunijet.studentManager.recycler.StudentAdapter
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Headers

const val BASE_URL = "http://192.168.132.36:8080"

class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: StudentAdapter
    lateinit var apiService: ApiService

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)


        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()

        getDataFromApi()
    }

    fun getDataFromApi() {

        apiService.getAllStudents().enqueue(object : Callback<List<Student>> {
            override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {

                val dataFromServer = response.body()!!
                setDataToRecycler(dataFromServer)

            }

            override fun onFailure(call: Call<List<Student>>, t: Throwable) {
                Log.v("testApi", t.message!!)
            }

        })

    }

    fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemClicked(student: Student, position: Int) {
        updateDataInServer(student, position)
    }

    override fun onItemLongClicked(student: Student, position: Int) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.contentText = "Delete this Item?"
        dialog.cancelText = "cancel"
        dialog.confirmText = "confirm"
        dialog.setOnCancelListener {
            dialog.dismiss()
        }
        dialog.setConfirmClickListener {

            deleteDataFromServer(student, position)
            dialog.dismiss()

        }
        dialog.show()
    }

    private fun deleteDataFromServer(student: Student, position: Int) {

        apiService
            .deleteStudent(student.name)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                }
            })

        myAdapter.removeItem(student, position)

    }

    private fun updateDataInServer(student: Student, position: Int) {

        val intent = Intent(this, MainActivity2::class.java)
        intent.putExtra("student", student)
        startActivity(intent)

    }


}