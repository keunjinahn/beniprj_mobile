package kr.ac.beni.beniprj.ui.custom

import android.content.Context
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.DialogUpdateBinding

class UpdateDialog(parentContext: Context, private var listener: OnItemClickListener?) :
    BaseDialog<DialogUpdateBinding>(parentContext) {

    override val layoutId: Int get() = R.layout.dialog_update


    override fun setUIEventListener() {
        mBinding?.textOk?.setOnClickListener {
            listener?.onItemClick(CONFIRM)
            dismiss()
        }
    }

    override fun initView() {
        setCancelable(false)
    }

}