package com.example.crudoperation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.crudoperation.databinding.ActivityMainBinding
import com.example.crudoperation.viewmodel.MainViewModel

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