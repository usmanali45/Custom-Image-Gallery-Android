package com.smartphotography.customimagesgallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.lang.Exception

class RVadapter(list: MutableList<ModelClass>,txt: TextView)
    : RecyclerView.Adapter<RVadapter.ViewHolder>() {
    var selectedImages:MutableList<ModelClass> = mutableListOf()
    var txt:TextView
    init {
        this.selectedImages=list
        this.txt=txt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return selectedImages.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id=selectedImages.get(position).getImageID()
        holder.setData(id)
        holder.img.setOnClickListener {
            try {
                selectedImages.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position,selectedImages.size)
                txt.text="${selectedImages.size}/6"
            }
            catch (e: Exception){}
        }

    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val img: ImageView

        init {
            this.img=itemView.findViewById(R.id.img)
        }
        fun setData(imageID:String){
            Glide.with(itemView).load(imageID).into(img)
        }

    }

}