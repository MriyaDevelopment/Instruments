package com.decorator1889.instruments.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject


open class InstrumentModel : RealmObject() {

    @Expose
    @SerializedName("id")
    var id = 0

    @Expose
    @SerializedName("instrument_name")
    var instrument_name = ""

    @Expose
    @SerializedName("type")
    var type = ""

    @Expose
    @SerializedName("image")
    var image = ""

    @Expose
    @SerializedName("description")
    var description = ""

}