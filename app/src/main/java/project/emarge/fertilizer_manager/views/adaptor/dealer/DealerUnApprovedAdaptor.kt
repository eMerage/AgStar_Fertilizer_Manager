package project.emarge.fertilizer_manager.views.adaptor.dealer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_dealers.view.*
import kotlinx.android.synthetic.main.listview_dealers.view.textview_code
import kotlinx.android.synthetic.main.listview_dealers.view.textview_name
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Dealer

class DealerUnApprovedAdaptor(val items: ArrayList<Dealer>, val context: Context) :
    RecyclerView.Adapter<DealerUnApprovedAdaptor.ViewHolderDealerUNApproved>() {



    lateinit var mClickListener: ClickListener

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderDealerUNApproved {
        return ViewHolderDealerUNApproved(
            LayoutInflater.from(context).inflate(
                R.layout.listview_dealers,
                parent,
                false
            )
        )

    }

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }
    interface ClickListener {
        fun onClick(dealer: Dealer, aView: View)
    }


    override fun onBindViewHolder(holder: ViewHolderDealerUNApproved, position: Int) {
        var itemPostion = items[position]
        holder.textviewCode?.text = itemPostion.dealerCode
        holder.textviewNumber?.text = itemPostion.dealerContactNumber.toString()
        holder.textviewName?.text = itemPostion.dealerName

        if(itemPostion.dealerLocationLan==0.0){
            holder.ImageViewLocationImage?.setImageResource(R.drawable.ic_round_red)
        }else{
            holder.ImageViewLocationImage?.setImageResource(R.drawable.ic_round_dark_green)
        }


        if((itemPostion.dealerimageCode=="") &&  (itemPostion.dealerImg=="")){
            holder.ImageViewImageImage?.setImageResource(R.drawable.ic_round_red)
        }else if((itemPostion.dealerimageCode!="") &&  (itemPostion.dealerImg!="")){
            holder.ImageViewImageImage?.setImageResource(R.drawable.ic_round_dark_green)
        }else{
            holder.ImageViewImageImage?.setImageResource(R.drawable.ic_round_light_yellow)
        }




    }


    inner class ViewHolderDealerUNApproved(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener  {
        val textviewCode = view.textview_code
        val textviewNumber = view.textview_number
        val textviewName = view.textview_name


        val ImageViewLocationImage = view.ImageView_location_image
        val ImageViewImageImage = view.ImageView_dealer_image




        init {
            view.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            mClickListener.onClick( items[adapterPosition], p0!!)
        }


    }

}

