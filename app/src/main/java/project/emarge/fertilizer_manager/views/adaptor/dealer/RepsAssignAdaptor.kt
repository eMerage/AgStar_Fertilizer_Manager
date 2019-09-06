package project.emarge.fertilizer_manager.views.adaptor.dealer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listview_reps.view.*
import project.emarge.fertilizer_manager.R
import project.emarge.fertilizer_manager.model.datamodel.Rep

class RepsAssignAdaptor(val items: ArrayList<Rep>, val context: Context) : RecyclerView.Adapter<RepsAssignAdaptor.ViewHolderRepAssign>() {

    lateinit var mClickListener: ClickListener

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderRepAssign {
        return ViewHolderRepAssign(
            LayoutInflater.from(context).inflate(R.layout.listview_reps, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolderRepAssign, position: Int) {
        holder.bindView(position)
    }

    fun setOnItemClickListener(aClickListener: ClickListener) {
        mClickListener = aClickListener
    }



    interface ClickListener {
        fun onClick(rep: Rep, aView: View)
    }


    inner class ViewHolderRepAssign(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        val textviewRepname = view.textview_repname


        override fun onClick(p0: View?) {
            mClickListener.onClick( items[adapterPosition], p0!!)
        }


        init {
            view.setOnClickListener(this)
        }

        fun bindView(position: Int) {
            items[position].let {
                textviewRepname?.text = it.name

            }
        }


    }
}

