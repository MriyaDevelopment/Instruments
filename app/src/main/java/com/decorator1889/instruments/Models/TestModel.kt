package com.decorator1889.instruments.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class TestModel : RealmObject() {
    @Expose
    @SerializedName("id")
    var id = 0

    @Expose
    @SerializedName("question")
    var question = ""

    @Expose
    @SerializedName("type")
    var type = ""

    @Expose
    @SerializedName("answer_one")
    var answer_one = ""

    @Expose
    @SerializedName("answer_two")
    var answer_two = ""

    @Expose
    @SerializedName("answer_three")
    var answer_three = ""

    @Expose
    @SerializedName("true_answer")
    var true_answer = ""
}