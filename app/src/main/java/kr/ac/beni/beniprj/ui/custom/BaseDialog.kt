package kr.ac.beni.beniprj.ui.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.ac.beni.beniprj.R

abstract class BaseDialog<B : ViewDataBinding?>(parentContext: Context) :
    Dialog(parentContext, R.style.DialogTheme) {

    companion object{
        const val CONFIRM = 0
        const val CANCEL = -1
    }

    var mBinding: B? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    protected abstract val layoutId: Int
    protected abstract fun initView()
    protected abstract fun setUIEventListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false)
        mBinding?.root?.let { setContentView(it) }
        initView()
        setUIEventListener()
    }

    init {
        val window = window
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        layoutParams.dimAmount = 0.5f
        if (window != null) window.attributes = layoutParams
    }
}
