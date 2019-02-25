package `in`.heis.drivysteuerung.htlinn


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_control_man.*
import kotlinx.android.synthetic.main.fragment_control_man.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */


@Suppress("DEPRECATION")
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

        view.seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                BluetoothConnection(context!!).sendCommand("speed: " + progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}


            override fun onStopTrackingTouch(seekBar: SeekBar) {}


        })

        view.dpad.setOnTouchListener { v: View, m: MotionEvent ->
            handleTouch(m)
            true
        }
        //BluetoothConnection(context!!).execute()

        button_ctr_man_up.setOnClickListener { BluetoothConnection(context!!).sendCommand("up") }
        button_ctr_man_down.setOnClickListener { BluetoothConnection(context!!).sendCommand("down") }
        button_ctr_man_left.setOnClickListener { BluetoothConnection(context!!).sendCommand("left") }
        button_ctr_man_right.setOnClickListener { BluetoothConnection(context!!).sendCommand("right") }
        button_ctr_man_stop.setOnClickListener {
            //BluetoothConnection(context!!).sendCommand("stop")
            Toast.makeText(context!!, "f", Toast.LENGTH_LONG).show()
        }
    }

    fun handleTouch(m: MotionEvent) {
        val pointerCount = m.pointerCount
        view!!.dpad_mask.isDrawingCacheEnabled = true
        view!!.dpad_mask.buildDrawingCache()
        val bitmap = view!!.dpad_mask.drawingCache


        for (i in 0 until pointerCount) {
            val x = m.getX(i)
            val y = m.getY(i)

            val id = m.getPointerId(i)
            val action = m.actionMasked
            val actionIndex = m.actionIndex
            var actionString: String
            val vibratorService = context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            button_ctr_man_stop.text = bitmap.width.toString() + "   " + bitmap.height.toString()
            if (x in 0..800 && y in 0..800) {
                val pixel = bitmap.getPixel(x.toInt(), y.toInt())
                when (action) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                        actionString = "DOWN"
                        var changed = changeImg(pixel)
                        button_ctr_man_left.text = x.toString()
                        button_ctr_man_right.text = y.toString()
                        if (action == MotionEvent.ACTION_DOWN) {
                            //vibratorService.vibrate(10000000)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibratorService.vibrate(VibrationEffect.createOneShot(1000000, -1))
                            }
                            //vibration = true
                            //HWE: 1. vco 2. phasenlagenvergleich 3. TP 4. f-Herunterteiler 5: referenzsignlal

                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        actionString = "UP"
                        view!!.dpad.setImageResource(R.drawable.dpad_normal)
                        vibratorService.cancel()


                    }
                }
            }

        }
    }

    fun changeImg(pixel: Int): Boolean {
        var red = -16908288
        var yellow = -16843264
        var green = -33489408
        var blue = -33554178
        var magenta = -16908034
        var changed = false
        when (pixel) {
            //center
            red -> {
                //if(view!!.dpad.)
                view!!.dpad.setImageResource(R.drawable.dpad_center)
                //view!!.dpad_center.visibility = View.VISIBLE
            }
            //up
            yellow -> {
                view!!.dpad.setImageResource(R.drawable.dpad_up)
            }
            //left
            green -> {
                view!!.dpad.setImageResource(R.drawable.dpad_left)
            }
            //down
            blue -> {
                view!!.dpad.setImageResource(R.drawable.dpad_down)
            }
            //right
            magenta -> {
                view!!.dpad.setImageResource(R.drawable.dpad_right)
            }

        }

        return changed


    }
}



