package project.emarge.fertilizer_manager.model.datamodel

import com.google.gson.annotations.SerializedName


data class Visits (

    @SerializedName("id")
    var visitsID: Int? = null,

    @SerializedName("code")
    var visitsCode: String? = null,

    @SerializedName("createdDate")
    var visitsDate: String? = null,

    @SerializedName("user")
    var visitsRep: Rep = Rep(),

    @SerializedName("dealerName")
    var visitsDealerName: String? = null,

    @SerializedName("dealerCode")
    var visitsDealerCode: String? = null,


    @SerializedName("visitPurpose")
    var visitsPurpose: ArrayList<Purpose>? = ArrayList<Purpose>(),


    @SerializedName("order")
    var visitsOrder: Orders = Orders(),

    @SerializedName("error")
    var loginNetworkError: NetworkError = NetworkError() ){}
