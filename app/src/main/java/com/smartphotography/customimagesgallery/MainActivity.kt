package com.smartphotography.customimagesgallery

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hasReadContactsPermission=
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if(hasReadContactsPermission== PackageManager.PERMISSION_GRANTED){
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1)
        }

        open_gallery.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA
                )== PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(this, Gallery::class.java))
            }
            else{
                showSnackBar(it, Manifest.permission.READ_EXTERNAL_STORAGE,"Please grant the required permissions.")
            }
        }
    }

    private fun showSnackBar(view: View, perm_type: String, message:String){
        Snackbar.make(view,message, Snackbar.LENGTH_LONG)
            .setAction("Enable access",{
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, perm_type)){
                    ActivityCompat.requestPermissions(this, arrayOf(
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                        1)
                }
                else{
                    val intent=Intent()
                    intent.action= Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri= Uri.fromParts("package",this.packageName,null)
                    intent.data=uri
                    this.startActivity(intent)
                }
            }).show()
    }

}