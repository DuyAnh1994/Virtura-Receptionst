package ai.ftech.dev.base.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder<T>(val binding : ViewDataBinding) : RecyclerView.ViewHolder(binding.root){

    open fun onBind(data: T){ }

    open fun onBind(data: T, payloads: MutableList<Any>){

    }
}