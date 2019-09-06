package project.emarge.fertilizer_manager.views.fragment.dealer

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_dealer_assign.*
import project.emarge.fertilizer_manager.R

import project.emarge.fertilizer_manager.model.datamodel.Dealer
import project.emarge.fertilizer_manager.model.datamodel.Rep
import project.emarge.fertilizer_manager.viewModels.dealers.DealerAssignViewModel
import project.emarge.fertilizer_manager.views.adaptor.dealer.AutoCompleteRepsAdapter
import project.emarge.fertilizer_manager.views.adaptor.dealer.DealerAssignAdaptor
import project.emarge.fertilizer_manager.views.adaptor.dealer.RepsAssignAdaptor


/**
 * A placeholder fragment containing a simple view.
 */
class DealerAssignFragment : Fragment() {



    lateinit var dealerAssignAdaptor: DealerAssignAdaptor
    lateinit var repsAssignAdaptor: RepsAssignAdaptor

    lateinit var autoCompleteRepsAdapter: AutoCompleteRepsAdapter


    lateinit var dialogDealerAssign: Dialog



    var selectedDealer = Dealer()

    lateinit var root : View


    private lateinit var pageViewModel: DealerAssignViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pageViewModel = activity?.run { ViewModelProviders.of(this)[DealerAssignViewModel::class.java] } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root =inflater.inflate(R.layout.fragment_dealer_assign, container, false)
        return root
    }


    private fun getAssignDealers(){
        pageViewModel.getAssignDealers().observe(this, Observer<ArrayList<Dealer>> {
            it?.let { result ->
                dealerAssignAdaptor = DealerAssignAdaptor(result, context as Activity)
                recyclerView_assign_dealers.adapter= dealerAssignAdaptor

                dealerAssignAdaptor.setOnItemClickListener(object : DealerAssignAdaptor.ClickListener {
                    override fun onClick(dealer: Dealer, aView: View) {
                        openDialogDealerAssign(dealer)
                    }

                })
            }
        })




    }


    private fun openDialogDealerAssign(dealer: Dealer) {
        dialogDealerAssign = Dialog(context as Activity)
        dialogDealerAssign.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogDealerAssign.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialogDealerAssign.setContentView(R.layout.dialog_assign_reps_to_dealer)
        dialogDealerAssign.setCancelable(true)



        var autoCompleteTextViewReps = dialogDealerAssign.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView_dialog_assign_reps_to_dealers)

        var recyclerViewAssignReps = dialogDealerAssign.findViewById<RecyclerView>(R.id.recyclerView_assign_reps)



        pageViewModel!!.getRepsToAssignDealers().observe(this, Observer<ArrayList<Rep>> {
            it?.let { result ->
                repsAssignAdaptor =
                    RepsAssignAdaptor(result, context as Activity)
                recyclerViewAssignReps.adapter= repsAssignAdaptor


                autoCompleteRepsAdapter = AutoCompleteRepsAdapter(
                    context as Activity,
                    R.layout.fragment_dealer_assign,
                    R.id.lbl_name,
                    result
                )


                autoCompleteTextViewReps.setAdapter(autoCompleteRepsAdapter)


                repsAssignAdaptor.setOnItemClickListener(object : RepsAssignAdaptor.ClickListener {
                    override fun onClick(rep: Rep, aView: View) {
                        submitAssignDealer(rep,dealer)

                    }

                })
            }
        })



        autoCompleteTextViewReps.onItemClickListener = AdapterView.OnItemClickListener{ parent, _, position, _ ->
            var selectedRep : Rep = parent.getItemAtPosition(position) as Rep


            // binding.menu!!.setSelectedMenuTitleID(menuTitles.menuTitleID)
        }



        dialogDealerAssign.show()

    }


    private fun submitAssignDealer(rep: Rep, dealer :Dealer){
        pageViewModel.submitAssignDealers(rep,dealer).observe(this, Observer<Rep> {
            it?.let { result ->
                if(result.userStatus){
                    Toast.makeText(context as Activity, "Dealer Assign successful", Toast.LENGTH_LONG).show()
                    dialogDealerAssign.dismiss()
                    getAssignDealers()

                }else{
                    Toast.makeText(context as Activity, "Dealer Assign not successful,please try again", Toast.LENGTH_LONG).show()
                }


            }
        })

    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): DealerAssignFragment {
            return DealerAssignFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}