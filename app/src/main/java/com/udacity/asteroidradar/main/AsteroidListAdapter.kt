package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

/**
 * @param asteroidClickListener to define actions to be done once an item is clicked
 */
class AsteroidListAdapter(val asteroidClickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidListAdapter.AsteroidViewHolder>(DiffCallback) {

    class AsteroidViewHolder(private val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // data binding to bind asteroid to data in xml
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    // create the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(
            ItemAsteroidBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //to display data in the specified location
    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
        holder.itemView.setOnClickListener {
            asteroidClickListener.onClick(asteroid)
        }
    }

    // class clickListener that is used to save data about asteroid item clicked
    class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
    // DiffCallback to replace old items by new ones
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }
}
