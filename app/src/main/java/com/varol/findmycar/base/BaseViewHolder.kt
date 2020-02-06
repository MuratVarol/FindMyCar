package com.varol.findmycar.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(val itemBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    abstract fun bindData(position: Int)
}