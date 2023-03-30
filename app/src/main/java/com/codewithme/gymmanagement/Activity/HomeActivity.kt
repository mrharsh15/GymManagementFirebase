package com.codewithme.gymmanagement.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.codewithme.gymmanagement.R
import com.codewithme.gymmanagement.databinding.ActivityHomeBinding
import com.codewithme.gymmanagement.fragment.FragmentAddMember
import com.codewithme.gymmanagement.fragment.HomeFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawer:DrawerLayout
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        setSupportActionBar(binding.homeInclude.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawer = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawer,binding.homeInclude.toolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        binding.navView.setNavigationItemSelectedListener(this)
        toggle.syncState()
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // User not signed in, go back to login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            loadFragment(HomeFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
//                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
                val fragment = HomeFragment()
                loadFragment(fragment)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_add -> {
                loadFragment(FragmentAddMember())
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }

            }
//            R.id.nav_nav_fee_pending -> {
////                Toast.makeText(this, "Fee Pending", Toast.LENGTH_LONG).show()
//                val fragment = FragmentFeePending()
//                loadFragment(fragment)
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START)
//                }
//            }
////            R.id.nav_update_fee -> {
//////                Toast.makeText(this, "Update Fee", Toast.LENGTH_LONG).show()
////                val fragment = FragmentAppUpdateFee()
////                loadFragment(fragment)
////                if (drawer.isDrawerOpen(GravityCompat.START)) {
////                    drawer.closeDrawer(GravityCompat.START)
////                }
////            }
//            R.id.nav_change_password -> {
////                Toast.makeText(this, "Change Password", Toast.LENGTH_LONG).show()
//                val fragment = FragmentChangePassword()
//                loadFragment(fragment)
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START)
//                }
//            }
//            R.id.nav_log_out -> {
////                Toast.makeText(this, "Log Out", Toast.LENGTH_LONG).show()
//                logOut()
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START)
//                }
//            }
//            R.id.nav_import_export_database -> {
////                Toast.makeText(this, "Home", Toast.LENGTH_LONG).show()
//                val fragment = FragmentImportExportDatabase()
//                loadFragment(fragment)
//                if (drawer.isDrawerOpen(GravityCompat.START)) {
//                    drawer.closeDrawer(GravityCompat.START)
//                }
//            }
        }
        return true
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(supportFragmentManager.backStackEntryCount>0){
            supportFragmentManager.popBackStack()
        }
    }
}