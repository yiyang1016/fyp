package com.example.covid_19shoppingcentre


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_distance_tracking.*


class distance_tracking : AppCompatActivity() {

    private val REQUEST_BLUETOOTH_TURN_ON = 1
    private val BLE_SCAN_PERIOD: Long  = 10000
    private lateinit var bleAdapter: BluetoothAdapter
    private lateinit var bleManager: BluetoothManager
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var bleScanCallback: BleScanCallback
    private var bleScanResults = mutableMapOf<String?, BluetoothDevice?>()
    private lateinit var bleScanHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distance_tracking)

        bleImageButton.setOnClickListener{view->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        bleScanHandler = Handler()
        bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter
        if(!bleAdapter.isEnabled){
            val bluetoothTurnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(bluetoothTurnOn, REQUEST_BLUETOOTH_TURN_ON)
        }else {
            bleStartScan.run()
        }
    }

    //Start Scan nearby bluetooth
    private val bleStartScan = Runnable {
        bleScanner = bleAdapter.bluetoothLeScanner
        bleScanCallback = BleScanCallback(bleScanResults as HashMap<String?, BluetoothDevice?>)
        bleScanCallback.setContext(this.applicationContext)
        bleScanner.startScan(bleScanCallback)
        Toast.makeText(this.applicationContext, "Start Scan Nearby Device", Toast.LENGTH_SHORT).show()
        bleScanHandler.postDelayed(bleStopScan, this.BLE_SCAN_PERIOD)
    }

    private val bleStopScan = Runnable{
        if(bleScanner !=null){
            bleScanner.stopScan(bleScanCallback)
        }
        Toast.makeText(this.applicationContext, "Bluetooth Activity End", Toast.LENGTH_SHORT).show()
    }


    class BleScanCallback(resultMap: HashMap<String?, BluetoothDevice?>) : ScanCallback(){
        var resultOfScan = resultMap
        private var context: Context?= null

        fun setContext(context: Context){
            this.context = context
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            results?.forEach{result -> addScanResult(result)}
        }

        override fun onScanFailed(errorCode: Int) {
            Toast.makeText(this.context, "BLE Failed to scan" + "Error Code: "+ errorCode, Toast.LENGTH_SHORT).show()
        }

        fun addScanResult(scanResult: ScanResult?){
            val bleDevice = scanResult?.device
            val deviceAddress = bleDevice?.address
            val rssiValue = scanResult?.rssi
            if(!resultOfScan.contains(deviceAddress)){
                resultOfScan.put(deviceAddress, bleDevice)
                if(this.context !=null){
                    Toast.makeText(this.context, bleDevice?.name + ":" + bleDevice?.address + rssiValue, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_BLUETOOTH_TURN_ON->{
                when(requestCode){
                    RESULT_OK-> {
                        Toast.makeText(this.applicationContext, "Bluetooth Activated", Toast.LENGTH_SHORT).show()
                    }
                    RESULT_CANCELED ->{
                        Toast.makeText(this.applicationContext, "Bluetooth Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Handle action bar item clicks here.
        //The action bar will automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManfest.xml
        return when (item.itemId){
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
