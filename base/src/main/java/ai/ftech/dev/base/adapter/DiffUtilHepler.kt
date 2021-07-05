package ai.ftech.dev.base.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

open class BaseDiffUtilItemCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.javaClass.declaredFields.contentEquals(newItem.javaClass.declaredFields)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.javaClass === newItem.javaClass
    }
}

open class BaseDiffUtilCallback<T : Any>(private val oldData: List<T>? = null,
                                         private val newData: List<T>? = null
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newData?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData?.get(oldItemPosition)?.javaClass?.declaredFields.contentEquals(newData?.get(newItemPosition)?.javaClass?.declaredFields)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData?.get(oldItemPosition) == newData?.get(newItemPosition)
    }
}