package `in`.heis.drivysteuerung.htlinn

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Klasse
 * Beschreibung: Aufbau der Verbindung, Trennung der Verbindung, Überprüfung, Rückgabe der gekoppelten Geräte, Datenübertragung
 */
class BluetoothConnection(val context: Context) : AsyncTask<Void, Void, Boolean>() {
    private var connectSuccess: Boolean = true

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001806-0000-1000-8000-00805f9b34fb")
        var m_bluetoothSocket: BluetoothSocket? = null
        val m_bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var m_isConnected: Boolean = false
        var m_address: String? = null
        lateinit var m_progress: ProgressDialog
        var isFinished: Boolean = false
    }

    /**
     * Funktion: isEnabled
     * Input: -
     * Output: Boolean
     * Beschreibung: Überprüfung ob Bluetooth unterstüctzt und eingeschalten ist.
     */

    fun isEnabled(): Boolean {
        if (BluetoothConnection.m_bluetoothAdapter == null) Toast.makeText(
            context,
            "Ihr Gerät wird nicht unterstützt",
            Toast.LENGTH_SHORT
        ).show()
        if (!m_bluetoothAdapter!!.isEnabled) {
            return false
        }
        return true
    }

    /**
     * Funktion: getPairedDevList
     * Input: -
     * Output: btDeviceString_list [Liste von bereits gekoppelten Geräte]
     * Beschreibung: Fkt schreibt alle bereits gekoppelten Geräte in eine Liste
     */
    fun getPairedDevList(): ArrayList<String> {
        val m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val btDeviceString_list: ArrayList<String> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {

            m_pairedDevices?.forEach { device ->
                btDeviceString_list.add(device.name + "\n" + device.address)
            }
        } else println("no paired Dev found")/*DEBUG*/


        return btDeviceString_list
    }


    /////////////////////////////////////////////////////////////
    ////// AsyncTask - Start
    /////////////////////////////////////////////////////////////

    /**
     * Beschreibung: Vorbereitung bevor Verbindung hergestellt wird - erstellen des ProgressDialogs
     */
    override fun onPreExecute() {
        super.onPreExecute()
        m_progress = ProgressDialog.show(context, "Verbinden...", "Bitte habe einen Moment Geduld")
    }

    /**
     * Beschreibung: Tatsächlicher Verbindungsaufbau:
     *                  (1) Überprüfung ob bereits eine Verbindung besteht
     *                  (2) Socket erstellen
     *                  (3) Verbindung herstellen
     */
    override fun doInBackground(vararg p0: Void?): Boolean? {

        if (m_bluetoothSocket == null || !m_isConnected) {
            val device: BluetoothDevice = m_bluetoothAdapter!!.getRemoteDevice(
                m_address
            )
            try {
                m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(
                    m_myUUID
                )
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                m_bluetoothSocket!!.connect()
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }

        }
        return true

    }

    /**
     * Beschreibung: Überprüfung ob Verbindungsaufbau erfolgreich war
     *                  (b:y) Freigabe des Steuerfragmentes
     *                  (b:n) Toast
     */
    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (!connectSuccess) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
        } else {
            m_isConnected = true
        }
        m_progress.dismiss()
        SelectMenu(null, null, null).makeNewLayout()

    }

    /////////////////////////////////////////////////////////////
    ////// AsyncTask - End
    /////////////////////////////////////////////////////////////


    fun disconnect() {

        Toast.makeText(context, "disconnecting", Toast.LENGTH_SHORT).show()
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        //finish()
    }

    fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}