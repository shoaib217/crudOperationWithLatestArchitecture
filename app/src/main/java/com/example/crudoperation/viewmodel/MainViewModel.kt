package com.example.crudoperation.viewmodel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.crudoperation.R
import com.example.crudoperation.fragments.HomeFragmentDirections

class MainViewModel: ViewModel() {
    lateinit var arrayList:ArrayList<PersonModel>
    var personArrayList = MutableLiveData<ArrayList<PersonModel>>()
    val personData = MutableLiveData<PersonModel>()
    var editData:Boolean = false
    var position = 0
    var showDeleteDialog = MutableLiveData<Boolean>()
    var addViewButton = MutableLiveData<Boolean>()

    init {
        addViewButton.value = false
        showDeleteDialog.value =false
        personArrayList = MutableLiveData(ArrayList())
        personData.value = PersonModel()
    }

    fun personSave(view: View){
        val data = personData.value
        Log.d("crud", "personSave:${data}")
        if (editData){
            data?.let { arrayList.set(position, it) }
        }else{
            arrayList.add(data!!)
        }
        personArrayList.postValue(arrayList)
        view.findNavController().popBackStack()
        personData.postValue(PersonModel())
    }

    fun editPerson(personModel: PersonModel,view: View,position:Int){
        personData.postValue(personModel)
        this.position = position
        editData = true
        val action = HomeFragmentDirections.actionHomeFragmentToPersonalDetail()
        action.header = "Personal Detail Edit"
        action.addButton = false
        view.findNavController().navigate(action)
    }

    fun deletePerson(position: Int):Boolean{
        this.position = position
        showDeleteDialog.postValue(true)
        return false
    }

}

data class PersonModel(
    var name:String = "",
    var email:String = "",
    var mobile:String = "",
)