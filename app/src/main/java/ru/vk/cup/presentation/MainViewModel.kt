package ru.vk.cup.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import ru.vk.cup.NavigationType
import ru.vk.cup.data.InteractiveItemRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = InteractiveItemRepository(application)

    fun getItems(navigationType: NavigationType) = Pager(
        pagingSourceFactory = { InteractiveItemDataSource(repository, navigationType) },
        config = PagingConfig(pageSize = 10, enablePlaceholders = false)
    ).flow.cachedIn(viewModelScope)
}