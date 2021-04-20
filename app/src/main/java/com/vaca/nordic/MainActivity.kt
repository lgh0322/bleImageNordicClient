package com.vaca.nordic

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    val scan = BleScanManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan.initScan(application)
        scan.start()
        scan.setCallBack(object : BleScanManager.Scan {
            override fun scanReturn(name: String, bluetoothDevice: BluetoothDevice) {
                Log.e("sdkjfh",name)
            }
        })
    }


}