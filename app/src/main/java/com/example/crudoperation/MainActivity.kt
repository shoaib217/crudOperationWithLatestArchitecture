package com.example.crudoperation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.renderscript.Element
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.crudoperation.databinding.ActivityMainBinding
import com.example.crudoperation.viewmodel.MainViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var model:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this)[MainViewModel::class.java]
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewModel = model
        binding.lifecycleOwner = this
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHost.navController
        val appBarConfig = AppBarConfiguration(navController.graph)
        binding.toolbar
        binding.toolbar.setupWithNavController(navController,appBarConfig)
        model.arrayList = ArrayList()

        model.showToast.observe(this){
            if(it.isNotEmpty()){
                Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun isImageBlurry(bitmap: Bitmap): Boolean {
        // Calculate the average color difference between adjacent pixels
        var sumColorDiff = 0L
        var numPixels = 0
        for (x in 1 until bitmap.width) {
            for (y in 1 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                val prevPixel = bitmap.getPixel(x-1, y-1)
                val colorDiff = colorDifference(pixel, prevPixel)
                sumColorDiff += colorDiff
                numPixels++
            }
        }
        val avgColorDiff = sumColorDiff / numPixels

        Log.d("crud","avgColorDiff $avgColorDiff")
        // If the average color difference is below a threshold, then the image is considered blurry
        val threshold = 1000
        return avgColorDiff < threshold
    }

    fun colorDifference(color1: Int, color2: Int): Int {
        val redDiff = Color.red(color1) - Color.red(color2)
        val greenDiff = Color.green(color1) - Color.green(color2)
        val blueDiff = Color.blue(color1) - Color.blue(color2)
        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            if (!uri.path.isNullOrEmpty()){
                Log.d("Curd","image path -- ${uri.path}")
                val imageFile = File(uri.path.toString())
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                if (isImageBlurry(bitmap)){
                    Toast.makeText(this@MainActivity,"Capture Image Is Blur",Toast.LENGTH_SHORT).show()
                }else{
                    model.pImage.postValue(imageFile.absolutePath)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun showDailog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Crud Operation")
        builder.setMessage("Are You Sure You Want To Exit?")

        builder.setPositiveButton("Yes"){dailog,which ->
            dailog.dismiss()
            finishAffinity()
        }
        builder.setNegativeButton("No"){ dailog ,_->
            dailog.dismiss()
        }
        builder.show()
    }

}