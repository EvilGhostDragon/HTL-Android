package `in`.heis.drivysteuerung.htlinn


import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_connection.*
import java.util.*


class ConnectionFragment : Fragment() {

    val REQUEST_ENABLE_BLUETOOTH = 1

    companion object {
        var selectedDevice: String = "Raspberry"
        var selectedAdress: String? = null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_connection, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.text_con_selecteddev.text = ConnectionFragment.selectedDevice
        if (ConnectionFragment.selectedDevice == "Arduino") switch_con_selecedev.isChecked = true

        checkHW()
        makeList()
        switch_con_selecedev.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    ConnectionFragment.selectedDevice = "Arduino"
                    BluetoothConnection.m_myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
                } else {
                    ConnectionFragment.selectedDevice = "Raspberry"
                    BluetoothConnection.m_myUUID = UUID.fromString("00001806-0000-1000-8000-00805f9b34fb")
                }

                activity!!.text_con_selecteddev.text = ConnectionFragment.selectedDevice
            }
        })
        /**
         * Beschreibung: Herunterziehen bewirkt ein Neuaufbau der Liste der gekoppelten Geräte
         */
        refresh_connection.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                checkHW()
                makeList()
                refresh_connection.isRefreshing = false
            }

        })


    }

    /**
     * Funktion: makeList
     * Input: -
     * Output: -
     * Beschreibung: Erstellung der sichtbaren Liste im Fragment. Liste aller bereits gekoppelten Geräte (Name + Adresse) + "Neuses Gerät hinzufügen" Eintrag mit Weiterleiung auf Hilfe
     *              + Bei Klick auf Eintrag wird versuch eine Verbindung aufzubauen.
     */
    fun makeList() {
        val btDevice_list = BluetoothConnection(context!!).getPairedDevList()
        val addNewDevTxt = "Neues Gerät koppeln"

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

    /**
     * Funktion: connect
     * Input/Output: -
     * Beschreibung: Dialogfenster bei klick auf Eintrag der Liste - Bestätigung ob tatsächlich verbunden werden soll.
     *              (b:y): connect
     *              (b:n): nothing
     */
    fun connect() {
        var selectedDivText_info: String = "Plattform: " + selectedDevice + "\n" + selectedAdress
        SelectMenu.view = activity!!.drawer_layout
        SelectMenu.act = activity!!

        AlertDialog.Builder(context!!)
            .setTitle("Verbindung herstellen?")
            .setMessage(selectedDivText_info)
            .setNegativeButton("Nein") { _, _ -> null }
            .setPositiveButton("Ja") { dialog, which ->
                BluetoothConnection(context!!).execute()
            }
            .show()
    }

    /**
     * Funktion: checkHW
     * Input/Output: -
     * Beschreibung: Fkt Überprüft ob Bluetooth aktiv ist.
     *              (b:y): nothing
     *              (b:n): Stellt Frage ob Bluetooth aktiviert weden soll
     */

    fun checkHW() {
        if (!BluetoothConnection(context!!).isEnabled()) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    /**
     * Beschreibung: Anforderung der Berechtigung Bluetooth zu aktivieren
     *              (b:y): Bluetooth aktivieren
     *              (b:n): Toast
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        BluetoothConnection(context!!).context
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (BluetoothConnection.m_bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(activity, "Bluetooth wurde aktiviert", Toast.LENGTH_SHORT).show()
                    makeList()
                } else Toast.makeText(activity, "Bluetooth wurde deaktiviert", Toast.LENGTH_SHORT).show()
            } else if (resultCode == Activity.RESULT_CANCELED) Toast.makeText(
                activity,
                "Bitte schalte Bluetooth ein",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

