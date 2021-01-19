package com.udacity.asteroidradar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ListItemBinding

class AsteroidsAdapter(private val clickListener: AsteroidClickListener):
        ListAdapter<Asteroid, RecyclerView.ViewHolder>(DiffCallback()) {

    class ViewHolder private constructor(private val binding: ListItemBinding):
            RecyclerView.ViewHolder(binding.root) {

            fun bind(item: Asteroid, clickListener: AsteroidClickListener){
                binding.asteroid = item
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }

            companion object {
                fun from(parent: ViewGroup): ViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                    return ViewHolder(binding)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder as ViewHolder
        holder.bind(asteroid, clickListener)
    }



    class DiffCallback: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }



    class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit){
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }


}