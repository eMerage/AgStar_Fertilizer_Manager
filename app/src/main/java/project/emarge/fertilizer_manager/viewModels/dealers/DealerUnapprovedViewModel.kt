package project.emarge.fertilizer_manager.viewModels.dealers

import android.app.Application
import android.net.Uri
import android.widget.ProgressBar
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import project.emarge.fertilizer_manager.model.datamodel.Dealer

class DealerUnapprovedViewModel(application: Application) : AndroidViewModel(application) {


    var dealerRepository: DealerRepo = DealerRepo(application)





    fun getDealers(progressBar: ProgressBar): MutableLiveData<ArrayList<Dealer>>{
        return dealerRepository.getUnApprovedDealers(progressBar)
    }


    fun updateDealersLocation(location: LatLng, dealer: Dealer, selectedImagefilePath: Uri, phoneNumber: String,isCam : Boolean): MutableLiveData<Dealer>{
        return dealerRepository.updateDealerLocation(location,dealer,selectedImagefilePath,phoneNumber,isCam)
    }


}