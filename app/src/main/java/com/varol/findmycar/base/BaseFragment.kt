package com.varol.findmycar.base

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.varol.findmycar.BR
import com.varol.findmycar.R
import com.varol.findmycar.internal.extension.EMPTY
import com.varol.findmycar.internal.extension.observeNonNull
import com.varol.findmycar.internal.extension.showPopup
import com.varol.findmycar.internal.extension.showToast
import com.varol.findmycar.internal.navigation.NavigationCommand
import com.varol.findmycar.internal.popup.PopUpType
import com.varol.findmycar.internal.popup.PopupUiModel
import com.varol.findmycar.internal.util.Failure
import com.varol.findmycar.internal.view.informbar.InformBar
import com.varol.findmycar.internal.view.informbar.InformBarModel
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import kotlin.reflect.KClass


abstract class BaseFragment<out VM : BaseAndroidViewModel, DB : ViewDataBinding>(viewModelClass: KClass<VM>) :
    Fragment() {

    //no need for ViewModelProviders
    open val viewModel: VM by viewModelByClass(viewModelClass)

    lateinit var binding: DB

    abstract val getLayoutId: Int

    open fun initialize() { }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId, container, false)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)
        initialize()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeNavigation()
        observeFailure()
        observeInform()
    }

    private fun observeNavigation() {
        viewModel.navigation.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { command ->
                handleNavigation(command)
            }
        })
    }

    protected open fun handleNavigation(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.To -> {
                with(command) {
                    directions.let {
                        findNavController().navigate(it, getExtras())
                    }
                }
            }
            is NavigationCommand.Popup -> {
                with(command) {
                    showPopup(model, callback)
                }
            }
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }

    private fun observeFailure() {
        viewModel.failure.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { failure -> handleFailure(failure) }
        }
    }

    private fun observeInform() {
        viewModel.inform.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { informBarModel ->
                showInformBarMessage(informBarModel)
            }
        }
    }

    fun startInstalledAppDetailsActivity() {
        val startSettingsIntent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            addCategory(Intent.CATEGORY_DEFAULT)
            data = Uri.parse("package: ${context?.applicationContext?.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        activity?.startActivity(startSettingsIntent)
    }

    protected fun handleFailure(failure: Failure) {
        val (title, message) = when (failure) {
            is Failure.NoConnectivityError ->
                Pair(String.EMPTY, getString(R.string.error_message_network_connection))
            is Failure.UnknownHostError ->
                Pair(String.EMPTY, getString(R.string.error_message_unknown_host))
            is Failure.ServerError ->
                Pair(String.EMPTY, failure.message)
            is Failure.ParsingDataError,
            is Failure.EmptyResponse ->
                Pair(String.EMPTY, getString(R.string.error_message_invalid_response))
            is Failure.UnknownError ->
                Pair(String.EMPTY, failure.message ?: getString(R.string.error_unknown))
            is Failure.HttpError ->
                Pair(String.EMPTY, getString(R.string.error_message_http, failure.code.toString()))
            is Failure.TimeOutError ->
                Pair(String.EMPTY, getString(R.string.error_message_timeout))
            else ->
                Pair(String.EMPTY, failure.message ?: failure.toString())
        }

        showPopup(
            PopupUiModel(
                title = title,
                message = message,
                popUpType = PopUpType.Error
            ), null
        )
    }

    private fun showInformBarMessage(informBarModel: InformBarModel) {
        view?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                with(informBarModel) {
                    InformBar.make(it, message, informType, duration).show()
                }
            } else {
                context.showToast(informBarModel.message)
            }
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("InlinedApi")
    fun isLocationEnabled(): Boolean {
        context?.let { ctx ->
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val lm =
                    ctx.getSystemService(LOCATION_SERVICE) as LocationManager
                lm.isLocationEnabled
            } else { // This is Deprecated in API 28
                val mode = Settings.Secure.getInt(
                    ctx.contentResolver, Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF
                )
                mode != Settings.Secure.LOCATION_MODE_OFF
            }
        }
        return false
    }

    open fun getExtras(): FragmentNavigator.Extras = FragmentNavigatorExtras()

}