package kr.ac.beni.beniprj.ui.custom

import android.content.Context
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.DialogUpdateBinding

class CustomDialog(parentContext: Context,private var title:String,private var message:String, private var textOk:String, private var listener: OnItemClickListener?) :
    BaseDialog<DialogUpdateBinding>(parentContext) {

    override val layoutId: Int get() = R.layout.dialog_update

    override fun setUIEventListener() {

        mBinding?.textTitle?.text = title
        mBinding?.textContent?.text = message
        mBinding?.textOk?.text = textOk
        mBinding?.textOk?.setOnClickListener {
            listener?.onItemClick(CONFIRM)
            dismiss()
        }
    }

    override fun initView() {
        setCancelable(false)
    }

}