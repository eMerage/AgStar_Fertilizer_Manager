package project.emarge.fertilizer_manager.viewModels.visits

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observer
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import emarge.project.caloriecaffe.network.api.APIInterface
import emarge.project.caloriecaffe.network.api.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import project.emarge.fertilizer_manager.model.datamodel.Image
import project.emarge.fertilizer_manager.model.datamodel.Visits
import project.emarge.fertilizer_manager.services.network.NetworkErrorHandler
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class VisitsRepo(application: Application) {


    var app: Application = application
    var networkErrorHandler: NetworkErrorHandler = NetworkErrorHandler()

    var apiInterface: APIInterface = ApiClient.client(application)


    var encryptedPreferences: EncryptedPreferences =
        EncryptedPreferences.Builder(application).withEncryptionPassword("122547895511").build()
    private val USER_ID = "userID"

    val userID = encryptedPreferences.getInt(USER_ID, 0)



    fun geVisits(lod : ObservableField<Boolean>): MutableLiveData<ArrayList<Visits>> {
        val dataVisits = MutableLiveData<ArrayList<Visits>>()
        var listVisits = ArrayList<Visits>()

        lod.set(true)

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                .show()
        } else {

        }

        apiInterface.getVisitsByAdmin(userID)
                .subscribeOn(Schedulers.newThread())
                .doOnError { it }
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Visits>> {
                    override fun onSubscribe(d: Disposable) {
                    }
                    override fun onNext(log: ArrayList<Visits>) {
                        listVisits = log

                    }
                    override fun onError(e: Throwable) {
                        lod.set(false)
                        Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                        lod.set(false)
                        dataVisits.postValue(listVisits)
                    }
                })




        return dataVisits
    }





    fun visitsFilter(sDate : String , eDate : String,list : ArrayList<Visits>): MutableLiveData<ArrayList<Visits>> {
        val dataVisits = MutableLiveData<ArrayList<Visits>>()
        var listVisits = ArrayList<Visits>()

        var listVi = ArrayList<Visits>()

        if (sDate != "" && eDate == "") {
            for(items in list){
                if(items.visitsDate?.substring(0,10).equals(sDate)){ listVisits.add(items) }else{ }
            }
            dataVisits.postValue(listVisits)
        }else if(sDate != "" && eDate != ""){

            var oldFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = oldFormat.parse(eDate)
            val cal = Calendar.getInstance()
            cal.time = date




            lateinit var dateStart: Date
            lateinit var dateEnd:Date
            try {
                dateStart = oldFormat.parse(sDate)
                dateEnd = oldFormat.parse(eDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            var calendar = GregorianCalendar()
            calendar.time = dateStart



            var endCalendar = GregorianCalendar()
            endCalendar.time = dateEnd


            var datesInRange = ArrayList<Date>()


         while (endCalendar.after(calendar)) {
                val result = endCalendar.time
                datesInRange.add(result)
                for(items in list){
                    if(items.visitsDate?.substring(0,10).equals(oldFormat.format(result))){
                        listVisits.add(items)
                    }else{ }
                }
             endCalendar.add(Calendar.DATE, -1)
            }

            dataVisits.postValue(listVisits)
        }
        return dataVisits
    }



    fun getMissingImagesFromServer() :MutableLiveData<Int>{

        val imagecount = MutableLiveData<Int>()

        if (!app.isConnectedToNetwork()) {

        } else {
            var listImages = ArrayList<Image>()

            apiInterface.getmissingImages(userID)
                .subscribeOn(Schedulers.newThread())
                .doOnError { it }
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ArrayList<Image>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(log: ArrayList<Image>) {
                        listImages = log

                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        imagecount.postValue(listImages.size)
                        getImages(listImages)
                    }
                })

        }

        return  imagecount

    }

    fun getImages(listImages: ArrayList<Image>) {

      try {
          var misingImagesPath = ArrayList<Image>()
          var arrIntranalImages = getImagesFromIntranalStorage()
          for ((index, item) in arrIntranalImages.withIndex()) {
              val file = File(arrIntranalImages[index])
              for(imagess in listImages){
                  if(file.name == imagess.name){
                      misingImagesPath.add(Image(imagess.imageID,imagess.name,arrIntranalImages[index].toString(),imagess.imageCode))
                  }else{

                  }
              }
          }

          val storageDir: File = app?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
          if(storageDir == null){
          }else{
              var filePathOfImage = storageDir.path
              for(item in  storageDir.list()){
                  for(imagess in listImages){
                      if(item == imagess.name){
                          var path = "$filePathOfImage/$item"
                          misingImagesPath.add(Image(imagess.imageID,imagess.name,path,imagess.imageCode))
                      }else{

                      }
                  }

              }
          }

          uploademissingImagesToServer(misingImagesPath)
      }catch (ex : Exception){ }
    }

    fun uploademissingImagesToServer(list: ArrayList<Image>){
        for(imagemissing in list){
            val file = File(imagemissing.imageUrl)
            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("imageFile", file.name, requestBody)
            apiInterface.saveImageFile(fileToUpload, imagemissing.imageCode)
                .subscribeOn(Schedulers.io())
                .doOnError {}
                .repeat(5)
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Image> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(log: Image) {

                    }
                    override fun onError(e: Throwable) {
                        Toast.makeText(app, networkErrorHandler(e).errorMessage, Toast.LENGTH_LONG).show()

                    }
                    override fun onComplete() {
                    }
                })
        }

    }

    private fun getImagesFromIntranalStorage(): Array<String?> {
        val proj = arrayOf<String>(MediaStore.Images.Media.DATA)
        val orderBy = MediaStore.Images.Media._ID

        var cursor = app?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            proj,
            null,
            null,
            orderBy
        )
        var count = cursor?.count
        var arrPath = arrayOfNulls<String>(count!!)
        for (i in 0 until count) {
            cursor?.moveToPosition(i)
            val dataColumnIndex = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
            arrPath[i] = cursor?.getString(dataColumnIndex!!)

        }
        return arrPath
    }









    fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }


}