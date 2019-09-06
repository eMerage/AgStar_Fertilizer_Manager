package project.emarge.fertilizer_manager.viewModels.dealers

import android.app.Application
import android.widget.ProgressBar
import androidx.lifecycle.*
import project.emarge.fertilizer_manager.model.datamodel.Dealer

class DealerApprovedViewModel(application: Application) : AndroidViewModel(application) {

    private val _index = MutableLiveData<Int>()


    var dealerRepository: DealerRepo = DealerRepo(application)



    fun getApprovedDealers(pro : ProgressBar): MutableLiveData<ArrayList<Dealer>>{
        return dealerRepository.getApprovedDealers(pro)
    }


    val text: LiveData<String> = Transformations.map(_index) {
        "Hello  $it"
    }

}