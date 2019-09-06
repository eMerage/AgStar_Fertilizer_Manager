package project.emarge.fertilizer_manager.viewModels.products

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.*
import project.emarge.fertilizer_manager.model.datamodel.Products
import project.emarge.fertilizer_manager.model.datamodel.ProductsCategory
import project.emarge.fertilizer_manager.model.datamodel.Rep

class ProductsToAssignViewModel(application: Application) : AndroidViewModel(application) {

    var productsRepository: ProductsRepo = ProductsRepo(application)





    fun getRepsToAssign(): MutableLiveData<ArrayList<Rep>>{
        return  productsRepository.getRespToAssignedProducts()
    }


    fun getProducts(category: Int): MutableLiveData<ArrayList<Products>>{
        return  productsRepository.getProducts(category)
    }


    fun addProducts(product: Products,addedProduct: ArrayList<Products>): MutableLiveData<ArrayList<Products>>{
        return productsRepository.addProducts(product,addedProduct)
    }

    fun submitAssignProducts(rep: Rep,addedProduct: ArrayList<Products>,progressBar: ProgressBar): MutableLiveData<Boolean>{
        return productsRepository.submitAssignProducts(rep,addedProduct,progressBar)
    }


    fun getProductCategory(): MutableLiveData<ArrayList<ProductsCategory>>{
        return productsRepository.getProductsCategory()
    }



}