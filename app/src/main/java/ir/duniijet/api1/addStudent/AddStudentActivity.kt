package ir.duniijet.api1.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.JsonObject
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.duniijet.api1.MainRepository
import ir.duniijet.api1.databinding.ActivityMain2Binding
import ir.duniijet.api1.model.ApiService
import ir.duniijet.api1.model.Student
import ir.duniijet.api1.util.asyncRequest
import ir.duniijet.api1.util.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    private val compositeDisposable = CompositeDisposable()
    lateinit var addStudentViewModel: AddStudentViewModel
    var isInserting = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)

        addStudentViewModel = AddStudentViewModel(MainRepository())


        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()
        val testMode = intent.getParcelableExtra<Student>("student")
        if (testMode == null) {
            isInserting = true
        } else {
            isInserting = false
        }
        if (!isInserting) {
           logicUpdateStudent()

        }

        binding.btnDone.setOnClickListener {
            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent()
            }


        }


    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun logicUpdateStudent() {
        binding.btnDone.text = "update"
        val dataFromIntent = intent.getParcelableExtra<Student>("student")!!
        binding.edtScore.setText(dataFromIntent.score.toString())
        binding.edtCourse.setText(dataFromIntent.course)

        val splitedName = dataFromIntent.name.split(" ")
        binding.edtFirstName.setText(splitedName[0])
        binding.edtLastName.setText(splitedName[(splitedName.size - 1)])
    }

    private fun updateStudent() {
        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {
            addStudentViewModel
                .updateStudent(
                    Student(firstName + " " + lastName, course, score.toInt())
                )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student updated :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("error -> " + e.message ?: "null")
                    }
                })



        } else {
            showToast("لطفا اطلاعات را کامل وارد کنید")
        }


    }

    private fun addNewStudent() {


        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {

            addStudentViewModel
                .insertNewStudent(
                    Student(firstName + " " + lastName, course, score.toInt())
                )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student inserted :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("error -> " + e.message ?: "null")
                    }

                })



        } else {
            showToast("لطفا اطلاعات را وارد کنید")
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()

        }

        return true
    }
}

