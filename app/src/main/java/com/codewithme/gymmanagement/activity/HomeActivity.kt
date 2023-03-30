package com.codewithme.gymmanagement.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codewithme.gymmanagement.R
import com.codewithme.gymmanagement.databinding.ActivityHomeBinding
import com.codewithme.gymmanagement.fragment.FragmentAddMember
import com.codewithme.gymmanagement.fragment.HomeFragment
import com.codewithme.gymmanagement.fragment.RenewFragment
import com.codewithme.gymmanagement.model.User
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var drawer:DrawerLayout
    lateinit var binding: ActivityHomeBinding
    private  val bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

//        setSupportActionBar(binding.homeInclude.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.homeInclude.toolbar.title = "Home"
        drawer = binding.drawerLayout
        toggle = ActionBarDrawerToggle(this, drawer,binding.homeInclude.toolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        binding.navView.setNavigationItemSelectedListener(this)
        toggle.syncState()

        val header = binding.navView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.username)
        FirebaseDatabase.getInstance().reference.child("user").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)!!
                userName.text = "Welcome, ${user.name}"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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
                binding.homeInclude.toolbar.title = "Home"
                val fragment = HomeFragment()
                loadFragment(fragment)
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }
            }
            R.id.nav_add -> {
                binding.homeInclude.toolbar.title = "Add Member"
                bundle.putBoolean("renew", false)
                loadFragment(FragmentAddMember())
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                }

            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut().apply {
                    startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    finish()
                }
            }
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
            fragment.arguments = bundle
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