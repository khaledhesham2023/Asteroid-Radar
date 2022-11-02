package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding the fragment to UI using data binding
        val binding = FragmentDetailBinding.inflate(inflater)
        // setting the lifecycle owner
        binding.lifecycleOwner = this

        // safe Args to pass selected asteroid data from MainFragment recyclerview item to details fragment
        val asteroid = DetailFragmentArgs.fromBundle(arguments!!).selectedAsteroid
        // binding asteroid in UI to asteroid transferred from MainFragment
        binding.asteroid = asteroid
        // actions taken when pressing help button which is showing dialog about astronomical unit explanation unit
        binding.helpButton.setOnClickListener {
            displayAstronomicalUnitExplanationDialog()
        }
        // return the view along with data binding
        return binding.root
    }
// A function that is called to set the alertDialog
    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(activity!!)
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
