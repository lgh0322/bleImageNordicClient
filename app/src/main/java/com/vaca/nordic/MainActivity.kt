package com.vaca.nordic

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.vaca.ble.nordic.BleDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    val dataScope = CoroutineScope(Dispatchers.IO)
    val scan = BleScanManager()
    val worker=BleDataWorker()
    var isConnect=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        scan.initScan(application)
        scan.start()
        scan.setCallBack(object : BleScanManager.Scan {
            override fun scanReturn(name: String, bluetoothDevice: BluetoothDevice) {
                Log.e("sdkjfh",name)
                scan.stop()
                if(!isConnect){
                    isConnect=true
                    worker.initWorker(application,bluetoothDevice)
                    dataScope.launch {
                        worker.waitConnect()
                        Log.e("士大夫立刻","大师傅立刻就圣诞快乐")
                    }

                }
            }
        })
    }


}