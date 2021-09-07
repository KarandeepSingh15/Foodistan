package com.karandeepsingh.foodistan.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.karandeepsingh.foodistan.R
import com.karandeepsingh.foodistan.fragments.*

class HomeScreen : AppCompatActivity() {


    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var FrameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtNavigationHeaderSubtext: TextView
    lateinit var txtNavigationHeaderText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        var previousItem: MenuItem? = null

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        FrameLayout = findViewById(R.id.FrameLayout)
        navigationView = findViewById(R.id.navigationView)
        val NavigationHeader = navigationView.getHeaderView(0)
        txtNavigationHeaderText = NavigationHeader.findViewById(R.id.txtNavigationHeaderText)
        txtNavigationHeaderSubtext = NavigationHeader.findViewById(R.id.txtNavigationHeaderSubtext)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        txtNavigationHeaderText.text = sharedPreferences.getString("name", "Navigation Drawer")
        txtNavigationHeaderSubtext.text = sharedPreferences.getString("mobile_number", "")

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
            if (previousItem?.isChecked == true) {
                previousItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousItem = it
            val runnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler(Looper.getMainLooper()).postDelayed(runnable, 100)
            when (it.itemId) {
                R.id.HomePage -> {
                    openHome()
                    supportActionBar?.title = "All Restaurants"

                }
                R.id.OrderHistoryFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.FrameLayout, OrderHistory()).commit()
                    supportActionBar?.title="My Previous Orders"
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
                    val alert = AlertDialog.Builder(this@HomeScreen)
                    alert.setTitle("Confirmation")
                    alert.setMessage("Are you sure you want to exit?")
                    alert.setPositiveButton("Yes") { text, Listener ->
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@HomeScreen, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    alert.setNegativeButton("No") { text, Listener ->
                        navigationView.setCheckedItem(R.id.HomePage)

                    }
                    alert.create()
                    alert.show()

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
        val frag = supportFragmentManager.findFragmentById(R.id.FrameLayout)
        when (frag) {
            !is HomePage -> {
                openHome()
                supportActionBar?.title = "All Restaurants"
            }
            else -> super.onBackPressed()
        }

    }
}