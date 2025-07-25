package com.example.tachyon.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tachyon.R
import com.example.tachyon.model.StoryModel
import com.example.tachyon.ui.activity.UpdateStoryActivity
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

//    fun updateData(stories: List<StoryModel>) {
//        data.clear()
//        data.addAll(stories)
//        notifyDataSetChanged()
//    }
fun updateData(newStories: List<StoryModel>) {
    val diffCallback = object : DiffUtil.Callback() {
        override fun getOldListSize(): Int = data.size
        override fun getNewListSize(): Int = newStories.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return data[oldItemPosition].storyId == newStories[newItemPosition].storyId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return data[oldItemPosition] == newStories[newItemPosition]
        }
    }

    val diffResult = DiffUtil.calculateDiff(diffCallback)

    data.clear()
    data.addAll(newStories)
    diffResult.dispatchUpdatesTo(this)
}

    fun getStoryId(position: Int) : String{
        return data[position].storyId
    }


}