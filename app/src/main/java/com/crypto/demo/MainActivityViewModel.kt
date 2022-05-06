package com.crypto.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivityViewModel(private val currencyDao: ICurrencyInfoDao) : ViewModel() {
    private val DEBOUNCE_DELAY: Long = 300L
    private var debounceJob: Job? = null

    private val uiStateFlow = MutableStateFlow(
        MainActivityUIState(
            currencyInfoList = emptyList(),
            sortAscending = null,
            sortDrawableId = null
        )
    )
    val uiState: StateFlow<MainActivityUIState> = uiStateFlow.asStateFlow()

    suspend fun fetchCurrencies() {
        currencyDao.getAll().collect {
            val newState = uiStateFlow.value.copy(currencyInfoList = it)
            uiStateFlow.value = newState
        }
    }

    fun toggleSorting() {
        val isAscending = uiStateFlow.value.sortAscending?.not() ?: true
        val drawableId = getSortDrawable(isAscending)
        val newState =
            uiStateFlow.value.copy(sortAscending = isAscending, sortDrawableId = drawableId)
        uiStateFlow.value = newState

        delayAction {
            val list =
                if (isAscending) {
                    uiStateFlow.value.currencyInfoList.sortedBy { it.name }
                } else {
                    uiStateFlow.value.currencyInfoList.sortedByDescending { it.name }
                }
            val sortedState = uiStateFlow.value.copy(currencyInfoList = list)
            uiStateFlow.value = sortedState
        }
    }

    private fun delayAction(action: (() -> Unit)?) {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(DEBOUNCE_DELAY)
            action?.invoke()
        }
    }

    private fun getSortDrawable(isAscending: Boolean): Int =
        if (isAscending) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
}

class MainActivityViewModelFactory(private val currencyDao: ICurrencyInfoDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(currencyDao) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}