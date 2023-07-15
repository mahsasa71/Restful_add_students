package ir.duniijet.api1

import io.reactivex.Completable
import io.reactivex.Single
import ir.duniijet.api1.model.ApiService
import ir.duniijet.api1.model.Student
import ir.duniijet.api1.util.BASE_URL
import ir.duniijet.api1.util.dataclassStudentToJasonObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }
    fun getAllStudents(): Single<List<Student>> {
        return apiService.getAllStudents()
    }

    fun insertStudent(student: Student): Completable {
        return apiService.insertStudent(dataclassStudentToJasonObject(student))
    }

    fun updateStudent(student: Student): Completable {
        return apiService.updateStudent(student.name, dataclassStudentToJasonObject(student))
    }

    fun removeStudent(studentName: String): Completable {
        return apiService.deleteStudent(studentName)
    }
}