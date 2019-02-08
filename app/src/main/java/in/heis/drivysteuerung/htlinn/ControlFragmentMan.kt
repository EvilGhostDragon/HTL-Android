package `in`.heis.drivysteuerung.htlinn


import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_control_man.*
import kotlinx.android.synthetic.main.fragment_control_man.view.*
import java.io.IOException
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ControlFragmentMan : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment



        return inflater.inflate(R.layout.fragment_control_man, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.seekBar!!.max = 10

        view.seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                BluetoothConnection(context!!).sendCommand(progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}


            override fun onStopTrackingTouch(seekBar: SeekBar) {}



        })
        //BluetoothConnection(context!!).execute()

        button_ctr_man_up.setOnClickListener { BluetoothConnection(context!!).sendCommand("up") }
        button_ctr_man_down.setOnClickListener { BluetoothConnection(context!!).sendCommand("down") }
        button_ctr_man_left.setOnClickListener { BluetoothConnection(context!!).sendCommand("left") }
        button_ctr_man_right.setOnClickListener { BluetoothConnection(context!!).sendCommand("right") }
        button_ctr_man_stop.setOnClickListener { BluetoothConnection(context!!).sendCommand("stop") }









    }



}
