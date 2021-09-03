package com.karandeepsingh.foodistan.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.fragments.FAQ
import com.karandeepsingh.foodistan.fragments.Favourites
import com.karandeepsingh.foodistan.fragments.HomePage
import com.karandeepsingh.foodistan.fragments.Profile

class HomeScreen : AppCompatActivity() {


    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var FrameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        var previousItem:MenuItem?=null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        FrameLayout = findViewById(R.id.FrameLayout)
        navigationView = findViewById(R.id.navigationView)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        setSupportActionBar(toolbar)
        openHome()
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeScreen,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if(previousItem?.isChecked==true)
            {
                previousItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousItem=it
            val runnable= Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler(Looper.getMainLooper()).postDelayed(runnable,100)
            when (it.itemId) {
                R.id.HomePage -> {
                    openHome()
                    supportActionBar?.title = "All Restaurants"

                }
                R.id.Favourites -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.FrameLayout,
                        Favourites()
                    ).commit()
                    supportActionBar?.title = "Favourites"

                }
                R.id.Profile -> {
                    supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, Profile())
                        .commit()
                    supportActionBar?.title = "Profile"


                }
                R.id.FAQ -> {
                    supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, FAQ())
                        .commit()
                    supportActionBar?.title = "FAQs"

                }
                R.id.Logout -> {
                    sharedPreferences.edit().clear().apply()
                    val intent = Intent(this@HomeScreen, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            return@setNavigationItemSelectedListener true
        }



    }

    fun openHome() {
        supportFragmentManager.beginTransaction().replace(R.id.FrameLayout, HomePage()).commit()
        navigationView.setCheckedItem(R.id.HomePage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.FrameLayout)
        when(frag)
        {
            !is HomePage->{openHome()
                supportActionBar?.title="All Restaurants"}
            else-> super.onBackPressed()
        }

    }
}