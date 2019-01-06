package `in`.heis.drivysteuerung.htlinn

import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_connection.*
import kotlin.system.exitProcess
import `in`.heis.drivysteuerung.htlinn.SelectMenu
import android.app.Activity
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_home.*


val KEY_IS_FIRST_TIME = "in.heis.drivysteuerung.htlinn.first_time"
val KEY = "in.heis.drivysteuerung.htlinn"
public var btn_pressed: Boolean = false
var connectedDevice: String? = null


open class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

    fun isFirstTime(): Boolean {
        return getSharedPreferences(KEY, Context.MODE_PRIVATE).getBoolean(KEY_IS_FIRST_TIME, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        SelectMenu(-1,drawer_layout,this@MainActivity).action()
        println(this)
        if (isFirstTime()) {
            Toast.makeText(this, "yes", Toast.LENGTH_LONG).show()
            getSharedPreferences(KEY, Context.MODE_PRIVATE).edit().putBoolean(KEY_IS_FIRST_TIME, false).apply()
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        SelectMenu(item.itemId,drawer_layout,this@MainActivity).action()
        return true

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        SelectMenu(item.itemId,drawer_layout,this@MainActivity).action()
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)

    }


//
//    inner class MenuSelect(val itemId: Int) {
//
//        fun action() {
//
//            when (itemId) {
//                R.id.action_settings -> {
//                    Toast.makeText(this@MainActivity, "k", Toast.LENGTH_LONG).show()
//                    btn_pressed = !btn_pressed
//                    println(this@MainActivity.nav_view)
//                }
//                R.id.action_exit -> {
//                    exitProcess(-1)
//                }
//                R.id.action_connection -> {
//                    //Toast.makeText(this@MainActivity, "k", Toast.LENGTH_LONG).show()
//
//                }
//                else -> {
//                    change()
//                }
//
//
//            }
//        }
//
//        fun change() : Boolean{
//            val fragment = when (itemId) {
//                R.id.nav_home -> {
//                    HomeFragment()
//                }
//
//                R.id.nav_connection_con -> {
//                    ConnectionFragment()
//                }
//
//                R.id.nav_help_con -> {
//                    HelpConnectionFragment()
//                }
//                else -> {
//                    HomeFragment()
//                }
//            }
//                supportFragmentManager
//
//                .beginTransaction()
//                .replace(R.id.ContentPlaceholder, fragment)
//                .commit()
//            return true
//        }
//
//
//    }

}

