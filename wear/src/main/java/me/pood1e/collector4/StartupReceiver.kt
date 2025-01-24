package me.pood1e.collector4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking

class StartupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        WorkManager.getInstance(context).enqueue(
            OneTimeWorkRequestBuilder<RegisterForPassiveDataWorker>().build()
        )
    }
}

class RegisterForPassiveDataWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        runBlocking {
            triggerService(appContext)
        }
        return Result.success()
    }
}