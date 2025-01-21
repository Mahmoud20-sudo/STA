package sa.sauditourism.employee.components

import androidx.lifecycle.DefaultLifecycleObserver
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {
    var currentPosition: Long = 0
    var isPlaying = false
    var currentPage = 0

    override fun onSwipeToRefresh() {
        //TODO onSwipeToRefresh
    }
}
