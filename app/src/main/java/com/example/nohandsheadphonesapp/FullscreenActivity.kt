package com.example.nohandsheadphonesapp

import ConnectThread
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.util.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)
        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {

            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                // Device doesn't support Bluetooth
            }

//            val getUuidsMethod: Method = BluetoothAdapter
//
//            val uuids = getUuidsMethod.invoke(bluetoothAdapter, null) as Array<ParcelUuid>
//
//            val id = uuids[1].uuid
//

            if (bluetoothAdapter?.isEnabled == false) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                var REQUEST_ENABLE_BT = 2
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            }

            var myDevice: BluetoothDevice? = null

            val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                Log.v("Appinfo", device.name + " " + device.address + " " + device.type.toString())

                if (device.name == "Galaxy J5 2017"){
                    myDevice = device
                    Log.v("Appinfo", myDevice?.name)
                }
            }

//            myDevice?.uuids
//
//            val tManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            val uuid = tManager.deviceId

            val MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

            val connThread = ConnectThread(myDevice)

//            val mySocket = myDevice?.createInsecureRfcommSocketToServiceRecord(MY_UUID)

            try {

//                var mySocket = myDevice.getMethod("createRfcommSocket", arrayOf<Class<*>?>(Int::class.javaPrimitiveType)).invoke(myDevice, 1) as BluetoothSocket
//                mySocket?.connect()

//                var inputStream = mySocket?.inputStream
//                var outputStream = mySocket?.outputStream
//
//                var bts: ByteArray = ByteArray(1024)
//                inputStream?.read(bts)
                connThread.run()
            }
            catch (e: IOException){
                Log.d("Appinfo", e.toString())

            }

        }
    }
}
