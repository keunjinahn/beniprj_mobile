package kr.ac.beni.beniprj.ui.custom

import android.content.Context
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.DialogCustomYesOrNoBinding

class CustomYesOrNoDialog(parentContext: Context, private var title:String, private var message:String, private var textCancel:String,
                          private  var textOk:String, private var listener: OnItemYesOrNoClickListener?) :
    BaseDialog<DialogCustomYesOrNoBinding>(parentContext) {

    override val layoutId: Int get() = R.layout.dialog_custom_yes_or_no

    interface OnItemYesOrNoClickListener{
        fun onItemClick(flag:Int)
    }

    override fun setUIEventListener() {
        mBinding?.textTitle?.text = title
        mBinding?.textContent?.text = message
        mBinding?.textCancel?.text = textCancel
        mBinding?.textOk?.text = textOk

        mBinding?.textCancel?.setOnClickListener {
            listener?.onItemClick(CANCEL)
            dismiss()
        }
        mBinding?.textOk?.setOnClickListener {
            listener?.onItemClick(CONFIRM)
            dismiss()
        }
    }

    override fun initView() {
        setCancelable(false)
    }

}