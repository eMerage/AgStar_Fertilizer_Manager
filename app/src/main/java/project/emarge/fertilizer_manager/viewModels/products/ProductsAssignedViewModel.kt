package project.emarge.fertilizer_manager.viewModels.products

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import project.emarge.fertilizer_manager.model.datamodel.Rep

class ProductsAssignedViewModel(application: Application) : AndroidViewModel(application) {

    private val _index = MutableLiveData<Int>()

    var productsRepository: ProductsRepo = ProductsRepo(application)




    fun getRepPoducts(progressBar: ProgressBar, com : CompositeDisposable): MutableLiveData<ArrayList<Rep>>{
        return productsRepository.getAssignedProducts(progressBar,com)
    }





    val text: LiveData<String> = Transformations.map(_index) {
        "Hello  $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}