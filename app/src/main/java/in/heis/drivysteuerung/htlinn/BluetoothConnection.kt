package `in`.heis.drivysteuerung.htlinn

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.widget.CircularProgressDrawable
import android.support.v4.widget.DrawerLayout
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_connection.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class BluetoothConnection(val context: Context) : AsyncTask<Void, Void, String>() {
    private var connectSuccess: Boolean = true

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001806-0000-1000-8000-00805f9b34fb")
        var m_bluetoothSocket: BluetoothSocket? = null
        val m_bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        var m_isConnected: Boolean = false
        var m_address: String? = null
        var isFinished: Boolean = false
        lateinit var m_progress: ProgressDialog
    }

    //////////////////////////////////////////////////////////////////////////

    fun isEnabled(): Boolean {
        if (BluetoothConnection.m_bluetoothAdapter == null) Toast.makeText(
            context,
            "not sup",
            Toast.LENGTH_SHORT
        ).show()
        if (!m_bluetoothAdapter!!.isEnabled) {
            return false
        }
        return true
    }

    fun getPairedDevList(): ArrayList<String> {
        val m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val btDeviceString_list: ArrayList<String> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {

            m_pairedDevices?.forEach { device ->
                btDeviceString_list.add(device.name + "\n" + device.address)
            }
        } else Toast.makeText(context, "no paired Dev", Toast.LENGTH_SHORT).show()


        return btDeviceString_list
    }


    /////////////////////////////////////////////////////////////


    override fun onPreExecute() {
        super.onPreExecute()
        isFinished = false
        Toast.makeText(context, "connecting", Toast.LENGTH_SHORT).show()
        m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
    }

    override fun doInBackground(vararg p0: Void?): String? {

        if (m_bluetoothSocket == null || !m_isConnected) {
            val device: BluetoothDevice = m_bluetoothAdapter!!.getRemoteDevice(
                m_address
            )
            try {
                println("yes")

                m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(
                    m_myUUID
                )
                println("yes3")
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                m_bluetoothSocket!!.connect()
                println("yes4")

            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }

        }
        return true.toString()

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (!connectSuccess) {
            Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show()
        } else {
            m_isConnected = true
        }
        m_progress.dismiss()
        SelectMenu(null, null, null).makeNewLayout()

    }


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