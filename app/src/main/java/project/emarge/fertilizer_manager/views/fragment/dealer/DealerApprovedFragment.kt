package project.emarge.fertilizer_manager.views.fragment.dealer

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_dealer_approved.*
import kotlinx.android.synthetic.main.fragment_dealer_approved.view.*

import project.emarge.fertilizer_manager.R

import project.emarge.fertilizer_manager.model.datamodel.Dealer
import project.emarge.fertilizer_manager.viewModels.dealers.DealerApprovedViewModel
import project.emarge.fertilizer_manager.views.adaptor.dealer.DealerApprovedAdaptor


/**
 * A placeholder fragment containing a simple view.
 */
class DealerApprovedFragment : Fragment() {

    lateinit var dealerApprovedAdaptor: DealerApprovedAdaptor
    private lateinit var pageViewModel: DealerApprovedViewModel
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = activity?.run { ViewModelProviders.of(this)[DealerApprovedViewModel::class.java] }
            ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_dealer_approved, container, false)
        return root
    }


    override fun onStart() {
        super.onStart()
        getAssignDealers()
        root.swiperefresh_dealer_approved.setOnRefreshListener {
            getAssignDealers()
        }

    }


    private fun getAssignDealers() {
        pageViewModel.getApprovedDealers(root.progressBar_dealer_approved).observe(this, Observer<ArrayList<Dealer>> {
            it?.let { result ->
                if (result.isEmpty()) {
                    textview_approved_dealers.visibility = View.VISIBLE
                    textview_approved_dealers.text = "No Approved dealers"
                    recyclerView_approved_dealers.visibility = View.GONE
                } else {
                    textview_approved_dealers.visibility = View.GONE
                    recyclerView_approved_dealers.visibility = View.VISIBLE
                }

                root.swiperefresh_dealer_approved.isRefreshing = false
                dealerApprovedAdaptor = DealerApprovedAdaptor(result, context as Activity)
                recyclerView_approved_dealers.adapter = dealerApprovedAdaptor
                dealerApprovedAdaptor.setOnItemClickListener(object : DealerApprovedAdaptor.ClickListener {
                    override fun onClick(dealer: Dealer, aView: View) {

                    }
                })
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
        fun newInstance(sectionNumber: Int): DealerApprovedFragment {
            return DealerApprovedFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}