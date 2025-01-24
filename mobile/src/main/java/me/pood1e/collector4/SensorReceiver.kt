package me.pood1e.collector4

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.api.http.HttpRequest
import com.apollographql.apollo.api.http.HttpResponse
import com.apollographql.apollo.network.http.HttpInterceptor
import com.apollographql.apollo.network.http.HttpInterceptorChain
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.runBlocking
import me.pood1e.collector4.type.ChangeUserStatusInput

class SensorReceiver : WearableListenerService() {
    private val token = ""

    private val interceptor = object : HttpInterceptor {
        override suspend fun intercept(
            request: HttpRequest,
            chain: HttpInterceptorChain
        ): HttpResponse {
            return chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer $token").build()
            )
        }
    }

    private val apolloClient = ApolloClient.Builder()
        .serverUrl("https://api.github.com/graphql")
        .addHttpInterceptor(interceptor)
        .build()

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.forEach { event ->
            when (event.type) {
                DataEvent.TYPE_CHANGED -> {
                    event.dataItem.run {
                        if (uri.path?.compareTo("/heartrate") == 0) {
                            val heartRate = DataMapItem.fromDataItem(this)
                                .dataMap.getInt("heartrate")
                            Log.i("hr", "$heartRate")
                            if (heartRate != 0) {
                                runBlocking {
                                    val response = apolloClient.mutation(
                                        ChangeUserStatusMutation(
                                            ChangeUserStatusInput(
                                                message = Optional.present("$heartRate"),
                                                emoji = Optional.present(":heart:")
                                            )
                                        )
                                    ).execute()
                                    Log.i("response", "${response.data}")
                                }
                            }
                        }
                    }
                }

                DataEvent.TYPE_DELETED -> {
                    // DataItem deleted
                }
            }
        }
    }
}