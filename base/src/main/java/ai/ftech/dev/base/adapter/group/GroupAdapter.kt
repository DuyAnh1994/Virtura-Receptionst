package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GroupAdapter : RecyclerView.Adapter<BaseViewHolder<*>>() {

    protected val groupManager: GroupManager = GroupManager(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutId = groupManager.getLayoutResource(viewType)

        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutId, parent, false)

        return groupManager.onCreateVH(binding, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is GroupVH<*, *>) {
            groupManager.onBindViewHolder(holder as GroupVH<Any, GroupData<*>>, position)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<*>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (holder is GroupVH<*, *>) {
            groupManager.onBindViewHolder(holder as GroupVH<Any, GroupData<*>>, position, payloads)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return groupManager.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return groupManager.getItemCount()
    }

    fun notifyAllGroupChanged() {
        groupManager.notifyDataSetChanged()
    }

    fun addGroup(data: GroupData<*>) {
        groupManager.addGroupData(data)
    }

    fun addGroupDataAtIndex(index: Int, data: GroupData<*>) {
        groupManager.addGroupDataAtIndex(index, data)
    }

    fun removeGroup(groupData: GroupData<*>?) {
        if (groupData != null) {
            groupManager.removeGroup(groupData)
        }
    }

    fun removeGroupWithoutNotify(groupData: GroupData<*>?) {
        if (groupData != null) {
            groupManager.removeGroupWithoutNotify(groupData)
        }
    }

    fun clear() {
        groupManager.clear()
    }


    fun getPositionInGroup(adapterPosition: Int): Int {
        val group: GroupData<*>? = groupManager.findGroupDataByAdapterPosition(adapterPosition)
        return if (group != null) {
            adapterPosition - group.adapterPosition
        } else -1
    }

    fun getIndexOfGroup(groupData: GroupData<*>): Int {
        return groupManager.getIndexOfGroupData(groupData)
    }

    fun addRawGroupAtIndex(index: Int, groupData: GroupData<*>) {
        groupManager.addRawGroupDataAtIndex(index, groupData)
    }
}