package project.emarge.fertilizer_manager.viewModels.dealers

import android.app.Application
import androidx.lifecycle.*
import project.emarge.fertilizer_manager.model.datamodel.Dealer
import project.emarge.fertilizer_manager.model.datamodel.Rep

class DealerAssignViewModel(application: Application) : AndroidViewModel(application) {

    private val _index = MutableLiveData<Int>()


    var dealerRepository: DealerRepo = DealerRepo(application)

    private var repsRespond: MutableLiveData<ArrayList<Rep>>? = null



    fun getAssignDealers(): MutableLiveData<ArrayList<Dealer>>{
        var dealerRespond: MutableLiveData<ArrayList<Dealer>> = dealerRepository.getAssignDealers()
        return dealerRespond!!
    }


    fun submitAssignDealers(rep: Rep, dealer :Dealer): MutableLiveData<Rep>{
        var assignDealersRespond: MutableLiveData<Rep> = dealerRepository.submitAssignDealer(rep,dealer)
        return assignDealersRespond!!
    }


    fun getRepsToAssignDealers(): MutableLiveData<ArrayList<Rep>>{
        repsRespond = MutableLiveData<ArrayList<Rep>>()
        repsRespond = dealerRepository.getRepsToAssignDealers()
        return repsRespond!!
    }







    val text: LiveData<String> = Transformations.map(_index) {
        "Hello  $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}