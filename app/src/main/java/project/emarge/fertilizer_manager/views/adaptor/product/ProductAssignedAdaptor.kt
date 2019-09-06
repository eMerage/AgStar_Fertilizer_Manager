package project.emarge.fertilizer_manager.views.adaptor.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_assigned_products.view.*
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Products
import project.emarge.fertilizer_manager.model.datamodel.Rep

class ProductAssignedAdaptor(val items: ArrayList<Rep>, val context: Context) :
    RecyclerView.Adapter<ProductAssignedAdaptor.ViewHolderProductAssignedAdaptor>() {


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProductAssignedAdaptor {
        return ViewHolderProductAssignedAdaptor(
            LayoutInflater.from(context).inflate(
                R.layout.listview_assigned_products,
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolderProductAssignedAdaptor, position: Int) {

        var itemPostion = items[position]
        holder?.textviewRepName?.text = itemPostion.name
        if (itemPostion.productsList == null) {
            var emptylist = ArrayList<Products>()
            setAssignedProducts(context, holder, emptylist)
        } else {
            setAssignedProducts(context, holder, itemPostion.productsList!!)
        }

    }


    fun setAssignedProducts(context: Context, holder: ViewHolderProductAssignedAdaptor, itemsPro: ArrayList<Products>) {
        holder?.recyclerviewProducts?.adapter = ProductsAssignedProductsAdaptor(itemsPro, context)
    }


    inner class ViewHolderProductAssignedAdaptor(view: View) : RecyclerView.ViewHolder(view) {
        val textviewRepName = view.textview_rep_name
        val recyclerviewProducts = view.recyclerview_assing_products

    }
}

