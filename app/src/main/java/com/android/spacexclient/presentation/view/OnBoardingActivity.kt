package com.android.spacexclient.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.spacexclient.SpaceXClientApplication
import com.android.spacexclient.databinding.ActivityOnBoardingBinding
import com.android.spacexclient.presentation.utils.AppSharedPreferenceManager

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences =
            (applicationContext as SpaceXClientApplication).component.getSharedPrefs()

        if (!AppSharedPreferenceManager(sharedPreferences).getIsFirstTime())
            startActivity(Intent(this, MainActivity::class.java))
        else
        {
            binding = ActivityOnBoardingBinding.inflate(layoutInflater)
            val root = binding.root
            setContentView(root)

            binding.button2.setOnClickListener {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

    }
}