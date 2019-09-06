package project.emarge.fertilizer_manager.viewModels.products

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pddstudio.preferences.encrypted.EncryptedPreferences
import emarge.project.caloriecaffe.network.api.APIInterface
import emarge.project.caloriecaffe.network.api.ApiClient
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import project.emarge.fertilizer_manager.BuildConfig
import project.emarge.fertilizer_manager.model.datamodel.*

import project.emarge.fertilizer_manager.services.network.NetworkErrorHandler

class ProductsRepo(application: Application) {

    val tokenID = BuildConfig.RESOURCE_ID
    var app: Application = application
    var networkErrorHandler: NetworkErrorHandler = NetworkErrorHandler()

    var apiInterface: APIInterface = ApiClient.client(application)

    var encryptedPreferences: EncryptedPreferences =
        EncryptedPreferences.Builder(application).withEncryptionPassword("122547895511").build()
    private val USER_ID = "userID"

    val userID = encryptedPreferences.getInt(USER_ID, 0)

    val addedProducts = MutableLiveData<ArrayList<Products>>()



    fun getAssignedProducts(progressBar: ProgressBar,com : CompositeDisposable): MutableLiveData<ArrayList<Rep>> {
        var data= MutableLiveData<ArrayList<Rep>>()
        var listReps = ArrayList<Rep>()

        progressBar.visibility =View.VISIBLE

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                .show()
        } else {

        }

        apiInterface.getRepsByAdminProductsAssigned(userID)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ArrayList<Rep>> {
                override fun onSubscribe(d: Disposable) {
                    com.add(d)
                }
                override fun onNext(log: ArrayList<Rep>) {
                    listReps = log
                }

                override fun onError(e: Throwable) {
                    progressBar.visibility =View.GONE
                    Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                }

                override fun onComplete() {
                    data?.postValue(listReps)
                    progressBar.visibility =View.GONE

                }
            })

        return data!!
    }





    fun addProducts(products: Products, addedProduct: ArrayList<Products>): MutableLiveData<ArrayList<Products>> {

        if (addedProduct.contains(products)) {
            Toast.makeText(app, "Product Already added", Toast.LENGTH_LONG).show()
        } else {
            addedProduct.add(products)
            var listReps = ArrayList<Products>()
            listReps = addedProduct
            addedProducts.postValue(listReps)
        }
        return addedProducts

    }


    fun submitAssignProducts(
        rep: Rep,
        addedProduct: ArrayList<Products>,
        progressBar: ProgressBar
    ): MutableLiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        var respondData = false


        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection, Please try again", Toast.LENGTH_LONG).show()
        } else {
            progressBar.visibility = View.VISIBLE

            val jsonObject = JsonObject()
            jsonObject.addProperty("UserID", rep.userID.toString())
            jsonObject.addProperty("AssignedByID", userID.toString())
            val locJsonArr = JsonArray()
            for (item in addedProduct) {
                val ob = JsonObject()
                ob.addProperty("ID", item.productsID)
                locJsonArr.add(ob)
            }
            jsonObject.add("Products", locJsonArr)


            apiInterface.assignProductsToRep(jsonObject)
                .subscribeOn(Schedulers.io())
                .doOnError { it }
                .doOnTerminate { }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Boolean> {
                    override fun onSubscribe(d: Disposable) {

                    }
                    override fun onNext(log: Boolean) {
                        respondData = log
                    }
                    override fun onError(e: Throwable) {
                        progressBar.visibility = View.VISIBLE
                        Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                    }

                    override fun onComplete() {
                        progressBar.visibility = View.GONE
                        data.postValue(respondData)
                    }
                })

        }




        return data

    }


    fun getProducts( cat: Int): MutableLiveData<ArrayList<Products>> {
        val datagetProducts = MutableLiveData<ArrayList<Products>>()
        var listPro = ArrayList<Products>()

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                .show()
        } else {

        }

        apiInterface.getProducts(cat, 0)
            .subscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ArrayList<Products>> {
                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(log: ArrayList<Products>) {
                    listPro = log
                }
                override fun onError(e: Throwable) {
                    Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                }

                override fun onComplete() {

                    Handler().postDelayed(Runnable {
                        datagetProducts.postValue(listPro)
                    }, 6000)


                }
            })

        return datagetProducts
    }


    fun getProductsCategory(): MutableLiveData<ArrayList<ProductsCategory>> {
        val data = MutableLiveData<ArrayList<ProductsCategory>>()
        var listProCat = ArrayList<ProductsCategory>()

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                .show()
        } else {

        }

        apiInterface.getProductCategories(5050)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ArrayList<ProductsCategory>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(log: ArrayList<ProductsCategory>) {
                    listProCat = log
                }
                override fun onError(e: Throwable) {
                    Toast.makeText(app, networkErrorHandler(e).errorTitle, Toast.LENGTH_LONG).show()
                }

                override fun onComplete() {
                    data.postValue(listProCat)
                }
            })

        return data
    }


    fun getRespToAssignedProducts(): MutableLiveData<ArrayList<Rep>> {
        val data = MutableLiveData<ArrayList<Rep>>()
        var listReps = ArrayList<Rep>()

        if (!app.isConnectedToNetwork()) {
            Toast.makeText(app, "No internet connection you will miss the latest information ", Toast.LENGTH_LONG)
                .show()
        } else {

        }

        apiInterface.getRepsByAdmin(userID)
            .subscribeOn(Schedulers.io())
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
                    data.postValue(listReps)

                }
            })

        return data
    }


    fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }


}