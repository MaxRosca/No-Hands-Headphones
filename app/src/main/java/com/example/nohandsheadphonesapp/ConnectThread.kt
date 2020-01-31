
import android.bluetooth.BluetoothSocket
import android.bluetooth.BluetoothDevice
import java.util.UUID.randomUUID
import android.util.Log
import java.io.IOException
import java.util.*

class ConnectThread(private val mmDevice: BluetoothDevice?) : Thread() {
    private val mmSocket: BluetoothSocket?
    var MY_UUID = UUID.fromString("0000112f-0000-1000-8000-00805f9b34fb")

    init {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        var tmp: BluetoothSocket? = null

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = mmDevice?.createInsecureRfcommSocketToServiceRecord(MY_UUID)
        } catch (e: IOException) {

            Log.d("CONNECTTHREAD", "Could not close connection:" + e.toString())
        }

        mmSocket = tmp
    }

    override fun run() {
        // Cancel discovery because it will slow down the connection
        //mBluetoothAdapter.cancelDiscovery();
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket!!.connect()
            var inputStream = mmSocket.inputStream
            var outputStream = mmSocket.outputStream
            val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream
            var numBytes: Int // bytes returned from read()

            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    inputStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.v("Appinfo", "Input stream was disconnected", e)
                    break
                }
                Log.v("Appinfo"," measa" + numBytes.toString())


            }

        } catch (connectException: IOException) {
            Log.d("CONNECTTHREAD", "Could not close connection:" + connectException.toString())
            // Unable to connect; close the socket and get out
            try {
                mmSocket!!.close()
            } catch (closeException: IOException) {
                Log.d("CONNECTTHREAD", "Could not close connection:" + closeException.toString())
            }

            return
        }

        // Do work to manage the connection (in a separate thread)
        // manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket  */
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
        }

    }
}
