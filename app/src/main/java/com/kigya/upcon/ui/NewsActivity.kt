package com.kigya.upcon.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.kigya.upcon.R
import com.kigya.upcon.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity(R.layout.activity_news) {

    private val viewBinding by viewBinding(ActivityNewsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewBinding) {
            newsNavHostFragment
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment
        ) as NavHostFragment

        val navController = navHostFragment.navController
        viewBinding.bottomNavigationView.setupWithNavController(navController)
    }

}