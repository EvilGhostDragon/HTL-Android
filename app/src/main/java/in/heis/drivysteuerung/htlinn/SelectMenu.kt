package `in`.heis.drivysteuerung.htlinn

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlin.system.exitProcess


class SelectMenu(val itemId: Int?, override val containerView: View?, val activity: Activity?) : LayoutContainer {
    init {
        //   val activity: AppCompatActivity = getActivity(containerView?.context,) as AppCompatActivity
    }

    companion object {
        var view : View? = null
        var act: Activity? = null
    }


    fun action() {

        when (itemId) {
            R.id.action_settings -> {
                Toast.makeText(containerView?.context, "k", Toast.LENGTH_LONG).show()
                btn_pressed = !btn_pressed
            }
            R.id.action_exit -> {
                exitProcess(-1)
            }
            R.id.nav_connection_disc -> {
                BluetoothConnection(containerView?.context!!).disconnect()

                activity!!.nav_view.menu.findItem(R.id.nav_connection_disc).isEnabled = false
                activity.nav_view.menu.findItem(R.id.nav_connection_con).isEnabled = true

                activity.nav_view.menu.findItem(R.id.nav_control).isEnabled = false

                text_nav_selecteddev.text = "Zurzeit keine aktive Verbindung"

                change()

            }
            else -> {
                change()
            }


        }
    }

    fun change(): Boolean {
        val fragment = when (itemId) {
            R.id.nav_home -> {
                HomeFragment()
            }

            R.id.nav_connection_con -> {
                ConnectionFragment()
            }

            R.id.nav_control -> {
                ControlFragment()
            }

            R.id.nav_help_con -> {
                HelpConnectionFragment()
            }
            else -> {
                HomeFragment()
            }
        }

        (activity as FragmentActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.ContentPlaceholder, fragment)
            .commit()

        if (activity.nav_view.menu.findItem(itemId!!) != null) {

            val id: MenuItem? = activity.nav_view.checkedItem
            id?.isChecked = false
            activity.nav_view.menu.findItem(itemId).isChecked = true
        }
        return true
    }

    /**
     * Funktion: makeNewLayout
     * Input/Output: -
     * Beschreibung: Nach erfolgreichen Verbinden mit RasPi/Arduino wird der Steuerbereich der App und der Verbindung trennen Button aktiviert. Deaktiverit wird der Verbindung herstellen Button
     *                  (1) Dialog mit Hinweis ob Verbindung erfolgreich war
     *                  (2) Aktivierung und Deaktivierung der genannten Menus
     *                  (3) Weiterleiung auf das Homefragment
     */
    fun makeNewLayout() {


        var selectedDivText: String =
            "Mit ${ConnectionFragment.selectedDevice} verbunden\n${ConnectionFragment.selectedAdress}"
        //println(BluetoothConnection.m_isConnected.toString())

        if (!BluetoothConnection.m_isConnected) CreateAlertdialog(
            view?.context!!,
            "Verbindung konnte nicht aufgebaut werden. Überprüfe deine Einstellungen und ob dein Roboter in Reichweite steht. \n\nWeitere Hilfe findest du im Hilfebereich."
            , null
        ).error()
        else {
            act!!.nav_view.menu.findItem(R.id.nav_control).isEnabled = true
            act!!.nav_view.menu.findItem(R.id.nav_connection_disc).isEnabled = true
            act!!.nav_view.menu.findItem(R.id.nav_connection_con).isEnabled = false

            act!!.text_nav_selecteddev.text = selectedDivText

            CreateAlertdialog(
                view?.context!!,
                "Verbindungsaufbau war erfolgreich\nSteuerung jetzt aktiv",
                null
            ).info()
            SelectMenu(R.id.nav_home, drawer_layout, act!!).change()
        }

        //edit Activ
        //activity!!.nav_view.menu.findItem(R.id.nav_home).isChecked=true
    }
}
