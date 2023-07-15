package ir.duniijet.api1.util

import com.google.gson.JsonObject
import ir.duniijet.api1.model.Student

fun dataclassStudentToJasonObject(student:Student):JsonObject{
    val jsonObject = JsonObject()
    jsonObject.addProperty("name", student.name)
    jsonObject.addProperty("course", student.course)
    jsonObject.addProperty("score", student.score)
    return jsonObject

}