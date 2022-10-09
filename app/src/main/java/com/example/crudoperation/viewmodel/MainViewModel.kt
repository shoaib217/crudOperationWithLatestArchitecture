package com.example.crudoperation.viewmodel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.crudoperation.R

class MainViewModel: ViewModel() {
    lateinit var arrayList:ArrayList<PersonModel>
    var personArrayList = MutableLiveData<ArrayList<PersonModel>>()
    val personData = MutableLiveData<PersonModel>()
    var editData:Boolean = false
    var position = 0
    var showDeleteDialog = MutableLiveData<Boolean>()

    init {
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
        view.findNavController().navigate(R.id.action_homeFragment_to_personalDetail)
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