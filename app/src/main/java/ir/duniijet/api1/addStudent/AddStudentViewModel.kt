package ir.duniijet.api1.addStudent

import io.reactivex.Completable
import ir.duniijet.api1.MainRepository
import ir.duniijet.api1.model.Student

class AddStudentViewModel    (private val mainRepository: MainRepository) {


    fun insertNewStudent(student: Student): Completable {
        return mainRepository.insertStudent(student)
    }

    fun updateStudent(student: Student): Completable {
        return mainRepository.updateStudent(student)
    }

}