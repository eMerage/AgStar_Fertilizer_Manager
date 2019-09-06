package project.emarge.fertilizer_manager.views.adaptor.product

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.listview_reps_to_products.view.*

import project.emarge.fertilizer_manager.R

import project.emarge.fertilizer_manager.model.datamodel.Rep


class ProductToAssignedAdaptor(val items: ArrayList<Rep>, val context: Context) :
    RecyclerView.Adapter<ProductToAssignedAdaptor.ViewHolderProductToAssignedAdaptor>() {

    lateinit var mClickListener: ClickListener


    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderProductToAssignedAdaptor {
        return ViewHolderProductToAssignedAdaptor(LayoutInflater.from(context).inflate(R.layout.listview_reps_to_products, parent, false))

    }

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(dealer: Rep, aView: View)
    }

    override fun onBindViewHolder(holder: ViewHolderProductToAssignedAdaptor, position: Int) {
        var itemPostion = items[position]
        holder.textviewRepName?.text = itemPostion.name

        if (itemPostion.isRepSelected) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#088946"))
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"))
        }




    }

    inner class ViewHolderProductToAssignedAdaptor(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener  {
        val textviewRepName = view.textview_name
        val cardView = view.card_view

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            mClickListener.onClick( items[adapterPosition], p0!!)

            for (d in items) {
                d.isRepSelected=false
            }
            items[adapterPosition].isRepSelected=true
            notifyDataSetChanged()
        }
    }
}

