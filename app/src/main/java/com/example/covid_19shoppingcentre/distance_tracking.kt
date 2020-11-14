package com.example.covid_19shoppingcentre


import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker
import androidx.core.os.postDelayed
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
//    var deviceList = findViewById<EditText>(R.id.showDevice)
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distance_tracking)

        //read database to get device name and address
        readDatabase()
        bleScanHandler = Handler()
        bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter
        if(!bleAdapter.isEnabled){
            val bluetoothTurnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(bluetoothTurnOn, REQUEST_BLUETOOTH_TURN_ON)
        }

        //check Bluetooth status every 3 seconds
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                //Call your function here
                if(!bleAdapter.isEnabled){
                    val bluetoothTurnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(bluetoothTurnOn, REQUEST_BLUETOOTH_TURN_ON)
                }
                handler.postDelayed(this, 3000)//1 sec delay
            }
        }, 0)

        bleImageButton.setOnClickListener{view->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            bleStartScan.run()
            //Update local Bluetooth name
            database.child("Member").child("M00004").child("Bluetooth_Name").setValue(bleAdapter.name)
            database.child("Member").child("M00004").child("Bluetooth_Address").setValue(bleAdapter.address)
            readDatabase()
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
        distance.text = bleScanCallback.resultOfScan.toString()
    }

    private val bleStopScan = Runnable{
        if(bleScanner !=null){
            bleScanner.stopScan(bleScanCallback)
            bleScanResults.clear()
            //bleScanCallback.resultOfScan.clear()
        }
        Toast.makeText(this.applicationContext, " BLE Scanning Ended", Toast.LENGTH_SHORT).show()
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
                if(this.context !=null ){
                    Toast.makeText(this.context, bleDevice?.name + ":" + bleDevice?.address +"RSSI"+ rssiValue, Toast.LENGTH_SHORT).show()
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

    fun readDatabase(){
        val refSearch = FirebaseDatabase.getInstance().getReference("Member").orderByChild("Id").equalTo("M00004")
        database = Firebase.database.reference
        refSearch.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                val text = "Connection Failed"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (p0 in p0.children) {
                        textView3.setText("Bluetooth Name: " + p0.child("Bluetooth_Name").value.toString() +"\nBluetooth Address: "+ p0.child("Bluetooth_Address").value.toString())

                    }
                }
            }
        })
    }


}

