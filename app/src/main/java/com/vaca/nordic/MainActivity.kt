package com.vaca.nordic

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import com.vaca.ble.nordic.BleDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer

//YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
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
                        val bitmap=getBitmap(this@MainActivity, R.drawable.da)
                        val refImg=byteProcess(bitmap2Byte(bitmap))
                        worker.sendCmd(refImg)
//                        Log.e("撒旦立刻解放就",refImg.size.toString())
//                        val dd=refImg.size
//                        val gg=if(dd %200 ==0){
//                            dd/200
//                        }else{
//                            dd/200+1
//                        }
//                        for(k in 0 until gg){
//                            worker.sendCmd(getSegment(refImg,k))
//                        }



                    }

                }
            }
        })
    }
    fun getSegment(b: ByteArray,index:Int):ByteArray{
        val a1=index*200
        val a2=index*200+200
        return if(a2<=b.size){
            b.copyOfRange(a1,a2)
        }else{
            b.copyOfRange(a1,b.size)
        }
    }


    fun byteProcess(b: ByteArray):ByteArray{
        val len=b.size/4
        val a=ByteArray(len*3){
            0.toByte()
        }
        for(k in 0 until len){
            a[k*3]=b[k*4]
            a[k*3+1]=b[k*4+1]
            a[k*3+2]=b[k*4+2]
        }
        return a
    }

    fun bitmap2Byte(bitmap: Bitmap):ByteArray{
        val size = bitmap.rowBytes * bitmap.height
        val byteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        return byteBuffer.array()
    }
    fun getBitmap(context: Context, resId: Int): Bitmap {
        val options = BitmapFactory.Options()
        val value = TypedValue()
        context.resources.openRawResource(resId, value)
        options.inTargetDensity = value.density
        options.inPreferredConfig= Bitmap.Config.ARGB_8888
        options.inScaled = false //不缩放
        return BitmapFactory.decodeResource(context.resources, resId, options)
    }


}