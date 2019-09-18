package project.emarge.fertilizer_manager.viewModels.visits

import android.app.Application

import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import project.emarge.fertilizer_manager.model.datamodel.Visits


open class VisitsViewModel(application: Application) : AndroidViewModel(application) {



    var visitsRepository: VisitsRepo = VisitsRepo(application)

    private var visitsRespond: MutableLiveData<ArrayList<Visits>>? = null


     val isLoading = ObservableField<Boolean>()





    fun getVisitsFromServer(): MutableLiveData<ArrayList<Visits>>{
        return visitsRepository.geVisits(isLoading)
    }


    fun getVisitsByFilter(sDate : String , eDate : String,list : ArrayList<Visits>): MutableLiveData<ArrayList<Visits>>{
        return visitsRepository.visitsFilter(sDate,eDate,list)

    }


    fun uploadeMissingImages():MutableLiveData<Int>{
        return visitsRepository.getMissingImagesFromServer()
    }

}