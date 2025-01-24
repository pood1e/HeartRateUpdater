package me.pood1e.collector4

import android.os.SystemClock
import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.DataPointContainer
import androidx.health.services.client.data.DataType
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.runBlocking
import java.time.Instant


class PassiveDataService : PassiveListenerService() {
    private val dataClient by lazy { Wearable.getDataClient(this) }
    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        Log.i("aaa", "onNewDataPointsReceived: one batch received")
        val bootInstant =
            Instant.ofEpochMilli(System.currentTimeMillis() - SystemClock.elapsedRealtime())
        runBlocking {
            dataPoints.getData(DataType.HEART_RATE_BPM).forEach {
                val instant = bootInstant.plus(it.timeDurationFromBoot)
                Log.i("aaa", "onNewDataPointsReceived: $instant --- ${it.value}")
            }
            dataPoints.getData(DataType.HEART_RATE_BPM).last().let {
                trySend(it.value.toInt())
            }
        }
    }

    private fun trySend(heartRate: Int) {
        val request = PutDataMapRequest.create("/heartrate")
            .apply { dataMap.putInt("heartrate", heartRate) }
            .asPutDataRequest()
            .setUrgent()
        dataClient.putDataItem(request).addOnSuccessListener {
            Log.i("aaa", "send success: $it")
        }.addOnFailureListener {
            Log.i("aaa", "send failure: $it")
        }
    }
}