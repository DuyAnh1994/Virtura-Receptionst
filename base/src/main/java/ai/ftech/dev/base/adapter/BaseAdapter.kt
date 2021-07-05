package ai.ftech.dev.base.adapter

import ai.ftech.dev.base.BR
import ai.ftech.dev.base.common.GetLayoutId
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T : Any> : RecyclerView.Adapter<BaseViewHolder<*>>(), DiffUtilListener<T> {

    /**
     * Binding view
     */
    private lateinit var binding: ViewDataBinding

    /**
     * Inflate layout root
     */
    private lateinit var inflater: LayoutInflater

    /**
     * List items
     */
    var data: MutableList<T>? = null
        private set

    /**
     * Listener action in item of list
     */
    var listener: BaseItemListener? = null

    /**
     * Listener load more
     */
    var onLoadMore: () -> Unit = {}

    /**
     * Annotation
     */
    private var layoutIdAnnotation: GetLayoutId? = null

    init {
        val annotations = this::class.java.declaredAnnotations
        annotations.forEach {
            when (it) {
                is GetLayoutId -> {
                    layoutIdAnnotation = it
                }
            }
        }
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        layoutIdAnnotation?.let {
            binding = DataBindingUtil.inflate(inflater, it.layoutId, parent, false)
            binding.apply {
                setVariable(BR.itemListener, listener)
                // context in list
                val context = root.context as LifecycleOwner
                lifecycleOwner = context
                executePendingBindings()
            }
        }
        return BaseViewHolder<Any>(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int){
        holder.binding.apply {
            setVariable(BR.item, data?.get(holder.adapterPosition))
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
                    if (!data.isNullOrEmpty()) {
                        val lastCompleteShowPosition = layout.findLastVisibleItemPosition()
                        if (lastCompleteShowPosition == itemCount - 1 && dy > 0) {
                            onLoadMore.invoke()
                        }
                    }
                }
            }
        })
    }

    override fun submitList(newData: MutableList<T>?) {
        val new = newData?.toMutableList()
        val callback = BaseDiffUtilCallback(data, new)
        val diffResult = DiffUtil.calculateDiff(callback)
        this.data = new
        diffResult.dispatchUpdatesTo(this)
        /*diffResult.dispatchUpdatesTo(object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                notifyItemRangeInserted(position, count - position)
            }

            override fun onRemoved(position: Int, count: Int) {
                notifyItemRangeRemoved(position, count - position)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                notifyItemRangeChanged(position, count - position, payload)
            }
        })*/
    }
}
