package ai.ftech.dev.base.common

import android.view.View

interface BaseView {

    /**
     * Setup data binding
     */
    fun onInitBinding()

    /**
     * Call before create view
     */
    fun onPrepareInitView() {}

    /**
     * Call after finish create view
     */
    fun onInitView()

    /**
     * Call after init view , observer data changed
     */
    fun observerViewModel()

    /**
     * Handle click all view in screen
     *
     * @param view
     */
    fun onViewClick(view: View)
}