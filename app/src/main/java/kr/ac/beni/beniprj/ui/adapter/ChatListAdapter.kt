package kr.ac.beni.beniprj.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.model.Chat

class ChatListAdapter(listener: OnItemClickListener) :
    RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    private var onItemClickListener:OnItemClickListener

    init {
        this.onItemClickListener = listener
    }

    var lists = ArrayList<Chat>()

    interface OnItemClickListener{
        fun onClick(data: Chat)
    }

    fun addItem(item: Chat) {
        lists.add(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userLayout: LinearLayout
        var userContents: TextView
        var systemLayout: LinearLayout
        var systemContents: TextView


        init {
            userLayout = itemView.findViewById(R.id.user_layout)
            userContents = itemView.findViewById(R.id.user_contents)
            systemLayout = itemView.findViewById(R.id.system_layout)
            systemContents = itemView.findViewById(R.id.system_contents)

            itemView.setOnClickListener {
                val position = absoluteAdapterPosition
                val chat = lists[position]
                onItemClickListener.onClick(chat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val con = parent.context
        val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.item_chat, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Chat = lists[position]

        if (data.type == "user") {
            holder.userContents.text = data.contents
            holder.systemLayout.visibility = View.GONE
            holder.userLayout.visibility = View.VISIBLE
        } else {
            holder.systemContents.text = data.contents
            holder.userLayout.visibility = View.GONE
            holder.systemLayout.visibility = View.VISIBLE
        }

        if (data.voicefYn == 1) {
            holder.systemContents.setTextColor(Color.RED)
        } else {
            holder.systemContents.setTextColor(Color.BLACK)
        }
//        if (data.answer.isNullOrEmpty()) {
//            holder.checkedImage.visibility = View.GONE
//            holder.closeImage.visibility = View.VISIBLE
//        } else {
//            holder.closeImage.visibility = View.GONE
//            holder.checkedImage.visibility = View.VISIBLE
//        }
    }

    override fun getItemCount(): Int {
        return lists.size
    }
}