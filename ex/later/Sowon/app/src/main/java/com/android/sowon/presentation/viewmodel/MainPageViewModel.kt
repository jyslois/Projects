package com.android.sowon.presentation.viewmodel

import android.view.MenuItem
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class MainPageViewModel @Inject constructor(

): ViewModel() {

    // 클릭 이벤트 감지
    // 바톰 네비게이션
    private val _bottomNavigation = MutableSharedFlow<MenuItem>()
    val bottomNavigation = _bottomNavigation.asSharedFlow()

    suspend fun clickBottomNavigationItem(item: MenuItem) {
        _bottomNavigation.emit(item)
    }

}