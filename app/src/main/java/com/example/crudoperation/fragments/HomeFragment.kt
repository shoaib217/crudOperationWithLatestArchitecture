package com.example.crudoperation.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.crudoperation.MainActivity
import com.example.crudoperation.adapter.PersonAdapter
import com.example.crudoperation.databinding.FragmentHomeBinding
import com.example.crudoperation.viewmodel.MainViewModel
import com.example.crudoperation.viewmodel.PersonModel
import java.util.concurrent.Executor


class HomeFragment : Fragment() {
   lateinit var binding: FragmentHomeBinding
    val model : MainViewModel by activityViewModels()
    var mainActivity : MainActivity? = null
    var personAdapter: PersonAdapter? = null
    var personArray:ArrayList<PersonModel>?=null


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
        model.addViewButton.postValue(false)
        binding.floatingActionButton.setOnClickListener {
            model.editData = false
            val action = HomeFragmentDirections.actionHomeFragmentToPersonalDetail()
            action.header = "Personal Detail"
            action.addButton = true
            findNavController().navigate(action)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                mainActivity!!.showDailog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        binding.swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                binding.swipeRefresh.isRefreshing = false
                showDailog()
            }, 1000)
        }

        model.personArrayList.observe(viewLifecycleOwner){
            if (it != null && it.isNotEmpty()){
                Log.d("TAG", "onViewCreated: $it")
                setAdapter(it)
            }
        }

        model.searchText.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                personArray = ArrayList()
                for(i in model.personArrayList.value?.indices!!){
                    if (model.personArrayList.value!![i].name.contains(it)){
                        personArray!!.add(model.personArrayList.value!![i])
                    }
                }
                setAdapter(personArray!!)
            }
        }

        binding.searchView2.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty()!!){
                    personArray = ArrayList()
                    for(i in model.personArrayList.value?.indices!!){
                        if (model.personArrayList.value!![i].name.contains(newText.toString())){
                            personArray!!.add(model.personArrayList.value!![i])
                        }
                    }
                    setAdapter(personArray!!)
                }
                return false
            }

        })
        model.showDeleteDialog.observe(viewLifecycleOwner){
            if (it){
                showConfirmDialog()
                model.showDeleteDialog.postValue(false)
            }
        }
    }

    private fun showDailog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Crud Operation")
        builder.setMessage("Are You Sure You Want To Delete All Saved Data?")

        builder.setPositiveButton("Yes"){dailog,which ->
            dailog.dismiss()
            model.arrayList = ArrayList()
            setAdapter(model.arrayList)
        }
        builder.setNegativeButton("No"){ dailog ,_->
            dailog.dismiss()
        }
        builder.show()    }

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