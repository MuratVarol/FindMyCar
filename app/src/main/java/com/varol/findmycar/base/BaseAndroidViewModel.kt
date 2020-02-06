package com.varol.findmycar.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.varol.findmycar.internal.navigation.NavigationCommand
import com.varol.findmycar.internal.popup.PopupCallback
import com.varol.findmycar.internal.popup.PopupUiModel
import com.varol.findmycar.internal.util.Event
import com.varol.findmycar.internal.util.Failure
import com.varol.findmycar.internal.view.informbar.InformBarModel
import com.varol.findmycar.internal.view.informbar.InformType
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

abstract class BaseAndroidViewModel : AndroidViewModel {

    protected val context: Context

    private val isProgressVisible = MutableLiveData<Boolean>()

    constructor(context: Context) : super(context.applicationContext as Application) {
        this.context = context.applicationContext as Application
    }

    private val _failure = MutableLiveData<Event<Failure>>()
    var failure = _failure

    private val _success = MutableLiveData<Event<String>>()
    var success = _success

    private val _inform = MutableLiveData<Event<InformBarModel>>()
    var inform = _inform

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> = _navigation

    fun navigate(directions: NavDirections) {
        _navigation.value = Event(NavigationCommand.To(directions))
    }

    fun navigate(model: PopupUiModel, callback: PopupCallback?) {
        _navigation.value = Event(NavigationCommand.Popup(model, callback))
    }

    fun navigateBack() {
        _navigation.value = Event(NavigationCommand.Back)
    }

    fun showProgress() {
        isProgressVisible.postValue(true)
    }

    fun hideProgress() {
        isProgressVisible.postValue(false)
    }

    val isLoading = isProgressVisible

    protected open fun handleFailure(failure: Failure) {
        hideProgress()
        this._failure.value = Event(failure)
    }

    fun showInformBar(message: String) {
        _inform.value = Event(InformBarModel(message))
    }

    fun showErrorBar(message: String) {
        _inform.value = Event(InformBarModel(message, informType = InformType.Error))
    }

    protected fun getBackgroundScheduler(): Scheduler = Schedulers.io()

    protected fun getMainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()

    protected fun startCountdown(period: Long, timeUnit: TimeUnit): Observable<Long> {
        return Observable.interval(period, timeUnit)
            .subscribeOn(getBackgroundScheduler())
            .observeOn(getBackgroundScheduler())
    }

    protected fun startTimer(period: Long, timeUnit: TimeUnit, repeat: Long = 1): Observable<Long> {
        return Observable.timer(period, timeUnit)
            .repeat(repeat)
            .subscribeOn(getBackgroundScheduler())
            .observeOn(getBackgroundScheduler())
    }

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}
