package project.emarge.fertilizer_manager.views.adaptor.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_products.view.*
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Products

class ProductsAssignedProductsAdaptor(val items: ArrayList<Products>, val context: Context) :
    RecyclerView.Adapter<ProductsAssignedProductsAdaptor.ViewHolderProductsAssignedProductsAdaptor>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProductsAssignedProductsAdaptor {
        return ViewHolderProductsAssignedProductsAdaptor(
            LayoutInflater.from(context).inflate(
                R.layout.listview_products,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolderProductsAssignedProductsAdaptor, position: Int) {

        var itemProducts = items[position]
        holder?.textviewProductname?.text = itemProducts.productsName

    }




inner class ViewHolderProductsAssignedProductsAdaptor(view: View) : RecyclerView.ViewHolder(view) {
    val textviewProductname = view.textview_productname


}
}