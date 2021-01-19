package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidsAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecyclerViewAdapter()
        setupObservers()

        activity?.setTitle(getString(R.string.app_name))
        setHasOptionsMenu(true)

        return binding.root
    }


    private fun setupObservers() {
        viewModel.asteroids.observe(viewLifecycleOwner, { asteroids ->
            if (asteroids != null){
                adapter.submitList(asteroids)
            }
        })

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, { asteroid ->
            if (asteroid != null){
                navigateToDetailFragment(asteroid)
                viewModel.doneNavigating()
            }
        })
    }

    private fun setupRecyclerViewAdapter() {
        adapter = AsteroidsAdapter(AsteroidsAdapter.AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
    }

    private fun navigateToDetailFragment(asteroid: Asteroid){

        // Navigate to Detail Fragment
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            // Select Week Asteroids
            R.id.view_week_asteroids_menu -> viewModel.onMenuSelectWeekAsteroids()

            // Select Today Asteroids
            R.id.view_today_asteroids_menu -> viewModel.onMenuSelectTodayAsteroids()

            // Select Saved Asteroids
            R.id.view_saved_asteroids_menu -> viewModel.onMenuSelectSavedAsteroids()
        }
        return true
    }
}
