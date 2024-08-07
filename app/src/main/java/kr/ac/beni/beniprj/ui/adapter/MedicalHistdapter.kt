package kr.ac.beni.beniprj.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.model.MedicalHistInfo

class MedicalHistdapter(listener: OnItemClickListener) :
    RecyclerView.Adapter<MedicalHistdapter.ViewHolder>() {

    private var onItemClickListener:OnItemClickListener

    init {
        this.onItemClickListener = listener
    }

    var lists = ArrayList<MedicalHistInfo>()

    interface OnItemClickListener{
        fun onClick(data: MedicalHistInfo)
    }

    fun addItem(item: MedicalHistInfo) {
        lists.add(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var medicalTitle: TextView
        var medicalDate: TextView
        init {
            medicalTitle = itemView.findViewById(R.id.medical_hist_title)
            medicalDate = itemView.findViewById(R.id.medical_hist_date)

            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                val medicalHistInfo = lists[position]
                onItemClickListener.onClick(medicalHistInfo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val con = parent.context
        val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.item_medical_hist, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: MedicalHistInfo = lists[position]
        holder.medicalTitle.text = "${data.medicalMainName} ${data.medicalSubName}"
        holder.medicalDate.text = data.medicalDate
//        if (data.type == "user") {
//            holder.userContents.text = data.contents
//            holder.systemLayout.visibility = View.GONE
//            holder.userLayout.visibility = View.VISIBLE
//        } else {
//            holder.systemContents.text = data.contents
//            holder.userLayout.visibility = View.GONE
//            holder.systemLayout.visibility = View.VISIBLE
//        }
//
//        if (data.voicefYn == 1) {
//            holder.systemContents.setTextColor(Color.RED)
//        } else {
//            holder.systemContents.setTextColor(Color.BLACK)
//        }

    }

    override fun getItemCount(): Int {
        return lists.size
    }
}