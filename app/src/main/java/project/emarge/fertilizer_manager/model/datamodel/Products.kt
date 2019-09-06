package project.emarge.fertilizer_manager.model.datamodel

import com.google.gson.annotations.SerializedName


data class Products (

    @SerializedName("id")
    var productsID: Int? = null,

    @SerializedName("code")
    var productsCode: String? = null,

    @SerializedName("name")
    var productsName: String? = null,

    @SerializedName("quantity")
    var productsQTy: Int? = null,

    @SerializedName("imageUrl")
    var productsImg: String? = null,

    @SerializedName("error")
    var loginNetworkError: NetworkError = NetworkError() ){}
