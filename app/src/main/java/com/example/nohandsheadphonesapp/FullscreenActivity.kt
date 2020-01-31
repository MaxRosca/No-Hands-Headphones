package com.example.nohandsheadphonesapp

import ConnectThread
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class FullscreenActivity : AppCompatActivity() {

    val TAG = "MainActivityTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {

            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 2)
            }

            var myDevice: BluetoothDevice? = null

            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
//                Log.v(TAG, device.name + " " + device.address)

                if (device.name == "Galaxy J5 2017"){
                    myDevice = device
//                    Log.v(TAG, myDevice?.name)
                }
            }

            val connThread = ConnectThread(myDevice)

            try {
                connThread.run()
            }
            catch (e: IOException){
                Log.d(TAG, e.toString())

            }

        }
    }
}
