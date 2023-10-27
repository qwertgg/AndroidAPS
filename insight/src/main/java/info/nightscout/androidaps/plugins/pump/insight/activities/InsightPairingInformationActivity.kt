package info.nightscout.androidaps.plugins.pump.insight.activities

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import dagger.android.support.DaggerAppCompatActivity
import info.nightscout.androidaps.insight.databinding.ActivityInsightPairingInformationBinding
import info.nightscout.androidaps.plugins.pump.insight.connection_service.InsightConnectionService

class InsightPairingInformationActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityInsightPairingInformationBinding

    private var connectionService: InsightConnectionService? = null

    private val serviceConnection: ServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            connectionService = (binder as InsightConnectionService.LocalBinder).service
            connectionService?.let {
                if (!it.isPaired) {
                    overridePendingTransition(0, 0)
                    finish()
                    startActivity(Intent(this@InsightPairingInformationActivity, InsightPairingActivity::class.java))
                } else {
                    binding.serialNumber.text = it.pumpSystemIdentification?.serialNumber
                    binding.manufacturingDate.text = it.pumpSystemIdentification?.manufacturingDate
                    binding.systemIdAppendix.text = it.pumpSystemIdentification?.systemIdAppendix.toString()
                    binding.releaseSwVersion.text = it.pumpFirmwareVersions?.releaseSWVersion
                    binding.uiProcSwVersion.text = it.pumpFirmwareVersions?.uiProcSWVersion
                    binding.pcProcSwVersion.text = it.pumpFirmwareVersions?.pcProcSWVersion
                    binding.mdTelSwVersion.text = it.pumpFirmwareVersions?.mdTelProcSWVersion
                    binding.safetyProcSwVersion.text = it.pumpFirmwareVersions?.safetyProcSWVersion
                    binding.btInfoPageVersion.text = it.pumpFirmwareVersions?.btInfoPageVersion
                    binding.bluetoothAddress.text = it.bluetoothAddress
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            connectionService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsightPairingInformationBinding.inflate(layoutInflater)
        binding.deletePairing.setOnClickListener {
            connectionService?. run {
                reset()
                finish()
            }
        }
        setContentView(binding.root)
        bindService(Intent(this, InsightConnectionService::class.java), serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}