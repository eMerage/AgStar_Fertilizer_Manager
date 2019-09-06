package project.emarge.fertilizer_manager.model.datamodel

import com.google.gson.annotations.SerializedName


data class Rep (

    @SerializedName("id")
    var userID: Int = 0,

    @SerializedName("name")
    var name: String = "",


    @SerializedName("email")
    var email: String = "",

    @SerializedName("imageUrl")
    var imageUrl: String = "",


    @SerializedName("status")
    var userStatus: Boolean = false,


    @SerializedName("productsList")
    var productsList: ArrayList<Products>? = ArrayList<Products>(),


    @SerializedName("error")
    var loginNetworkError: NetworkError = NetworkError() ,


    var isRepSelected: Boolean = false




){}
