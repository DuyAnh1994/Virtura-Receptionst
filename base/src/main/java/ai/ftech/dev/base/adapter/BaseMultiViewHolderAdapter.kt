package ai.ftech.dev.base.adapter

import ai.ftech.dev.base.BR
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseMultiViewHolderAdapter<T : BaseMultiViewHolderAdapter.BaseModelType>(
    @LayoutRes private val resLayout: List<Int>,
    diffUtil: BaseDiffUtilItemCallback<T>
) : ListAdapter<T, BaseViewHolder<*>>(diffUtil) {

    companion object {
        const val VIEW_TYPE_DEFAULT = 0
    }

    /**
     * Binding view
     */
    private lateinit var binding: ViewDataBinding

    /**
     * Inflate layout root
     */
    private lateinit var inflater: LayoutInflater

    /**
     * Listener action in item of list
     */
    var listener: BaseItemListener? = null

    /**
     * Listener load more
     */
    var onLoadMore: () -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        binding = DataBindingUtil.inflate(inflater, resLayout[viewType], parent, false)
        binding.apply {
            setVariable(BR.itemListener, listener)
            // context in list
            val context = root.context as LifecycleOwner
            lifecycleOwner = context
            executePendingBindings()
        }
        return BaseViewHolder<Any>(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        holder.binding.apply {
            setVariable(BR.item, getItem(holder.adapterPosition))
            setVariable(BR.itemPosition, holder.adapterPosition)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        // Load more
        val layoutManager = recyclerView.layoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (layoutManager as LinearLayoutManager).let { layout ->
                    if (!currentList.toMutableList().isNullOrEmpty()) {
                        val lastCompleteShowPosition = layout.findLastVisibleItemPosition()
                        if (lastCompleteShowPosition == itemCount - 1 && dy > 0) {
                            onLoadMore.invoke()
                        }
                    }
                }
            }
        })
    }

    /**
     * Must extend this class if use multi type view holder for list
     */
    abstract class BaseModelType(open val viewType: Int = VIEW_TYPE_DEFAULT)
}
