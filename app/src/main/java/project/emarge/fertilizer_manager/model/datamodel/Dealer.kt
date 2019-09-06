package project.emarge.fertilizer_manager.model.datamodel

import android.net.Uri
import com.google.gson.annotations.SerializedName


data class Dealer (


        @SerializedName("id")
    var dealerID: Int? = null,

        @SerializedName("name")
    var dealerName: String? = null,

        @SerializedName("code")
    var dealerCode: String? = null,

        @SerializedName("contactNo")
    var dealerContactNumber: String? = null,


        @SerializedName("imageUrl")
    var dealerImg: String = "",


        @SerializedName("latitude")
    var dealerLocationLan: Double? = null,


        @SerializedName("longtitude")
    var dealerLocationLon: Double? = null,

        @SerializedName("status")
    var status: Boolean = false,

        @SerializedName("user")
    var dealersRep: Rep = Rep(),


        @SerializedName("imageCode")
        var dealerimageCode: String? = null,




        @SerializedName("error")
    var loginNetworkError: NetworkError = NetworkError(),


        var dealerImageCode: String = "",
        var dealerImage: Uri = Uri.EMPTY,
        var dealerImageName: String = "",
        var dealerImagePath: String = "",
        var isImageFromCamera: Boolean = false
    
    ){}
