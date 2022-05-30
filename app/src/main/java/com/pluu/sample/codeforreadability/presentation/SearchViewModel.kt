package com.pluu.sample.codeforreadability.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pluu.sample.codeforreadability.data.SavingRepository
import com.pluu.sample.codeforreadability.model.GenerateItem
import com.pluu.sample.codeforreadability.model.SampleItem
import com.pluu.sample.codeforreadability.provider.GenerateItemGenerator
import com.pluu.sample.codeforreadability.provider.provideRepository
import kotlinx.coroutines.launch
import logcat.logcat

class SearchViewModel(
    private val generator: GenerateItemGenerator,
    private val savingRepository: SavingRepository
) : ViewModel() {

    private val logRepository by lazy {
        provideRepository()
    }

    private val _items = MutableLiveData<List<SampleItem>>()
    val items: LiveData<List<SampleItem>> get() = _items

    private val _messageEvent = MutableLiveData<String>()
    val messageEvent: LiveData<String> get() = _messageEvent

    private val cachedList = mutableListOf<SampleItem>()

    fun generate() {
        val item = generator.generate()

        if (cachedList.none { it.text == item.text }) {
            val favoriteText = savingRepository.getFavorite()

            cachedList.add(
                item.toUiModel(
                    isFavorite = item.text == favoriteText,
                    onFavorite = ::updateFavorite
                )
            )
            _items.value = cachedList.sortedBy { it.text }
        } else {
            _messageEvent.value = "Duplicate item : ${item.text}"
        }
    }

    fun reset() {
        sendLog()
        cachedList.clear()
        _items.value = emptyList()
    }

    private fun sendLog() {
        // Use OkHttp, Retrofit
        viewModelScope.launch {
            // Case 1.
            val result = logRepository.sendLog()
            logcat { result.toString() }

            // Case 2.
//            logRepository.sendLogFlow()
//                .collect { result ->
//                    logcat { result.toString() }
//                }
        }
    }

    private fun updateFavorite(text: String) {
        savingRepository.saveFavorite(text)
        val snapshot = cachedList.map {
            it.copy(isFavorite = it.text == text)
        }
        cachedList.clear()
        cachedList.addAll(snapshot)
        _items.value = cachedList.sortedBy { it.text }
    }
}

private fun GenerateItem.toUiModel(
    isFavorite: Boolean,
    onFavorite: (String) -> Unit
): SampleItem = SampleItem(
    text = text,
    bgColor = bgColor,
    isFavorite = isFavorite,
    onFavorite = onFavorite
)