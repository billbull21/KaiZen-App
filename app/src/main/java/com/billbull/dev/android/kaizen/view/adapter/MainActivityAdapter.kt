package com.billbull.dev.android.kaizen.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.billbull.dev.android.kaizen.databinding.ItemListBinding
import com.billbull.dev.android.kaizen.models.db.entity.ActivityModel

class MainActivityAdapter(private val dataList: List<ActivityModel>, private val listener: (ActivityModel) -> Unit)
    : RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder>() {

    private lateinit var binding: ItemListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        binding = ItemListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) {
        with(holder) {
            with(dataList[position]) {
                binding.tvActivityName.text = activity_name
                binding.tvActivityTime.text = activity_time

                itemView.setOnClickListener { listener(this) }
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class MainActivityViewHolder(binding: ItemListBinding): RecyclerView.ViewHolder(binding.root)

}