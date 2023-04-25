package ca.qc.cstj.tpsynthese.ui.loading

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import ca.qc.cstj.tenretni.core.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingViewModel(application: Application) : AndroidViewModel(application) {
    private val _loadingUiState = MutableStateFlow<LoadingUiState>(LoadingUiState.Empty)
    val loadingUiState = _loadingUiState.asStateFlow()

    private var _timerCounter = 0

    private val timer = object: CountDownTimer(Constants.Delay.LOADING_DELAY, Constants.Delay.LOADING_DELAY/10) {
        override fun onTick(millisUntilFinished: Long) {
            _timerCounter++
            _loadingUiState.update {
                LoadingUiState.Working(_timerCounter)
            }
        }

        override fun onFinish() {
            cancel()
            _loadingUiState.update {
                LoadingUiState.Finished
            }
        }
    }

    fun startTimer() {
        _timerCounter = 0
        timer.start()
    }
}