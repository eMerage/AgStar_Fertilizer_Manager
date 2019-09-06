package project.emarge.fertilizer_manager.model.datamodel

import com.google.gson.annotations.SerializedName

data class Purpose  (
    @SerializedName("id")
    var purposeID: Int = 0,

    @SerializedName("code")
    var purposeCode: String = "",

    @SerializedName("name")
    var purposeName: String = ""

){}
