package `in`.heis.drivysteuerung.htlinn

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Toast

class CreateAlertdialog(val context: Context, val message:String, val titel:String?){



    fun custom():AlertDialog.Builder {

        val builder = AlertDialog.Builder(context)
            .setTitle(titel)
            .setMessage(message)

            .setPositiveButton("Ja") { dialog, which ->


            }

            .setNegativeButton("Nein") { dialog, which ->

            }

        return builder


    }

    fun info() {

        val builder = AlertDialog.Builder(context)
            .setTitle("Info")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->

            }
            .show()


    }
    fun error() {

        val builder = AlertDialog.Builder(context)
            .setTitle("Fehler")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->

            }
            .show()


    }




}