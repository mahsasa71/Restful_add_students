package ir.duniijet.api1.mainScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.duniijet.api1.MainRepository
import ir.duniijet.api1.addStudent.AddStudentActivity
import ir.duniijet.api1.databinding.ActivityMainBinding
import ir.duniijet.api1.mainScreen.Main.MainScreenViewModel
import ir.duniijet.api1.model.Student
import ir.duniijet.api1.util.asyncRequest
import ir.duniijet.api1.util.showToast


class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {
    lateinit var binding: ActivityMainBinding
    lateinit var myAdapter: StudentAdapter
    private val composeDisposable=CompositeDisposable()
    lateinit var mainScreenViewModel:MainScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        mainScreenViewModel= MainScreenViewModel(MainRepository())

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this,AddStudentActivity::class.java)
            startActivity(intent)

            composeDisposable.add(
                mainScreenViewModel.progressBarSubject.subscribe(){
                    if (it){
                        runOnUiThread {
                            binding.progressMain.visibility=View.VISIBLE
                            binding.recyclerMain.visibility=View.INVISIBLE
                        }

                    }
                    else{
                        runOnUiThread {
                            binding.progressMain.visibility=View.INVISIBLE
                            binding.recyclerMain.visibility=View.VISIBLE
                        }

                    }
                }
            )
        }


    }

    override fun onResume() {
        super.onResume()

        mainScreenViewModel.getAllStudents()
            .asyncRequest()
            .subscribe(object :SingleObserver<List<Student>>{
                override fun onSubscribe(d: Disposable) {
                    composeDisposable.add(d)

                }

                override fun onError(e: Throwable) {
                    showToast("error->"+e.message ?:"null")

                }

                override fun onSuccess(t: List<Student>) {
                    setDataToRecycler(t)
                }

            })


    }

    override fun onDestroy() {
        composeDisposable.clear()
        super.onDestroy()
    }



    fun setDataToRecycler(data: List<Student>) {
        val myData = ArrayList(data)
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onItemClicked(student: Student, position: Int) {
        val intent = Intent(this, AddStudentActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)

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

        mainScreenViewModel.removeStudent(student.name)
            .asyncRequest()
            .subscribe(object :CompletableObserver{
                override fun onSubscribe(d: Disposable) {

                    composeDisposable.add(d)


                }

                override fun onComplete() {
                    showToast("student removed")

                }

                override fun onError(e: Throwable) {
                    showToast("error->"+e.message?:"null")
                }

            })

        myAdapter.removeItem(student, position)

    }




    }
