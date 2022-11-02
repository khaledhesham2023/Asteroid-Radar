package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // dataBinding instance
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // dataBinding the viewModel with xml file
        binding.viewModel = viewModel
        binding.statusLoadingWheel.visibility = View.VISIBLE

        // navigate to detail screen once a recyclerView item is clicked
        binding.asteroidRecycler.adapter =
            AsteroidListAdapter(AsteroidListAdapter.AsteroidClickListener {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
            })

        viewModel.viewProgress.observe(viewLifecycleOwner, Observer { viewProgress ->
            if (!viewProgress) {
                binding.statusLoadingWheel.visibility = View.GONE
            }
        })
        // enable options menu
        setHasOptionsMenu(true)
        // return view along with dataBinding
        return binding.root
    }

    // inflating the menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // actions to be done once an option item is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_all_menu -> {
                viewModel.getAllAsteroids()
            }
            R.id.show_week_menu -> {
                viewModel.getWeekAsteroids()
            }
            R.id.show_today_menu -> {
                viewModel.getTodayAsteroids()
            }
        }
        return true
    }

}
