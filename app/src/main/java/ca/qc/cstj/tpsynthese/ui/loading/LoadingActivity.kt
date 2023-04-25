package ca.qc.cstj.tpsynthese.ui.loading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import ca.qc.cstj.tpsynthese.R
import ca.qc.cstj.tpsynthese.databinding.ActivityLoadingBinding
import ca.qc.cstj.tpsynthese.ui.MainActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

class LoadingActivity : AppCompatActivity() {

    private val viewModel : LoadingViewModel by viewModels()
    private lateinit var binding : ActivityLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.startTimer()
        viewModel.loadingUiState.onEach {
            when(it){
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
}