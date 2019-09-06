package project.emarge.fertilizer_manager.viewModels.dealers

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import emarge.project.caloriecaffe.network.api.APIInterface
import emarge.project.caloriecaffe.network.api.ApiClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

import project.emarge.fertilizer_manager.BuildConfig
import project.emarge.fertilizer_manager.model.datamodel.Dealer
import project.emarge.fertilizer_manager.model.datamodel.Image
import project.emarge.fertilizer_manager.model.datamodel.Rep
import project.emarge.fertilizer_manager.services.network.NetworkErrorHandler
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class DealerRepo(application: Application) {

    val tokenID = BuildConfig.RESOURCE_ID
    var app: Application = application
    var networkErrorHandler: NetworkErrorHandler = NetworkErrorHandler()

    var apiInterface: APIInterface = ApiClient.client(application)

    var encryptedPreferences: EncryptedPreferences =
            EncryptedPreferences.Builder(application).withEncryptionPassword("122547895511").build()
    private val USER_ID = "userID"

    val userID = encryptedPreferences.getInt(USER_ID, 0)


    fun updateDealerLocation(
            location: LatLng,
            dealer: Dealer, selectedImagefilePath: Uri, phoneNumber: String,isCam : Boolean
    ): MutableLiveData<Dealer> {


        val data = MutableLiveData<Dealer>()
        var dealerObject = Dealer()

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "You need internet connection to process this", Toast.LENGTH_LONG).show()

        } else if( (!phoneNumber.isNullOrEmpty())  && (phoneNumber.length != 11) ){
            Toast.makeText(app, "Invalid phone number ex - 94711111111", Toast.LENGTH_LONG).show()
        }else {

            var filePath: String = ""
            var imageCode: String = ""

            val jsonObject = JsonObject()
            jsonObject.addProperty("ID", dealer.dealerID)
            jsonObject.addProperty("Latitude", location.latitude)
            jsonObject.addProperty("Longtitude", location.longitude)



            if (selectedImagefilePath == Uri.EMPTY) {

            } else {
                imageCode = genarateImageCode()
                dealer.dealerImage = selectedImagefilePath
                dealer.dealerImageCode = imageCode
                dealer.isImageFromCamera = isCam

                try {
                    filePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(isCam){
                                selectedImagefilePath.path.toString()
                            }else{
                                addImagesUpKitKat(selectedImagefilePath)
                            }
                        }else{
                            addImagesUpKitKat(selectedImagefilePath)
                        }
                    }else{
                        addImages(selectedImagefilePath)
                    }
                }catch (ex : Exception){

                }


                if (filePath == "") {

                } else {
                    val file = File(filePath)
                    dealer.dealerImageName = file.name
                    dealer.dealerImagePath = filePath
                }

            }
            jsonObject.addProperty("ImageCode", imageCode)
            jsonObject.addProperty("ContactNo", phoneNumber)




            if(  (selectedImagefilePath != Uri.EMPTY)   &&   (filePath == "")  ){
                Toast.makeText(app, "Image capture error,Please try again", Toast.LENGTH_LONG).show()
            }else{


                apiInterface!!.updateDealer(jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Dealer> {
                        override fun onSubscribe(d: Disposable) {}
                        override fun onNext(log: Dealer) {
                            dealerObject = log
                        }

                        override fun onError(e: Throwable) {
                            Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                        }

                        override fun onComplete() {
                            data.postValue(dealerObject)
                            savePaymentImageDetails(dealer)
                        }
                    })

            }



        }

        return data
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun addImagesUpKitKat(data: Uri) : String{
        var filep : String = ""
        if (data == null) {
            Toast.makeText(app, "Please select image from gallery", Toast.LENGTH_LONG).show()
        } else {
            filep = getPath(app,data)
        }
        return filep
    }



    private fun addImages(data: Uri?) : String{
        var filep : String = ""
        if (data == null) {
            Toast.makeText(app, "Please select image from gallery", Toast.LENGTH_LONG).show()
        } else {
            filep = getRealPathFromURI(app,data)
        }
        return filep

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getPath(context:Context, uri:Uri):String {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri))
        {
            if (isExternalStorageDocument(uri))
            {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true))
                {
                    return (Environment.getExternalStorageDirectory().toString() + "/" + split[1])
                }
            }
            else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                return getDataColumn(context, contentUri, null, null)
            }
            else if (isMediaDocument(uri))
            {
                var docId = DocumentsContract.getDocumentId(uri)
                var split = docId.split((":").toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                var type = split[0]
                var contentUri:Uri = Uri.EMPTY
                when (type) {
                    "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf<String>(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }

        }
        else if ("content".equals(uri.scheme, ignoreCase = true))
        {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.lastPathSegment.toString()
            return this.getDataColumn(context, uri, null, null).toString()
        }
        else if ("file".equals(uri.scheme, ignoreCase = true))
        {
            return uri.path.toString()
        }

        return ""
    }


    fun getDataColumn(context:Context, uri:Uri?,selection:String?, selectionArgs:Array<String>?): String {

        lateinit var cursor:Cursor
        val column = "_data"
        val projection = arrayOf<String>(column)
        try
        {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)!!
            if (cursor != null && cursor.moveToFirst())
            {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        }
        finally
        {
            cursor.close()
        }
        return ""
    }

    private fun isExternalStorageDocument(uri:Uri):Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }
    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri:Uri):Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }
    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri:Uri):Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
    /**
     * @param uri - The Uri to check.
     * @return - Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri:Uri):Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }


    fun savePaymentImageDetails(dealer: Dealer) {
        if (!app.isConnectedToNetwork()) {
        } else {
            val locJsonArr = JsonArray()

            if (dealer.dealerImageCode.isNullOrEmpty()) {
            } else {

                val ob = JsonObject()
                ob.addProperty("ImageCode", dealer.dealerImageCode)
                ob.addProperty("ImageTypeID", 4)
                ob.addProperty("Name", dealer.dealerImageName)
                locJsonArr.add(ob)

                println("wwwwwwwwwwwwww ddddddd: "+locJsonArr)


                apiInterface.saveImageDetails(locJsonArr)
                        .subscribeOn(Schedulers.io())
                        .doOnError { }
                        .doOnTerminate { }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<Image> {
                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(log: Image) {
                            }

                            override fun onError(e: Throwable) {
                                Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()

                            }

                            override fun onComplete() {
                                Toast.makeText(app, "Image Details Upload Complete", Toast.LENGTH_LONG).show()
                                savePaymentImage(dealer)
                            }
                        })


            }


        }

    }


    fun savePaymentImage(dealer: Dealer) {

        val file = File(dealer.dealerImagePath)
        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("imageFile", file.name, requestBody)


        apiInterface.saveImageFile(fileToUpload, dealer.dealerImageCode)
                .subscribeOn(Schedulers.io())
                .doOnError {   println("ssssssssssssss : saveImageFile  doOnError") }
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Image> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(log: Image) {
                        println("ssssssssssssss : saveImageFile  onNext")
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(app, networkErrorHandler(e).errorMessage, Toast.LENGTH_LONG).show()

                        println("ssssssssssssss : saveImageFile  onError "+e.message)
                    }

                    override fun onComplete() {
                        println("ssssssssssssss : saveImageFile  onComplete")
                    }
                })


    }


    fun getRepsToAssignDealers(): MutableLiveData<ArrayList<Rep>> {

        val repsToAssignDealers = MutableLiveData<ArrayList<Rep>>()
        var listReps = ArrayList<Rep>()



        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG).show()
        } else {

        }

        apiInterface.getRepsByAdmin(userID)
                .subscribeOn(Schedulers.io())
                .doOnError { it }
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Rep>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(log: ArrayList<Rep>) {
                        listReps = log

                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                        repsToAssignDealers.postValue(listReps)

                    }
                })

        return repsToAssignDealers

    }


    fun getAssignDealers(): MutableLiveData<ArrayList<Dealer>> {
        val dataAssignDealers = MutableLiveData<ArrayList<Dealer>>()

        var dealerListApprovedDealers = ArrayList<Dealer>()


        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                    .show()
        } else {

        }

        apiInterface.getAssignDealers(userID)
                .subscribeOn(Schedulers.io())
                .doOnError { it }
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Dealer>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(log: ArrayList<Dealer>) {
                        dealerListApprovedDealers = log
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                        dataAssignDealers.postValue(dealerListApprovedDealers)

                    }
                })


        return dataAssignDealers

    }


    fun getUnApprovedDealers(progressBar: ProgressBar): MutableLiveData<ArrayList<Dealer>> {
        val data = MutableLiveData<ArrayList<Dealer>>()
        var dealerList = ArrayList<Dealer>()

        progressBar.visibility = View.VISIBLE

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                    .show()
        } else {

        }

        apiInterface.getUnapprovedDealers(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Dealer>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(log: ArrayList<Dealer>) {
                        dealerList = log

                    }

                    override fun onError(e: Throwable) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                        progressBar.visibility = View.GONE
                        data.postValue(dealerList)

                    }
                })

        return data
    }


    fun getApprovedDealers(pro : ProgressBar): MutableLiveData<ArrayList<Dealer>> {

        val dataApprovedDealers = MutableLiveData<ArrayList<Dealer>>()

        var dealerListApprovedDealers = ArrayList<Dealer>()

        pro.visibility = View.GONE
        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                    .show()
        } else {

        }

        apiInterface.getApprovedDealers(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Dealer>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(log: ArrayList<Dealer>) {
                        dealerListApprovedDealers = log

                    }

                    override fun onError(e: Throwable) {
                        pro.visibility = View.GONE
                        Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                        pro.visibility = View.GONE
                        dataApprovedDealers.postValue(dealerListApprovedDealers)
                    }
                })


        return dataApprovedDealers
    }


    fun submitAssignDealer(rep: Rep, dealer: Dealer): MutableLiveData<Rep> {

        val dataSubmitAssignProducts = MutableLiveData<Rep>()
        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection, Please try again", Toast.LENGTH_LONG).show()
        } else {


            val jsonObject = JsonObject()
            jsonObject.addProperty("DealerID", dealer.dealerID)
            jsonObject.addProperty("CreatedByID", userID.toString())
            jsonObject.addProperty("ID", rep.userID)


            apiInterface.assignDealerToRep(jsonObject)
                    .subscribeOn(Schedulers.io())
                    .doOnError { it }
                    .doOnTerminate { }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<Rep> {
                        override fun onSubscribe(d: Disposable) {

                        }

                        override fun onNext(log: Rep) {
                            dataSubmitAssignProducts.postValue(log)
                        }

                        override fun onError(e: Throwable) {

                            Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                        }

                        override fun onComplete() {

                        }
                    })

        }

        return dataSubmitAssignProducts

    }

    fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }

    fun genarateImageCode(): String {
        val c = Calendar.getInstance()
        val numberFromTime = c.get(Calendar.YEAR).toString() + c.get(Calendar.DATE).toString() + c.get(Calendar.HOUR).toString() + c.get(Calendar.MINUTE).toString() + c.get(Calendar.SECOND).toString() + c.get(Calendar.MILLISECOND).toString()
        val ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        var count = 3
        val builder = StringBuilder()
        while (count-- != 0) {
            val character = (Math.random() * ALPHA_NUMERIC_STRING.length).toInt()
            builder.append(ALPHA_NUMERIC_STRING[character])

        }
        return userID.toString() + numberFromTime + builder.toString()
    }


    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)!!
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()

            return if (cursor.getString(column_index) == null) {
                ""
            } else {
                cursor.getString(column_index)
            }

        } finally {
            cursor?.close()
        }
    }


}