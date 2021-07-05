package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseViewHolder
import androidx.databinding.ViewDataBinding


open class GroupVH<T, GD : GroupData<*>>(binding: ViewDataBinding) :
    BaseViewHolder<T>(binding) {

    var groupData: GD? = null

}