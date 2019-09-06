package project.emarge.fertilizer_manager.model.datamodel

import com.google.gson.annotations.SerializedName


data class ProductsCategory (

    @SerializedName("id")
    var productsID: Int? = null,

    @SerializedName("productCategory")
    var productCategory: String? = null,

    @SerializedName("error")
    var loginNetworkError: NetworkError = NetworkError() ){}
