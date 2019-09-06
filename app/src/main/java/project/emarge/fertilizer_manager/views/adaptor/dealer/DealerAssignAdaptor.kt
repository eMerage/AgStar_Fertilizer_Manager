package project.emarge.fertilizer_manager.views.adaptor.dealer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_assign_dealers.view.*
import kotlinx.android.synthetic.main.listview_dealers.view.textview_code
import kotlinx.android.synthetic.main.listview_dealers.view.textview_name
import kotlinx.android.synthetic.main.listview_dealers.view.textview_number
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Dealer

class DealerAssignAdaptor(val items: ArrayList<Dealer>, val context: Context) :
    RecyclerView.Adapter<DealerAssignAdaptor.ViewHolderDealerApproved>() {

    lateinit var mClickListener: ClickListener

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDealerApproved {
        return ViewHolderDealerApproved(
            LayoutInflater.from(context).inflate(R.layout.listview_assign_dealers, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderDealerApproved, position: Int) {
        holder.bindView(position)
    }

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }



    interface ClickListener {
        fun onClick(dealer: Dealer, aView: View)
    }


    inner class ViewHolderDealerApproved(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        val textviewCode = view.textview_code
        val textviewNumber = view.textview_number
        val textviewName = view.textview_name
        val textviewAssignRep = view.textview_assign_rep
        val textviewBtnAssign = view.textview_btn_assign



        override fun onClick(p0: View?) {
            mClickListener.onClick( items[adapterPosition], p0!!)
        }


        init {
            textviewBtnAssign.setOnClickListener(this)
        }

        fun bindView(position: Int) {
            items[position].let {
                textviewCode?.text = it.dealerCode
                textviewNumber?.text = it.dealerContactNumber.toString()
                textviewName?.text = it.dealerName
                textviewAssignRep?.text = it.dealersRep.name

            }
        }


    }
}

