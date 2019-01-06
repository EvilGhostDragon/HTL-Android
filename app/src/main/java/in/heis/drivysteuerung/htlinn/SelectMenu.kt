package `in`.heis.drivysteuerung.htlinn

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.support.design.widget.NavigationView
import android.support.v4.app.*
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import android.support.v4.app.Fragment
import android.view.MenuItem
import kotlin.system.exitProcess
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.abc_activity_chooser_view.*
import kotlinx.android.synthetic.main.abc_screen_toolbar.*
import kotlinx.android.synthetic.main.abc_search_view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.design_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.nav_header_main.*


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
                activity!!.nav_view.menu.findItem(R.id.nav_connection_con).isEnabled = true

                activity!!.nav_view.menu.findItem(R.id.nav_control_man).isEnabled = false
                activity!!.nav_view.menu.findItem(R.id.nav_control_aut).isEnabled = false

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

            R.id.nav_control_man -> {
                ControlFragmentMan()
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

        if (activity!!.nav_view.menu.findItem(itemId!!) != null) {

            val id: MenuItem? = activity!!.nav_view.checkedItem
            id?.isChecked = false
            activity!!.nav_view.menu.findItem(itemId!!).isChecked = true
        }
        return true
    }


    open fun makeNewLayout() {


        var selectedDivText: String =
            "Mit ${ConnectionFragment.selectedDevice} verbunden\n${ConnectionFragment.selectedAdress}"
        println(BluetoothConnection.m_isConnected.toString())

        println(view)
        println(act)
        if (!BluetoothConnection.m_isConnected) CreateAlertdialog(
            view?.context!!,
            "Verbindung konnte nicht aufgebaut werden"
            , null
        ).error()
        else {
            act!!.nav_view.menu.findItem(R.id.nav_control_man).isEnabled = true
            act!!.nav_view.menu.findItem(R.id.nav_control_aut).isEnabled = true
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
