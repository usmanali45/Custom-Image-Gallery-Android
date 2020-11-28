package com.smartphotography.customimagesgallery

class ModelClass(imageID:String) {
    private val imageID:String
    init {
        this.imageID=imageID
    }

    fun getImageID():String{
        return imageID
    }

}