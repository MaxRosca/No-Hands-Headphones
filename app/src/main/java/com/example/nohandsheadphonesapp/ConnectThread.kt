
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import java.io.IOException
import java.util.*

class ConnectThread(private val mmSocket: BluetoothSocket?, val handler: Handler, val textV: TextView) : Thread() {
    val TAG = "ConnectThreadTAG"
    var MY_UUID = UUID.fromString("0000112f-0000-1000-8000-00805f9b34fb")

    override fun run() {
        try {
            Log.i(TAG, "Connected to socket ! ")
            var inputStream = mmSocket?.inputStream
            var outputStream = mmSocket?.outputStream
            val mmBuffer: ByteArray = ByteArray(1024)
            var numBytes: Int

            while (true) {
                Log.v("sfas", "gi")

                try {
                    numBytes = inputStream!!.read(mmBuffer)
//                    Log.v(TAG, "HI")
                } catch (e: IOException) {
                    Log.v(TAG, "Input stream was disconnected", e)
                    break
                }

                handler.post {
                    val s = String(mmBuffer, 0, numBytes)

                    textV.text = s
                }
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
