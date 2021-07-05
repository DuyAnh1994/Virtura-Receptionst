package ai.ftech.dev.base.adapter.group

import ai.ftech.dev.base.adapter.BaseViewHolder
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GroupManager(private val adapter: RecyclerView.Adapter<*>) {

    private val dataSet = mutableListOf<GroupData<*>>()

    fun addRawGroupDataAtIndex(index: Int, data: GroupData<*>) {
        data.groupManager = this
        dataSet.add(index, data)
    }

    fun addGroupData(data: GroupData<*>) {
        data.groupManager = this
        dataSet.add(data)
        if (data.getCount() == 0) {
            data.detach()
        } else if (!data.isAttached()) {
            data.attach()
        }
    }

    fun addGroupDataAtIndex(index: Int, data: GroupData<*>) {
        data.groupManager = this
        dataSet.add(index, data)
        if (data.getCount() == 0) {
            data.detach()
        } else if (!data.isAttached()) {
            data.attach()
            shiftAdapterPosition(data, data.getCount())
        }
    }

    fun removeGroup(groupData: GroupData<*>) {
        if (dataSet.contains(groupData)) {
            shiftAdapterPosition(groupData, -groupData.getCount())
            groupData.detach()
            dataSet.remove(groupData)
            adapter.notifyItemRangeRemoved(groupData.adapterPosition, groupData.getCount())
        }
    }

    fun removeGroupWithoutNotify(groupData: GroupData<*>) {
        if (dataSet.contains(groupData)) {
            shiftAdapterPosition(groupData, -groupData.getCount())
            groupData.detach()
            dataSet.remove(groupData)
        }
    }

    fun getDataSet(): List<GroupData<*>?>? {
        return dataSet
    }

    fun getItemCount(): Int {
        var total = 0
        for (data in dataSet) {
            if (data.isAttached()) {
                total += data.getCount()
            }
        }
        return total
    }

    fun getLayoutResource(viewType: Int): Int {
        for (data in dataSet) {
            val layoutResource: Int = data.getLayoutResource(viewType)
            if (layoutResource != GroupData.INVALID_RESOURCE) {
                return layoutResource
            }
        }
        throw IllegalArgumentException("Can not find layout for type: $viewType")
    }

    fun getItemViewType(adapterPosition: Int): Int {
        val data: GroupData<*>? = findGroupDataByAdapterPosition(adapterPosition)
        return data?.getItemViewType(adapterPosition - data.adapterPosition)
            ?: throw IllegalArgumentException("Can not find data for position: $adapterPosition")
    }

    fun onCreateVH(itemViewBinding: ViewDataBinding, viewType: Int): BaseViewHolder<*> {
        for (data in dataSet) {
            val vh: BaseViewHolder<*>? = data.onCreateVH(itemViewBinding, viewType)
            if (vh != null) {
                return vh
            }
        }
        throw IllegalArgumentException("Can not find ViewHolder for type: $viewType")
    }

    fun onBindViewHolder(vh: GroupVH<Any, GroupData<*>>, position: Int) {
        vh.groupData = this.findGroupDataByAdapterPosition(position)
        vh.onBind(getItemDataAtAdapterPosition(position))
    }

    fun onBindViewHolder(vh: GroupVH<Any, GroupData<*>>, position: Int, payloads: MutableList<Any>) {
        vh.groupData = findGroupDataByAdapterPosition(position)
        vh.onBind(getItemDataAtAdapterPosition(position), payloads)
    }

    fun getItemDataAtAdapterPosition(adapterPosition: Int): Any {
        val data: GroupData<*>? = findGroupDataByAdapterPosition(adapterPosition)
        return data?.getDataInGroup(adapterPosition - data.adapterPosition)
            ?: throw IllegalArgumentException("Can not find data for position: $adapterPosition")
    }

    fun notifyRemove(group: GroupData<*>, adapterPosition: Int) {
        shiftAdapterPosition(group, -1)
        adapter.notifyItemRemoved(adapterPosition)
        checkToDetach(group)
    }

    fun notifyRemove(group: GroupData<*>, adapterPosition: Int, count: Int) {
        shiftAdapterPosition(group, -count)
        adapter.notifyItemRangeRemoved(adapterPosition, count)
        checkToDetach(group)
    }

    fun notifyInserted(group: GroupData<*>, adapterPosition: Int, count: Int) {
        shiftAdapterPosition(group, count)
        adapter.notifyItemRangeInserted(adapterPosition, count)
    }

    fun notifyChanged(group: GroupData<*>, adapterPosition: Int) {
        adapter.notifyItemChanged(adapterPosition)
    }

    fun notifyChanged(group: GroupData<*>, adapterPosition: Int, payload: Any?) {
        adapter.notifyItemChanged(adapterPosition, payload)
    }

    fun notifyChanged(group: GroupData<*>) {
        adapter.notifyItemRangeChanged(group.adapterPosition, group.getCount())
    }

    fun notifyChanged(group: GroupData<*>, payload: Any?) {
        adapter.notifyItemRangeChanged(group.adapterPosition, group.getCount(), payload)
    }

    fun notifyNewGroupAdded(group: GroupData<*>) {
        if (!group.isAttached()) {
            group.attach()
            shiftAdapterPosition(group, group.getCount())
        }
        adapter.notifyItemRangeInserted(group.adapterPosition, group.getCount())
    }

    fun notifyDataSetChanged() {
        for (group in dataSet) {
            if (group.isAttached() && group.getCount() <= 0) {
                group.detach()
            } else {
                if (group.getCount() > 0) {
                    group.attach()
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    fun findGroupDataByAdapterPosition(adapterPosition: Int): GroupData<*>? {
        for (data in dataSet) {
            if (data.isAttached()) {
                if (data.adapterPosition <= adapterPosition && adapterPosition < data.adapterPosition + data.getCount()) {
                    return data
                }
            }
        }
        return null
    }

    fun findAdapterPositionForGroup(group: GroupData<*>?): Int {
        val index: Int = dataSet.indexOf(group)
        return when {
            index > 0 -> {
                for (i in index - 1 downTo 0) {
                    val prev: GroupData<*> = dataSet.get(i)
                    if (prev.isAttached()) {
                        return prev.adapterPosition + prev.getCount()
                    }
                }
                0
            }
            index == 0 -> {
                0
            }
            else -> {
                throw IllegalArgumentException("The GroupData is not added")
            }
        }
    }

    fun getIndexOfGroupData(groupData: GroupData<*>?): Int {
        return if (groupData == null) {
            -1
        } else dataSet.indexOf(groupData)
    }

    fun clear() {
        val itr: MutableIterator<GroupData<*>> = dataSet.iterator()
        while (itr.hasNext()) {
            val next: GroupData<*> = itr.next()
            shiftAdapterPosition(next, -next.getCount())
            next.detach()
            itr.remove()
        }
        adapter.notifyDataSetChanged()
    }

    private fun shiftAdapterPosition(startGroup: GroupData<*>, count: Int) {
        val startIndex: Int = dataSet.indexOf(startGroup)
        for (i in startIndex + 1 until dataSet.size) {
            val next: GroupData<*> = dataSet[i]
            if (next.isAttached()) {
                next.adapterPosition += count
            }
        }
    }

    private fun checkToDetach(group: GroupData<*>) {
        if (group.getCount() == 0) {
            group.detach()
        }
    }
}