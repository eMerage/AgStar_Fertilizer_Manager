package project.emarge.fertilizer_manager.views.fragment.products

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_products_assigned.view.*
import kotlinx.android.synthetic.main.fragment_products_assigned.view.recyclerView_products_assigned
import project.emarge.fertilizer_manager.R

import project.emarge.fertilizer_manager.model.datamodel.Rep
import project.emarge.fertilizer_manager.viewModels.products.ProductsAssignedViewModel
import project.emarge.fertilizer_manager.views.adaptor.product.ProductAssignedAdaptor


/**
 * A placeholder fragment containing a simple view.
 */
class ProductsAssignedFragment : Fragment() {


    lateinit var productAssignedAdaptor: ProductAssignedAdaptor



    val mDisposables = CompositeDisposable()
    lateinit var pageViewModel: ProductsAssignedViewModel
    lateinit var root: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = activity?.run { ViewModelProviders.of(this)[ProductsAssignedViewModel::class.java] } ?: throw Exception("Invalid Activity")

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root=inflater.inflate(R.layout.fragment_products_assigned, container, false)
        return root

    }

    override fun onStart() {
        super.onStart()
        getAssigendProducts()
        root.swiperefresh_productsassign_items.setOnRefreshListener {
            getAssigendProducts()
        }

    }

    private fun getAssigendProducts() {
        pageViewModel.getRepPoducts(root.progress_products_assign,mDisposables).observe(this, Observer<ArrayList<Rep>> {
            it?.let { result ->
                if (result.isEmpty()) {
                    root.textview_assigned_products.visibility = View.VISIBLE
                    root.recyclerView_products_assigned.visibility = View.GONE
                    root.textview_assigned_products.text = "No products assigned to dealers"
                } else {
                    root.textview_assigned_products.visibility = View.GONE
                    root.recyclerView_products_assigned.visibility = View.VISIBLE
                }
                root.swiperefresh_productsassign_items.isRefreshing =  false
                productAssignedAdaptor = ProductAssignedAdaptor(result, context as Activity)
                root.recyclerView_products_assigned.adapter = productAssignedAdaptor
                root.swiperefresh_productsassign_items.isRefreshing = false

            }
        })

    }


    override fun onPause() {
        super.onPause()

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
        fun newInstance(sectionNumber: Int): ProductsAssignedFragment {
            return ProductsAssignedFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}