package com.project.vnpr.Activity

import android.content.Intent
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
//import android.widget.Toolbar
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.project.vnpr.*
import com.project.vnpr.Fragment.Scanner
import com.project.vnpr.Fragment.VehicleInfo

class MainActivity : AppCompatActivity() {
    companion object{
        var size: Point = Point()
        fun  go(){

        }
    }

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var mAuth:FirebaseAuth

    //    lateinit var viewPager: ViewPager
//    lateinit var adapter: ImageAdapter
//
    var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        windowManager.defaultDisplay.getSize(size)
        setContentView(R.layout.activity_main)

//        viewPager =findViewById(R.id.viewPager)
//         adapter =  ImageAdapter(this);
//        viewPager.adapter = adapter;

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        setUpToolbar()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.frame,
                VehicleInfo()
            )
            .addToBackStack("Vehicle Information")
            .commit()
        supportActionBar?.title="Vehicle Information"


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )



        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener {
            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.vehicleInformation -> {
                    openVehicleInfo()
                    drawerLayout.closeDrawers()
                }
                R.id.scanner -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            Scanner()
                        )
                        .addToBackStack("Scanner")
                        .commit()

                    supportActionBar?.title = "SCANNER"
                    drawerLayout.closeDrawers()
                }

                R.id.logout -> {
                    FirebaseAuth.getInstance().signOut();
                    finish()
                   val intent = Intent(this, login::class.java)
                    startActivity(intent)
                }
                R.id.Share -> {

                    val intent = Intent(Intent.ACTION_VIEW)
                    //intent.action = Intent.ACTION_SEND
                    intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=com.project.vnpr"))
                    //intent.putExtra(Intent.EXTRA_SUBJECT,"VEHICLE NUMBER PLATE RECOGNITION APP")
                    //intent.putExtra(Intent.EXTRA_TEXT,"BY SCANNING VEHICLE NUMBER PLATE")
                    startActivity(intent)
                }
                R.id.Feedback -> {

                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Vehicle Information"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openVehicleInfo() {
        val fragment = VehicleInfo()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Vehicle Information"
        navigationView.setCheckedItem(R.id.vehicleInformation)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when (frag) {
            !is VehicleInfo -> openVehicleInfo()
            else -> super.onBackPressed()
        }

    }
}
