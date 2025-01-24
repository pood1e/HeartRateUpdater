package me.pood1e.collector4

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.clearPassiveListenerService
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import androidx.health.services.client.getCapabilities
import androidx.health.services.client.setPassiveListenerService

suspend fun triggerService(context: Context) {
    val healthClient = HealthServices.getClient(context)
    val passiveMonitoringClient = healthClient.passiveMonitoringClient
    val capabilities = passiveMonitoringClient.getCapabilities()
    val supportHeartRate =
        DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
    if (supportHeartRate) {
        val passiveListenerConfig = PassiveListenerConfig.builder()
            .setDataTypes(setOf(DataType.HEART_RATE_BPM))
            .build()
        passiveMonitoringClient.clearPassiveListenerService()
        passiveMonitoringClient.setPassiveListenerService(
            PassiveDataService::class.java,
            passiveListenerConfig
        )
    }
}