package com.android.spacexclient.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacexclient.R
import com.android.spacexclient.SpaceXClientApplication
import com.android.spacexclient.databinding.ActivityMainBinding
import com.android.spacexclient.domain.RocketModel
import com.android.spacexclient.presentation.utils.AppSharedPreferenceManager
import com.android.spacexclient.presentation.utils.UIState
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RocketListingViewModel

    private lateinit var binding: ActivityMainBinding

    private val adapter = RocketAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        sharedPreferences = (applicationContext as SpaceXClientApplication).component.getSharedPrefs()

        if(AppSharedPreferenceManager(sharedPreferences).getIsFirstTime())
            startActivity(Intent(this, OnBoardingActivity::class.java))


        (applicationContext as SpaceXClientApplication).component.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[RocketListingViewModel::class.java]

        viewModel.getAllRockets().observe(this) {
            it?.let {
                when (it) {
                    is UIState.Success -> showData(it.data)
                    is UIState.Error -> showError(it.message)
                    is UIState.Loading -> showLoading()
                }
            }
        }

        viewModel.getRefreshing().observe(this) {
            it?.let {
                when (it) {
                    is UIState.Error -> showRefreshingError(it.message)
                    is UIState.Loading -> showRefreshing()
                    is UIState.Success -> hideRefreshing()
                }
            }
        }


        setUpList()
        setUpRefreshListener()
        viewModel.getRockets()
    }

    private fun hideRefreshing() {
        binding.refreshRockets.isRefreshing = false
    }


    private fun setUpList() {
        binding.rocketList.adapter = adapter
        binding.rocketList.layoutManager = LinearLayoutManager(this)
    }

    private fun setUpRefreshListener() {
        binding.refreshRockets.setOnRefreshListener {
            viewModel.refreshRocketList()
        }
    }

    private fun showRefreshing() {
        binding.refreshRockets.isRefreshing = true
    }

    private fun showRefreshingError(message: String) {
        binding.refreshRockets.isRefreshing = false

        val snackbar = Snackbar
            .make(binding.refreshRockets, message, Snackbar.LENGTH_LONG)
        snackbar.show()

        viewModel.clearRefreshState()
    }

    private fun showLoading() {
        binding.refreshRockets.isGone = true

        binding.progressBar.isVisible = true

        binding.errorMessage.isGone = true
        binding.button.isGone = true
    }

    private fun showError(message: String) {
        binding.refreshRockets.isGone = true

        binding.progressBar.isGone = true

        binding.errorMessage.isVisible = true
        binding.button.isVisible = true
        binding.errorMessage.text = message
    }

    private fun showData(list: List<RocketModel>) {
        binding.refreshRockets.isVisible = true

        binding.progressBar.isGone = true

        binding.errorMessage.isGone = true
        binding.button.isGone = true

        adapter.submitList(list)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val activeSwitch = menu.findItem(R.id.switch_action_bar)
        activeSwitch.setActionView(R.layout.use_switch)
        val sw =
            menu.findItem(R.id.switch_action_bar).actionView.findViewById<View>(R.id.switch2) as SwitchCompat
        sw.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.getActiveRockets()
            else
                viewModel.getRockets()

        }
        return true
    }
}