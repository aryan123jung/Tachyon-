package com.example.kokoro.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kokoro.R
import com.example.kokoro.model.StoryModel
import com.example.kokoro.ui.activity.UpdateStoryActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class StoryAdapter ( val context: Context,
    var data: ArrayList<StoryModel>): RecyclerView.Adapter<StoryAdapter.StoryViewHolder>(){

        class StoryViewHolder(itemView: View)
            : RecyclerView.ViewHolder(itemView){
                val imageView: ImageView = itemView.findViewById(R.id.getImage)
                val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar2)
                val editButton : TextView = itemView.findViewById(R.id.lblEdit)
                val sTitle: TextView = itemView.findViewById(R.id.displayTitle)
                val sDesc: TextView = itemView.findViewById(R.id.displayDesc)
            }


        override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):StoryViewHolder {
            val itemView : View = LayoutInflater.from(context).
            inflate(R.layout.sample_story,parent,false)

            return StoryViewHolder(itemView)
    }

    override fun getItemCount(): Int{
        return data.size
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.sTitle.text = data[position].storyTitle
        holder.sDesc.text = data[position].storyDesc
        holder.sDesc.maxLines = 5 // Initially collapsed
        holder.sDesc.ellipsize = android.text.TextUtils.TruncateAt.END

        var isExpanded = false
        holder.sDesc.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                holder.sDesc.maxLines = Integer.MAX_VALUE
                holder.sDesc.ellipsize = null
            } else {
                holder.sDesc.maxLines = 5
                holder.sDesc.ellipsize = android.text.TextUtils.TruncateAt.END
            }
        }

        Picasso.get().load(data[position].imageUrl).into(holder.imageView, object : Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                // Handle error
            }
        })

        holder.editButton.setOnClickListener {
            val intent = Intent(context, UpdateStoryActivity::class.java)
            intent.putExtra("storyId", data[position].storyId)
            context.startActivity(intent)
        }
    }

    fun updateData(stories: List<StoryModel>) {
        data.clear()
        data.addAll(stories)
        notifyDataSetChanged()
    }

    fun getStoryId(position: Int) : String{
        return data[position].storyId
    }


}