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
import kotlinx.android.synthetic.main.fragment_control.view.*


@Suppress("DEPRECATION")
class ControlFragment : Fragment() {
    var prevPixel = -16908288


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_control, container, false)
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


    }

    /**
     * Funktion: handelTouch
     * Input: MotionEvent
     * Output: -
     * Beschreibung: Fkt um Touch Position und Action am DPAD zu bestimmen
     */
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

            when (action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    if (x in 0..800 && y in 0..800) {
                        val pixel = bitmap.getPixel(x.toInt(), y.toInt())
                        if (pixel != prevPixel) {
                            actionString = "DOWN"
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && changeImg(pixel)) {
                                vibratorService.vibrate(VibrationEffect.createOneShot(100, -1))
                            }
                            prevPixel = pixel
                        }

                    }
                }

                MotionEvent.ACTION_UP -> {
                    actionString = "UP"
                    view!!.dpad.setImageResource(R.drawable.dpad_normal)
                    vibratorService.cancel()
                    BluetoothConnection(context!!).sendCommand("stop")
                    prevPixel = -16908288

                }
            }

        }


    }

    /**
     * Funktion: handelTouch
     * Input: Pixelfarbe
     * Output: Boolean
     * Beschreibung: DPad - Überlagerung von Maske und DPad - abhängig von der Farbe (Touchposition) wird das Bild demensprechend gewechselt
     */
    fun changeImg(pixel: Int): Boolean {
        var red = -16908288
        var yellow = -16843264
        var green = -33489408
        var blue = -33554178
        var magenta = -16908034
        var pressed = true
        when (pixel) {
            //center
            red -> {
                view!!.dpad.setImageResource(R.drawable.dpad_center)
                BluetoothConnection(context!!).sendCommand("stop")
            }
            //up
            yellow -> {
                view!!.dpad.setImageResource(R.drawable.dpad_up)
                BluetoothConnection(context!!).sendCommand("up")
            }
            //left
            green -> {
                view!!.dpad.setImageResource(R.drawable.dpad_left)
                BluetoothConnection(context!!).sendCommand("left")
            }
            //down
            blue -> {
                view!!.dpad.setImageResource(R.drawable.dpad_down)
                BluetoothConnection(context!!).sendCommand("down")
            }
            //right
            magenta -> {
                view!!.dpad.setImageResource(R.drawable.dpad_right)
                BluetoothConnection(context!!).sendCommand("right")
            }
            else -> pressed = false

        }

        return pressed


    }
}



