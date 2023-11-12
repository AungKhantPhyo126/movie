package com.akpdev.movies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.findNavController()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener {menu->
            navigateToSelectedScreen(menu)
        }


    }
    private fun navigateToSelectedScreen(menu:MenuItem):Boolean{
        when(menu.itemId){
            R.id.movieListFragment->{navController?.navigate(R.id.movieListFragment)}
            R.id.favoriteMoviesFragment->{navController?.navigate(R.id.favoriteMoviesFragment)}
        }
        return true
    }
}