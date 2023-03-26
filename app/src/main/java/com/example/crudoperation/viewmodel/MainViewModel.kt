package com.example.crudoperation.viewmodel

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
    var pImage= MutableLiveData<String>()
    var position = 0
    var showDeleteDialog = MutableLiveData<Boolean>()
    var addViewButton = MutableLiveData<Boolean>()
    val showToast = MutableLiveData<String>()
    val searchText = MutableLiveData<String>()

    init {
        showToast.value = ""
        addViewButton.value = false
        showDeleteDialog.value =false
        personArrayList = MutableLiveData(ArrayList())
        personData.value = PersonModel()
    }

    fun personSave(view: View){
        if (validated()){
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
    }

    private fun validated(): Boolean {
        var isValid = true
        if (personData.value!!.name.isEmpty()){
            showToast.postValue("Name Cannot Be Blank")
            isValid = false
        }else if (personData.value!!.mobile.isEmpty()){
            showToast.postValue("Mobile Number Cannot Be Blank")
            isValid = false
        }else if (personData.value!!.email.isEmpty()){
            showToast.postValue("Email Cannot Be Blank")
            isValid = false
        }
        return isValid
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