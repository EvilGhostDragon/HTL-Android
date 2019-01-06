package `in`.heis.drivysteuerung.htlinn


import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.preference.SwitchPreference
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_connection.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.text.FieldPosition
import java.util.concurrent.TimeUnit
import android.widget.Toast
import `in`.heis.drivysteuerung.htlinn.MainActivity
import android.net.sip.SipSession


class ConnectionFragment : Fragment() {

    val REQUEST_ENABLE_BLUETOOTH = 1
    val addNewDevTxt = "Neues Gerät koppeln"

    companion object {
        var selectedDevice: String = "Raspberry"
        var selectedAdress: String? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.text_con_selecteddev.text = ConnectionFragment.selectedDevice

        checkHW()
        makeList()

        switch_con_selecedev.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) ConnectionFragment.selectedDevice = "Arduino"
                else ConnectionFragment.selectedDevice = "Raspberry"

                activity!!.text_con_selecteddev.text = ConnectionFragment.selectedDevice
            }
        })

        refresh_connection.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                checkHW()
                makeList()
                refresh_connection.isRefreshing = false
            }

        })


    }

    fun makeList() {
        var m_continue = false

        var btDevice_list = BluetoothConnection(context!!).getPairedDevList()
        btDevice_list.add(addNewDevTxt)

        val adapter = ArrayAdapter(context!!, android.R.layout.simple_expandable_list_item_1, btDevice_list)
        listv_con_paired.adapter = adapter

        listv_con_paired.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            if (btDevice_list[position] == addNewDevTxt) {
                SelectMenu(R.id.nav_help_con, drawer_layout, activity!!).action()
            } else {
                val device: String = btDevice_list[position]
                val address: String = device.substring(device.length - 17)

                BluetoothConnection.m_address = address
                ConnectionFragment.selectedAdress = device


                connect()
            }
        }


    }

    fun connect() {
        var selectedDivText_info: String = "Plattform: " + selectedDevice + "\n" + selectedAdress
        SelectMenu.view = activity!!.drawer_layout
        SelectMenu.act = activity!!

        println(BluetoothConnection.m_myUUID)

        AlertDialog.Builder(context!!)
            .setTitle("Verbindung herstellen?")
            .setMessage(selectedDivText_info)

            .setNegativeButton("Abbruch") { _, _ -> null }
            .setPositiveButton("Ja") { dialog, which ->


                BluetoothConnection(context!!).execute()

            }
            .show()


    }


    fun checkHW() {
        if (!BluetoothConnection(context!!).isEnabled()) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        BluetoothConnection(context!!).context
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (BluetoothConnection.m_bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(activity, "has been enabled", Toast.LENGTH_SHORT).show()
                    makeList()
                } else Toast.makeText(activity, "has been disabled", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) Toast.makeText(
                activity,
                "cancl",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

