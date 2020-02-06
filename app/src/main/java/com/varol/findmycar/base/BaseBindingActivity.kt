package com.varol.findmycar.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.varol.findmycar.internal.util.functional.lazyThreadSafetyNone
import com.varol.findmycar.BR
import org.koin.androidx.viewmodel.ext.android.viewModelByClass
import kotlin.reflect.KClass


abstract class BaseActivity<out VM : ViewModel, DB : ViewDataBinding>(viewModelClass: KClass<VM>) :
    AppCompatActivity() {

    //no need for ViewModelProviders
    private val viewModel: VM by viewModelByClass(viewModelClass)

    abstract val layoutRes: Int

    lateinit var binding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewModel, viewModel)

    }

}