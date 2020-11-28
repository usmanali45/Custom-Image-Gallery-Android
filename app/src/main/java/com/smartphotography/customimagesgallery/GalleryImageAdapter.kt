package com.smartphotography.customimagesgallery

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class GalleryImageAdapter(localContext: Context): BaseAdapter() {
    var imagePaths:MutableList<String> = mutableListOf()
    private var context: Context
    private var screenWidth:Int=0
    init {
        context=localContext
        imagePaths=getAllShownImagesPath(context)
        screenWidth=context.resources.displayMetrics.widthPixels
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val picturesView: ImageView

        if (convertView == null) {
            picturesView = ImageView(context)
            picturesView.scaleType = ImageView.ScaleType.FIT_CENTER
            picturesView.layoutParams= AbsListView.LayoutParams(screenWidth/4-5,screenWidth/4-5)
        } else {
            picturesView = convertView as ImageView
        }
        val ro= RequestOptions().override(screenWidth/4-5)
        Glide.with(context).load(imagePaths[position]).apply(ro)
            .placeholder(R.drawable.placeholder).centerCrop()
            .into(picturesView)

        return picturesView
    }

    override fun getItem(position: Int): Any {
        return  position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imagePaths.size
    }

    fun getAllShownImagesPath(activity: Context):MutableList<String>{
        val uri: Uri
        val cursor: Cursor?
        val column_index_data:Int
        val column_index_folder_name:Int
        val listOfAllImages:MutableList<String> = mutableListOf()
        var absolutePath:String
        val orderBy= MediaStore.Images.Media.DATE_TAKEN

        uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection= arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media._ID)
        cursor=activity.contentResolver.query(uri,projection,null,null,orderBy+" DESC")
        column_index_data=cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        while (cursor.moveToNext()){
            absolutePath=cursor.getString(column_index_data)
            listOfAllImages.add(absolutePath)
        }
        cursor.close()

        return listOfAllImages
    }

}