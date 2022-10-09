package com.example.crudoperation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudoperation.MainActivity
import com.example.crudoperation.adapter.PersonAdapter
import com.example.crudoperation.databinding.FragmentHomeBinding
import com.example.crudoperation.viewmodel.MainViewModel
import com.example.crudoperation.viewmodel.PersonModel


class HomeFragment : Fragment() {
   lateinit var binding: FragmentHomeBinding
    val model : MainViewModel by activityViewModels()
    var mainActivity : MainActivity? = null
    var personAdapter: PersonAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            model.editData = false
            val action = HomeFragmentDirections.actionHomeFragmentToPersonalDetail("Personal Detail from arg")
            findNavController().navigate(action)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity!!.showDailog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        model.personArrayList.observe(viewLifecycleOwner){
            if (it != null && it.isNotEmpty()){
                Log.d("TAG", "onViewCreated: $it")
                setAdapter(it)
            }
        }
        model.showDeleteDialog.observe(viewLifecycleOwner){
            if (it){
                showConfirmDialog()
                model.showDeleteDialog.postValue(false)
            }
        }
    }

    private fun showConfirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Crud Operation")
        builder.setMessage("Are You Sure You Want To Delete ${model.arrayList.get(model.position).name}?")

        builder.setPositiveButton("Yes"){dailog,which ->
            dailog.dismiss()
            model.arrayList.removeAt(model.position)
            binding.recView.adapter!!.notifyDataSetChanged()
        }
        builder.setNegativeButton("No"){ dailog ,_->
            dailog.dismiss()
        }
        builder.show()
    }

    private fun setAdapter(arrayList: ArrayList<PersonModel>) {
        personAdapter = PersonAdapter(arrayList,model)
        binding.recView.layoutManager = LinearLayoutManager(context)
        binding.recView.adapter = personAdapter
    }

}