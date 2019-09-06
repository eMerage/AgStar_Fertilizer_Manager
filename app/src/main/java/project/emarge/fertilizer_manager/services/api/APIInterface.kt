package emarge.project.caloriecaffe.network.api





import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody

import project.emarge.fertilizer_manager.model.datamodel.*
import retrofit2.http.*
import java.util.ArrayList


/**
 * Created by Himanshu on 9/6/19.
 */
interface APIInterface {

    @GET("User/ValidateUser")
    abstract fun validateUser(
        @Query("username") username: String, @Query("password") password: String, @Query("usertypeID") usertypeID: Int, @Query(
            "pushtokenid"
        ) pushtokenid: String
    ): Observable<Rep>



    @GET("Dealer/GetUnapprovedDealersByAdmin")
     fun getUnapprovedDealers(@Query("adminID") adminID: Int):  Observable<ArrayList<Dealer>>

    @GET("Dealer/GetApprovedDealersByAdmin")
     fun getApprovedDealers(@Query("adminID") adminID: Int):  Observable<ArrayList<Dealer>>


    @GET("Dealer/GetApprovedDealersByAdminWithRep")
    fun getAssignDealers(@Query("adminID") adminID: Int):  Observable<ArrayList<Dealer>>


    @POST("Dealer/UpdateDealer")
    abstract fun updateDealer(@Body dealerInfo: JsonObject): Observable<Dealer>



    @GET("User/GetRepsByAdminProductsAssigned")
    fun getRepsByAdminProductsAssigned(@Query("adminID") adminID: Int):  Observable<ArrayList<Rep>>




    @GET("User/GetRepsByAdmin")
    fun getRepsByAdmin(@Query("adminID") adminID: Int):  Observable<ArrayList<Rep>>


    @GET("Product/GetProductCategories")
    fun getProductCategories(@Query("TokenID") tokenID: Int):  Observable<ArrayList<ProductsCategory>>


    @GET("Product/GetProducts")
    fun getProducts(@Query("categoryID") categoryID: Int,@Query("userID") userID: Int):  Observable<ArrayList<Products>>


    @POST("User/AssignProductsToRep")
    abstract fun assignProductsToRep(@Body nfo: JsonObject): Observable<Boolean>


    @POST("User/AssignDealerToRep")
    abstract fun assignDealerToRep(@Body nfo: JsonObject): Observable<Rep>


    @GET("Visit/GetVisitsByAdmin")
    fun getVisitsByAdmin(@Query("adminID") adminID: Int):  Observable<ArrayList<Visits>>


    @POST("Image/SaveImageDetails")
    abstract fun saveImageDetails(@Body nfo: JsonArray): Observable<Image>



    @Multipart
    @POST("Image/SaveImageFile")
    fun saveImageFile(@Part imageFile : MultipartBody.Part, @Query("imageCode") code: String): Observable<Image>


    @GET("Image/GetMissingImagesByUser")
    fun getmissingImages(@Query("userID") userID: Int):  Observable<ArrayList<Image>>



}
