package ca.qc.cstj.tpsynthese.ui.loading

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.ui.network.list.ListNetworkUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoadingViewModel(application: Application) : AndroidViewModel(application) {
    private val _loadingUiState = MutableStateFlow<LoadingUiState>(LoadingUiState.Empty)
    val loadingUiState = _loadingUiState.asStateFlow()

    var timerCounter = 0

    private val timer = object: CountDownTimer(Constants.Delay.LOADING_DELAY, Constants.Delay.LOADING_DELAY/10) {
        override fun onTick(millisUntilFinished: Long) {
            timerCounter++
            _loadingUiState.update {
                LoadingUiState.Working(timerCounter)
            }
        }

        override fun onFinish() {
            cancel()
            _loadingUiState.update {
                LoadingUiState.Finished
            }
        }
    }.start()

}