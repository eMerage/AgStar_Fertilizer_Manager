package project.emarge.fertilizer_manager.model.datamodel

import com.google.gson.annotations.SerializedName


data class Orders (

    @SerializedName("id")
    var orderID: Int? = null,

    @SerializedName("code")
    var orderCode: String? = null,

    @SerializedName("dispatchDate")
    var orderDispatchDate: String? = null,

    @SerializedName("dispatchType")
    var orderDispatchType: String? = null,

    @SerializedName("paymentType")
    var orderPaymentType: String? = null,

    @SerializedName("isOrderConfirmed")
    var isOrderConfirmed: Boolean = false,


    @SerializedName("visitProducts")
    var productsList: ArrayList<Products> = ArrayList<Products>(),

    @SerializedName("error")
    var loginNetworkError: NetworkError = NetworkError() ){}
