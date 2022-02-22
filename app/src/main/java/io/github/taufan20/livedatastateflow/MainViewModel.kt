package io.github.taufan20.livedatastateflow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn

class MainViewModel : ViewModel() {

    private var _liveDataMessage: MutableLiveData<String> = MutableLiveData<String>()
    val liveDataMessage: LiveData<String> = _liveDataMessage

    private var _liveDataToStateFlow: MutableLiveData<String> = MutableLiveData<String>()

    private var _stateFlowMessage: MutableStateFlow<String> = MutableStateFlow("StateFlow")
    val stateFlowMessage: StateFlow<String> = _stateFlowMessage

    fun hitLiveData() {
        _liveDataMessage.value = "Hello LiveData"
    }

    fun hitStateFlow() {
        _stateFlowMessage.value = "Hello StateFlow"
    }

    fun stateFlowToLiveData(): LiveData<String> {
        _stateFlowMessage.value = "StateFlow change to LiveData"
        return _stateFlowMessage.asLiveData(viewModelScope.coroutineContext)
    }

    fun changeLiveDataToStateFlow(): StateFlow<String> {

        return _liveDataToStateFlow.asFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "LiveData change to StateFlow"
        )
    }

}