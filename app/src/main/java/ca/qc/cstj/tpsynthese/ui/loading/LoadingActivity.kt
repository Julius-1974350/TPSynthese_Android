package ca.qc.cstj.tpsynthese.ui.loading

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ca.qc.cstj.tpsynthese.databinding.ActivityLoadingBinding
import ca.qc.cstj.tpsynthese.ui.MainActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LoadingActivity : AppCompatActivity() {

    private val viewModel : LoadingViewModel by viewModels()
    private lateinit var binding : ActivityLoadingBinding
    private var flag = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(flag) {
            binding = ActivityLoadingBinding.inflate(layoutInflater)
            setContentView(binding.root)
            viewModel.startTimer()
            show()
        }
    }

    override fun onResume(){
        super.onResume()
        show()
    }
    private fun show(){
        viewModel.loadingUiState.onEach {
            when (it) {
                LoadingUiState.Empty -> Unit
                LoadingUiState.Finished -> {
                    startActivity(MainActivity.newIntent(this))
                    finish()
                }

                is LoadingUiState.Working -> {
                    binding.countdown.text = it.progression.toString()
                    binding.pgbLoading.setProgress(it.progression, true)
                }
            }
        }.launchIn(lifecycleScope)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        flag = false
        onResume()
    }
}