package com.capstime.timecapsule.domain

import android.app.ActionBar
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewbinding.ViewBinding
import com.capstime.timecapsule.presentation.base_fragment.BaseFragment
import com.capstime.timecapsule.presentation.base_fragment.BaseObserveViewModelFragment


/*AbsctractPictureFragment.kt
* 16.01.2024 Globa Lev*/

abstract class AbsctractPictureFragment<B : ViewBinding> : BaseObserveViewModelFragment<B>() {

    private val sdk = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    private lateinit var windowInsetsController: WindowInsetsControllerCompat
    private var actionBar: ActionBar? = null
    private lateinit var window: Window
    private lateinit var decorView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().run {
            this@AbsctractPictureFragment.actionBar = actionBar
            this@AbsctractPictureFragment.window = window
            this@AbsctractPictureFragment.decorView = window.decorView
        }
    }
}


/*InterfaceRecyclerViewInside.kt
* 18.01.2024 Globa Lev*/
interface InterfaceRecyclerViewInside {
    fun initRecyclerView()

}

/*InterfaceRecyclerViewScrollChange.kt
* 18.01.2024 Globa Lev*/
interface InterfaceRecyclerViewScrollChange {
    fun scrollChange()
}

/*InterfaceRecyclerViewItemDecoration.kt
* 18.01.2024 Globa Lev*/
interface InterfaceRecyclerViewItemDecoration {
    val outRectFirstAndLastPosition: Int
        get() = 8

    val outRectPosition: Int
        get() = 8

    fun callItemDecoration()
}

/*InterfaceRecyclerViewEvents.kt
* 18.01.2024 Globa Lev*/

interface InterfaceRecyclerViewEvents {
    fun analyzeEvents(event: MotionEvent, scale: Float): Boolean
}

/*InterfaceRecuclerViewSnapHelper.kt
* 18.01.2024 Globa Lev*/
interface InterfaceRecuclerViewSnapHelper {
    val snapHelper: SnapHelper
        get() = PagerSnapHelper()
}

/*InterfaceScaleAndMoveRecyclerViewInsideFragment.kt
* 18.01.2024 Globa Lev*/
interface InterfaceScaleAndMoveRecyclerViewInsideFragment : InterfaceRecyclerViewInside,
    InterfaceRecyclerViewEvents, InterfaceRecyclerViewItemDecoration,
    InterfaceRecyclerViewScrollChange, InterfaceRecuclerViewSnapHelper


/*AbstractScaleAndMoveRecyclerViewInsideFragment.kt
* 18.01.2024 Globa Lev*/

abstract class AbstractScaleAndMoveRecyclerViewInsideFragment<B : ViewBinding> :
    AbsctractPictureFragment<B>(), InterfaceScaleAndMoveRecyclerViewInsideFragment {

    protected var recyclerViewBinding: RecyclerView? = null

    override fun callItemDecoration() {
        recyclerViewBinding?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {

                super.getItemOffsets(outRect, view, parent, state)

                when (parent.getChildAdapterPosition(view)) {
                    0 -> outRect.run {
                        left = outRectFirstAndLastPosition
                        right = outRectPosition
                    }

                    state.itemCount - 1 ->
                        outRect.run {
                            left = outRectPosition
                            right = outRectFirstAndLastPosition

                        }

                    else ->
                        outRect.run {
                            left = outRectPosition
                            right = outRectPosition
                        }
                }
            }
        })
    }

    override fun analyzeEvents(event: MotionEvent, scale: Float): Boolean =
        when (recyclerViewBinding?.scrollState) {
            RecyclerView.SCROLL_STATE_IDLE -> {
                event.pointerCount > 1 || scale != 1f

            }

            RecyclerView.SCROLL_STATE_DRAGGING -> false

            else -> false
        }
}

