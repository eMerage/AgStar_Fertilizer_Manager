package project.emarge.fertilizer_manager.views.fragment.products

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_products_to_assign.*
import kotlinx.android.synthetic.main.fragment_products_to_assign.view.*
import kotlinx.android.synthetic.main.fragment_products_to_assign.view.card_view_added
import project.emarge.fertilizer_manager.R

import project.emarge.fertilizer_manager.model.datamodel.Products
import project.emarge.fertilizer_manager.model.datamodel.ProductsCategory
import project.emarge.fertilizer_manager.model.datamodel.Rep
import project.emarge.fertilizer_manager.viewModels.products.ProductsToAssignViewModel
import project.emarge.fertilizer_manager.views.adaptor.product.*

import kotlin.collections.ArrayList


/**
 * A placeholder fragment containing a simple view.
 */
class ProductsToAssignFragment : Fragment() {

    lateinit var productToAssignedAdaptor: ProductToAssignedAdaptor
    lateinit var autoCompleteRepsToProductsAdapter: AutoCompleteRepsToProductsAdapter
    lateinit var productsAdaptor: ProductsAdaptor
    lateinit var autoCompleteProductsAdapter: AutoCompleteProductsAdapter

    lateinit var addedProductsAdaptor: AddedProductsAdaptor

    var selectedProCategoryID: Int = 0
    var selectedrep: Rep = Rep()
    var addedProducts = ArrayList<Products>()
    lateinit var root: View

    private lateinit var pageViewModel: ProductsToAssignViewModel


    var repList = ArrayList<Rep>()
    var proCategoryList = ArrayList<ProductsCategory>()
    var productList = ArrayList<Products>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = activity?.run { ViewModelProviders.of(this)[ProductsToAssignViewModel::class.java] }
            ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_products_to_assign, container, false)
        return root
    }


    override fun onStart() {
        super.onStart()

      //  getReps()
      //  getProductCategory()
      //  getProducts(selectedProCategoryID)




    /*    root.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                var selectedProductsCategory: ProductsCategory = parent.getItemAtPosition(position) as ProductsCategory
                selectedProCategoryID = selectedProductsCategory.productsID!!
                getProducts(selectedProCategoryID)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        root.autoCompleteTextView_products.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                productsAdaptor.notifyDataSetChanged()
            }

        root.autoCompleteTextView_products_reps.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                productToAssignedAdaptor.notifyDataSetChanged()

            }


        root.swiperefresh_producttoassigned.setOnRefreshListener {
            if (repList.isEmpty()) {
                getReps()
            }
            if (proCategoryList.isEmpty()) {
                getProductCategory()
            }
            if (productList.isEmpty()) {
                getProducts(selectedProCategoryID)
            }
        }


        root.button.setOnClickListener {

            pageViewModel.submitAssignProducts(selectedrep, addedProducts, root.progressbar_product_to_assigned)
                .observe(this, Observer<Boolean> {
                    it?.let { result ->
                        if (result) {
                            Toast.makeText(context as Activity, "Product Assign successful !!", Toast.LENGTH_LONG)
                                .show()
                            root.card_view_product_main.visibility = View.GONE
                            addedProducts.clear()
                            root.card_view_added.visibility = View.GONE

                        } else {
                            Toast.makeText(
                                context as Activity,
                                "Product Assign not successful,please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })

        }*/


    }


    private fun getReps() {

        pageViewModel.getRepsToAssign().observe(this, Observer<ArrayList<Rep>> {
            it?.let { result ->
                repList = result

                root.ProgressBar_recyclerView_products_to_assign.visibility = View.GONE

                if (result.isEmpty()) {
                    root.relativeLayout_recyclerView_products_to_assign.visibility = View.VISIBLE
                    root.recyclerView_products_to_assign.visibility = View.INVISIBLE
                    root.textview_no_approve_dealers_toassign.visibility = View.VISIBLE
                    root.textview_no_approve_dealers_toassign.text = "No Approved dealers"

                } else {
                    root.relativeLayout_recyclerView_products_to_assign.visibility = View.GONE
                    root.recyclerView_products_to_assign.visibility = View.VISIBLE
                    root.textview_no_approve_dealers_toassign.visibility = View.GONE
                }



                productToAssignedAdaptor = ProductToAssignedAdaptor(result, context as Activity)
                root.recyclerView_products_to_assign.adapter = productToAssignedAdaptor
                autoCompleteRepsToProductsAdapter = AutoCompleteRepsToProductsAdapter(
                    context as Activity,
                    R.layout.fragment_dealer_assign,
                    R.id.lbl_name,
                    result
                )
                root.autoCompleteTextView_products_reps.setAdapter(autoCompleteRepsToProductsAdapter)

                root.swiperefresh_producttoassigned.isRefreshing = false



                productToAssignedAdaptor.setOnItemClickListener(object : ProductToAssignedAdaptor.ClickListener {
                    override fun onClick(rep: Rep, aView: View) {
                        selectedrep = rep
                        root.card_view_product_main.visibility = View.VISIBLE
                    }
                })

            }
        })


    }


    private fun addedProduct(products: Products) {
        pageViewModel.addProducts(products, addedProducts).observe(this, Observer<ArrayList<Products>> {
            it?.let { result ->

                if (!root.card_view_added.isVisible) {
                    root.card_view_added.visibility = View.VISIBLE
                }

                addedProducts = result
                addedProductsAdaptor = AddedProductsAdaptor(addedProducts, context as Activity)
                recyclerView_added_products.adapter = addedProductsAdaptor
                addedProductsAdaptor.setOnItemClickListener(object : AddedProductsAdaptor.ClickListener {
                    override fun onClick(products: Products, aView: View) {
                        val alertDialogBuilder = AlertDialog.Builder(context)
                        alertDialogBuilder.setTitle("Warning!")
                        alertDialogBuilder.setMessage("Do you really want to delete this stock ?")
                        alertDialogBuilder.setPositiveButton(
                            "YES"
                        ) { _, _ ->
                            addedProducts.remove(products)
                            addedProductsAdaptor.notifyDataSetChanged()
                            if (addedProducts.isEmpty()) {
                                root.card_view_added.visibility = View.GONE
                            }
                        }
                        alertDialogBuilder.setNegativeButton(
                            "NO",
                            DialogInterface.OnClickListener { _, _ -> return@OnClickListener })
                        alertDialogBuilder.show()


                    }
                })


            }
        })
    }


    private fun getProductCategory() {
        pageViewModel.getProductCategory().observe(this, Observer<ArrayList<ProductsCategory>> {
            it?.let { result ->
                proCategoryList = result

                var listProCat = ArrayList<ProductsCategory>()
                listProCat.add(ProductsCategory(0, "All"))
                listProCat.addAll(result)
                val adapter = ProductCatSpinnerAdapter(
                    context as Activity,
                    R.layout.item_spinner, listProCat
                )
                root.spinner.adapter = adapter

                root.swiperefresh_producttoassigned.isRefreshing = false
            }
        })

    }

    private fun getProducts(proCat: Int) {

        pageViewModel.getProducts(proCat).observe(this, Observer<ArrayList<Products>> {
            it?.let { result ->

                productList = result

                root.ProgressBar_recyclerView_products.visibility = View.GONE
                if (result.isEmpty()) {
                    root.relativeLayout_recyclerView_products.visibility = View.VISIBLE
                    root.recyclerView_products.visibility = View.INVISIBLE
                    root.textview_no_products_toassign.visibility = View.VISIBLE
                    root.textview_no_products_toassign.text = "No Approved dealers"

                } else {
                    root.relativeLayout_recyclerView_products.visibility = View.GONE
                    root.recyclerView_products.visibility = View.VISIBLE
                    root.textview_no_products_toassign.visibility = View.GONE
                }

                productsAdaptor = ProductsAdaptor(result, context as Activity)
                root.recyclerView_products.adapter = productsAdaptor

                autoCompleteProductsAdapter = AutoCompleteProductsAdapter(
                    context as Activity,
                    R.layout.fragment_dealer_assign,
                    R.id.lbl_name,
                    result
                )
                root.autoCompleteTextView_products.setAdapter(autoCompleteProductsAdapter)

                root.swiperefresh_producttoassigned.isRefreshing = false

                productsAdaptor.setOnItemClickListener(object : ProductsAdaptor.ClickListener {
                    override fun onClick(products: Products, aView: View) {

                        addedProduct(products)
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
        fun newInstance(sectionNumber: Int): ProductsToAssignFragment {
            return ProductsToAssignFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}