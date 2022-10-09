package com.example.crudoperation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudoperation.databinding.PersonCardBinding
import com.example.crudoperation.viewmodel.MainViewModel
import com.example.crudoperation.viewmodel.PersonModel

class PersonAdapter(val arrayList: ArrayList<PersonModel>, val model: MainViewModel) : RecyclerView.Adapter<PersonViewHolder>() {
    lateinit var binding: PersonCardBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        binding = PersonCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PersonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.binding.model = arrayList[position]
        holder.binding.viewModel = model
        holder.binding.position = position
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}

class PersonViewHolder(val binding: PersonCardBinding):RecyclerView.ViewHolder(binding.root) {

}
