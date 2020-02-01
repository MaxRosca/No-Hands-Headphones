package com.example.nohandsheadphonesapp

import ConnectThread
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*


class FullscreenActivity : AppCompatActivity() {

    val TAG = "MainActivityTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        window.decorView.systemUiVisibility = SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                SYSTEM_UI_FLAG_FULLSCREEN or SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val connectButton: Button = findViewById(R.id.connectButton)
        val sendButton: Button = findViewById(R.id.sendButton)
        val sendMessageEditText = findViewById<EditText>(R.id.sendMessage)
        val recievedText = findViewById<TextView>(R.id.recievedMessages)
        var connected = false
        var connThread: ConnectThread?
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        val mainHandler = Handler()

        connectButton.setOnClickListener {

            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 2)
            }

            // Huawei - 5C:A8:6A:89:1E:70
            // J5 - BC:54:51:07:62:C5
            // Note 9 - F4:C2:48:E7:63:B6

            var myDevice: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice("F4:C2:48:E7:63:B6")

            Log.i(TAG, myDevice?.name)
//            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
//            pairedDevices?.forEach { device ->
//                Log.v(TAG, device.name + " " + device.address)
//
//                if (device.name == "Galaxy J5 2017"){
//                    myDevice = device
////                    Log.v(TAG, myDevice?.name)
//                }
//            }

            try {
                var MY_UUID = UUID.fromString("0000112f-0000-1000-8000-00805f9b34fb")

                try {
                    var mySocket = myDevice?.createInsecureRfcommSocketToServiceRecord(MY_UUID)

                    if(mySocket?.isConnected != true) {
                        mySocket!!.connect()
                    }

                    inputStream = mySocket.inputStream
                    outputStream = mySocket.outputStream
                    connThread = ConnectThread(mySocket, mainHandler, recievedText)
                    connThread!!.start()

                } catch (e: IOException) {
                    Log.d(TAG, "Could not close connection:" + e.toString())
                }
            }
            catch (e: Exception){
                Log.d(TAG, e.toString())

            }

        }

        sendButton.setOnClickListener {
            if(connected){
                outputStream?.write(sendMessageEditText.text.toString().toByteArray())
            }
        }
    }
}
