package com.android.spacexclient.presentation

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.spacexclient.SpaceXClientApplication
import com.android.spacexclient.databinding.ActivityMainBinding
import com.android.spacexclient.databinding.BottomSheetDialogBinding
import com.android.spacexclient.domain.RocketModel
import com.android.spacexclient.presentation.utils.AppSharedPreferenceManager
import com.android.spacexclient.presentation.utils.Query
import com.android.spacexclient.presentation.utils.UIState
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RocketListingViewModel

    private lateinit var binding: ActivityMainBinding

    private val adapter = RocketAdapter()

    private val linearLayoutManager = LinearLayoutManager(this)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var query = Query()

    private val filterUiStatusKey = "Filter Status"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        sharedPreferences =
            (applicationContext as SpaceXClientApplication).component.getSharedPrefs()

        if (AppSharedPreferenceManager(sharedPreferences).getIsFirstTime())
            startActivity(Intent(this, OnBoardingActivity::class.java))


        (applicationContext as SpaceXClientApplication).component.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory)[RocketListingViewModel::class.java]

        viewModel.getAllRockets().observe(this) {
            it?.let {
                Timber.e("Get rockets state emitted ${it.toString()}")
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
        setUpRetryButton()
        setUpFilterButton()
    }

    private fun setUpFilterButton() {
        binding.floatingActionButton.setOnClickListener {
            showFilterDialog(query.onlyActive)
        }
    }

    private fun setUpRetryButton() {
        binding.button.setOnClickListener {
            refresh()
        }
    }

    private fun hideRefreshing() {
        binding.refreshRockets.isRefreshing = false
    }


    private fun setUpList() {
        binding.rocketList.adapter = adapter
        binding.rocketList.layoutManager = linearLayoutManager
    }

    private fun setUpRefreshListener() {
        binding.refreshRockets.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        viewModel.refreshRocketList()
        query = Query.EMPTY
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
        binding.listData.isGone = true
        binding.errorLayout.isGone = true

        binding.loading.isVisible = true
    }

    private fun showError(message: String) {
        binding.listData.isGone = true
        binding.loading.isGone = true

        binding.errorLayout.isVisible = true
        binding.errorMessage.text = message
    }

    private fun showData(list: List<RocketModel>) {
        binding.loading.isGone = true
        binding.errorLayout.isGone = true

        binding.listData.isVisible = true
        adapter.submitList(list)
    }

    private fun applyFilter(onlyActive: Boolean) {
        if(query.onlyActive == onlyActive)
            return
        else query.onlyActive = onlyActive
        viewModel.getRockets(query)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(filterUiStatusKey, query.onlyActive);
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        query.onlyActive = savedInstanceState.getBoolean(filterUiStatusKey)
    }

    private fun showFilterDialog(onlyActive: Boolean) {
        val bottomSheetDialog = BottomSheetDialog(this)

        val dialogBinding = BottomSheetDialogBinding
            .inflate(LayoutInflater.from(this))
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.checkBox.isChecked = onlyActive
        dialogBinding.applyFilterButton
            .setOnClickListener {
                applyFilter(dialogBinding.checkBox.isChecked)
                bottomSheetDialog.dismiss()
            }

        bottomSheetDialog.show()
    }

}
