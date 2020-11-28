package com.smartphotography.customimagesgallery

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_gallery.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val TAG="Gallery"
class Gallery : AppCompatActivity() {
    private var pathToFile:String=""
    private var list: MutableList<ModelClass> = mutableListOf()
    private lateinit var rvAdapter:RVadapter
    private lateinit var galleryImageAdapter: GalleryImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        galleryImageAdapter=GalleryImageAdapter(applicationContext)
        rvAdapter=RVadapter(list,txt)

        gv.adapter=galleryImageAdapter

        val layoutManager= GridLayoutManager(this,4)
        rv.layoutManager= layoutManager
        gv.onItemClickListener=object :AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                addImageToRV(galleryImageAdapter.imagePaths[position])
            }
        }

        gallery_btn.setOnClickListener {
            if(rv.childCount==6){
                Toast.makeText(applicationContext,"6 images can be selected", Toast.LENGTH_SHORT).show()
            }
            else {
                pickImageFromGallery()
            }
        }
        camera_btn.setOnClickListener {
            if(rv.childCount==6){
                Toast.makeText(applicationContext,"6 images can be selected", Toast.LENGTH_SHORT).show()
            }
            else {
                startCamera()
            }
        }
        next.setOnClickListener {
            Log.d(TAG,"Selected images list is: ")
            for (image in rvAdapter.selectedImages){
                Log.d(TAG,"\n${image.getImageID()}")
            }
            Toast.makeText(this,"selectedImageList size = ${rvAdapter.selectedImages.size}",Toast.LENGTH_SHORT).show()
        }
    }

    companion object{
        private val GALLERY_IMAGE_PICK_CODE=1000
        private val CAMERA_IMAGE_PICK_CODE=1001
    }

    private fun startCamera() {
        val takepic= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(takepic.resolveActivity(packageManager)!=null){
            val photoFile: File?
            photoFile=createPhotoFile()
            if(photoFile!=null){
                pathToFile=photoFile.absolutePath
                val photoUri= FileProvider.getUriForFile(applicationContext,
                    "com.smartphotography.customimagesgallery.fileprovider",photoFile)
                takepic.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(takepic, CAMERA_IMAGE_PICK_CODE)
            }
        }
    }
    fun pickImageFromGallery() {
        val intent= Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type="image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivityForResult(intent, GALLERY_IMAGE_PICK_CODE)
    }
    private fun createPhotoFile(): File? {
        val name: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir:File=
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        var image:File?=null
        try {
            image=File.createTempFile(name,".jpg",storageDir)
        }catch (e: IOException){}
        return image
    }
    fun addImageToRV(imageID:String){
        if(rv.childCount>5) {
            Toast.makeText(applicationContext,"6 images can be selected.",Toast.LENGTH_SHORT).show()
        }
        else{
            list.add(ModelClass(imageID))
            Log.d(TAG,"ImageID IS=${imageID}")
            txt.text="${list.size}/6"
            rv.adapter = rvAdapter
            rvAdapter.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode== GALLERY_IMAGE_PICK_CODE && resultCode==Activity.RESULT_OK){
            val clipdata=data?.clipData
            if(clipdata!=null){
                if(rv.childCount+clipdata.itemCount>6){
                    Toast.makeText(applicationContext,"Only 6 images can be selected.",Toast.LENGTH_SHORT).show()
                }
                else {
                    loop@ for (i in 0..(clipdata.itemCount - 1)) {
                        val imageURI = clipdata.getItemAt(i).uri
                        list.add(ModelClass(imageURI.toString()))
                        txt.text="${list.size}/6"
                        rv.adapter = rvAdapter
                        rvAdapter.notifyDataSetChanged()
                    }
                }
            }
            else{
                val imageURI=data?.data
                addImageToRV(imageURI.toString())
            }
        }
        if (requestCode== CAMERA_IMAGE_PICK_CODE && resultCode==Activity.RESULT_OK){
            addImageToRV(pathToFile)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}