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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


val KEY_IS_FIRST_TIME = "in.heis.drivysteuerung.htlinn.first_time"
val KEY = "in.heis.drivysteuerung.htlinn"
var btn_pressed: Boolean = false
var connectedDevice: String? = null


open class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{

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

        if (isFirstTime()) {
            // TODO: Hilfe Anzeigen - kleine Einleitung
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
        menuInflater.inflate(R.menu.hb_menu, menu)
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


    /**
     * Funktion: isFirstTime
     * Input: -
     * Output: Boolean
     * Beschreibung: Überprüfung ob die App zum erseten mal nach der Installation gestartet wurde
     */
    fun isFirstTime(): Boolean {
        return getSharedPreferences(KEY, Context.MODE_PRIVATE).getBoolean(KEY_IS_FIRST_TIME, true)
    }


}

