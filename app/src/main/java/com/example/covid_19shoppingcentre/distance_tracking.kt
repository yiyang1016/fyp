package com.example.covid_19shoppingcentre


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_distance_tracking.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class distance_tracking : AppCompatActivity() {

    private val REQUEST_BLUETOOTH_TURN_ON = 1
    private val BLE_SCAN_PERIOD: Long = 100000
    private lateinit var bleAdapter: BluetoothAdapter
    private lateinit var bleManager: BluetoothManager
    private lateinit var bleScanner: BluetoothLeScanner
    private lateinit var bleScanCallback: BleScanCallback
    private var bleScanResults = mutableMapOf<String?, BluetoothDevice?>()
    private lateinit var bleScanHandler: Handler
    var count = 0
    var resumeCount = 0
    //    var deviceList = findViewById<EditText>(R.id.showDevice)
   // private lateinit var database:     DatabaseReference
    private var database = FirebaseDatabase.getInstance().getReference("Member")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_distance_tracking)

        setActionBar()

        bleScanHandler = Handler()
        bleManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bleAdapter = bleManager.adapter
        if (!bleAdapter.isEnabled) {
            val bluetoothTurnOn = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(bluetoothTurnOn, REQUEST_BLUETOOTH_TURN_ON)
        }
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Toast.makeText(
                    this,
                    "The permission to get BLE location data is required",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), 1
                )
            }
        } else {
            //Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show()
        }

        //val getVisible = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        //startActivityForResult(getVisible, 0)

        bleImageButton.setOnClickListener { view ->
            //check Bluetooth status every 3 seconds
            if(count == 0){
                if(resumeCount == 1){
                    onResume()
                    resumeCount==0
                }
                val handler = Handler()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        //Call your function here
                        if (!bleAdapter.isEnabled) {
                            showNotification(
                                "Please Enable Bluetooth",
                                "Kindly Enable the Bluetooth inside your phone"
                            )
                        }
                            handler.postDelayed(this, 3000)//3 sec delay
                    }
                }, 0)

                status.text = "STATUS : ON"
                bleStartScan.run()
                //bleScanHandler.postDelayed(bleStopScan, this.BLE_SCAN_PERIOD)
                count++
                val bluetoothScanninghandler = Handler()
                bluetoothScanninghandler.postDelayed(object : Runnable {
                    override fun run() {
                        //Call your function here
                        println(bleScanCallback.addRssi.map { "bleScanCallback.addRSSi" + it.key.toString() + '-' + it.value.toString() })
                        if (bleScanCallback.addRssi.isNotEmpty()) {
                            for ((key, value) in bleScanCallback.addRssi) {
                                val rssi = value
                                //compareValues(rssi, -73) < 0
                                if (rssi > -63 && rssi <= 0) {
                                    marksDeduct(5, rssi)
                                    showNotification(
                                        "Close Distance",
                                        "Please Keep Your Social Distance More than 1.5 Meters."
                                    )
                                }else if (rssi < -69 && rssi > -72) {
                                    marksDeduct(3, rssi)
                                    showNotification(
                                        "Close Distance",
                                        "Please Keep Your Social Distance More than 1.5 Meters."
                                    )
                                }else if (rssi < -73 && rssi > -100) {
                                    marksDeduct(2, rssi)
                                    showNotification(
                                        "Close Distance",
                                        "Please Keep Your Social Distance More than 1.5 Meters."
                                    )
                                }
                            }

                            bleScanCallback.addRssi.clear()
                        }
                        bluetoothScanninghandler.postDelayed(this, 10000)//10 sec delay
                    }
                }, 0)

                /*secondCount++
                if(secondCount == 1){
                    onPause()
                }else if(secondCount == 2){
                    onResume()
                    secondCount = 0
                }*/
            }else if(count == 1 ){
                onPause()
                //secondCount++
            }


            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()

        }
    }

    //Start Scan nearby bluetooth
    private val bleStartScan = Runnable {
        bleScanner = bleAdapter.bluetoothLeScanner
        bleScanCallback = BleScanCallback(bleScanResults as HashMap<String?, BluetoothDevice?>)
        bleScanCallback.setContext(this.applicationContext)
        bleScanner.startScan(bleScanCallback)
        Toast.makeText(this.applicationContext, "Start Scan the Nearby Device", Toast.LENGTH_SHORT)
            .show()
        //bleScanHandler.postDelayed(bleStopScan, this.BLE_SCAN_PERIOD)
    }

    private val bleStopScan = Runnable {
        if (bleScanner != null) {
            bleScanner.stopScan(bleScanCallback)
            
            //println(bleScanCallback.addRssi.map { "bleScanCallback.addRSSi" + it.key.toString() + '-' + it.value.toString()})
            bleScanResults.clear()
            bleScanCallback.resultOfScan.clear()
            bleScanCallback.addRssi.clear()
            //bleStartScan.run()
        }
        Toast.makeText(this.applicationContext, " BLE Scanning Ended", Toast.LENGTH_SHORT).show()
    }


    class BleScanCallback(resultMap: HashMap<String?, BluetoothDevice?>) : ScanCallback() {
        var resultOfScan = resultMap
        private var context: Context? = null
        var deviceNameAddress: HashMap<String?, String?> = HashMap<String?, String?>()
        var addRssi: HashMap<String, Int> = HashMap<String, Int>()

        fun setContext(context: Context) {
            this.context = context
        }

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            results?.forEach { result -> addScanResult(result) }
        }

        override fun onScanFailed(errorCode: Int) {
            Toast.makeText(
                this.context,
                "BLE Failed to scan" + "Error Code: " + errorCode,
                Toast.LENGTH_SHORT
            ).show()
        }

        fun addScanResult(scanResult: ScanResult) {
            val bleDevice = scanResult.device
            val deviceAddress = bleDevice.address
            val rssiValue = scanResult.rssi
            if (!resultOfScan.contains(deviceAddress)) {
                resultOfScan.put(deviceAddress, bleDevice)
                if (this.context != null) {
                    Toast.makeText(
                        this.context,
                        bleDevice?.name + ":" + bleDevice?.address + "RSSI" + rssiValue,
                        Toast.LENGTH_SHORT
                    ).show()
                    //deviceNameAddress.put(bleDevice?.name, deviceAddress)
                    addRssi.put(deviceAddress, rssiValue)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_BLUETOOTH_TURN_ON -> {
                when (requestCode) {
                    RESULT_OK -> {
                        Toast.makeText(
                            this.applicationContext,
                            "Bluetooth Activated",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    RESULT_CANCELED -> {
                        Toast.makeText(
                            this.applicationContext,
                            "Bluetooth Failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    fun showNotification(title: String, message: String) {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "COVID-19 Shopping Centre",
                "Bluetooth Disable",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Bluetooth are disabled by user"
            mNotificationManager.createNotificationChannel(channel)
        }
        val fullScreenIntent = Intent(this, distance_tracking::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, 0,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val mBuilder = NotificationCompat.Builder(applicationContext, "COVID-19 Shopping Centre")
            .setSmallIcon(R.mipmap.ic_launcher_covid_round) // notification icon
            .setContentTitle(title) // title for notification
            .setContentText(message)// message for notification
            .setAutoCancel(true) // clear notification after click
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setFullScreenIntent(fullScreenPendingIntent, true)
        mNotificationManager.notify(0, mBuilder.build())
    }

    fun marksDeduct(marks: Int, rssiValue: Int) {
        val id = intent.getStringExtra("MemberID")
        val ref = FirebaseDatabase.getInstance().getReference("SocialDistanceScore")
        var scoreId = ""
        val refSearch =
            FirebaseDatabase.getInstance().getReference("SocialDistanceScore").orderByKey()
                .limitToLast(1)


        //Create new score history data
        refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                val text = "Connection Failed"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (p0 in p0.children) {
                        val getScoreId = p0.getValue(Score::class.java)
                        scoreId = getScoreId?.Score_Id.toString()
                    }

                    val cal = ((scoreId.substring(1, 6)).toInt()) + 1
                    val num = 100000 + cal
                    val newId = "S" + num.toString().substring(1, 6)

                    val currentDateTime1 = LocalDateTime.now()
                    val dateFormat1: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                    val dateText1 = currentDateTime1.format(dateFormat1)
                    val data = SocialDistanceScore(
                        newId,
                        "Close Contact",
                        marks,
                        dateText1, id, rssiValue
                    )

                    ref.child(newId).setValue(data).addOnSuccessListener {
                        //Toast.makeText(applicationContext, "Added Successfully", Toast.LENGTH_SHORT)
                        //    .show()
                    }

                    val refSearch = FirebaseDatabase.getInstance().getReference().child("Member")
                        .orderByChild("Id").equalTo(id)
                    refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            val text = "Connection Failed"
                            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                for (p0 in p0.children) {
                                    val current =
                                        Integer.parseInt(p0.child("CurrentScore").value.toString()) - marks
                                    if (current == 70 || current == 50 || current == 30) {
                                        showNotification(
                                            "Warning Message",
                                            "Social Distance Mark Low than $current. Please keep social distance before get bar!"
                                        )
                                    }
                                    database.child(id).child("CurrentScore").setValue(current)
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "CurrentScore Missing from the database",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    })

                }

            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = intent.getStringExtra("MemberID")

        val intent1 = Intent(this, MainActivity::class.java).apply {
            putExtra("MemberID", id)
        }
        startActivity(intent1)

        //Handle action bar item clicks here.
        //The action bar will automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManfest.xml
        when (item.itemId){
            R.id.historyBtn -> {
                val intent = Intent(this, social_distance_score_history::class.java).apply {
                    putExtra("MemberID", id)
                }

                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onPause() {
        super.onPause()
        count = 0
        status.text = "STATUS : OFF"
        bleStopScan
        resumeCount++
        //bleScanHandler.postDelayed(bleStopScan, this.BLE_SCAN_PERIOD)
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Distance Tracking"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

    }
}
