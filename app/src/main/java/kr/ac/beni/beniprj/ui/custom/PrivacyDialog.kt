package kr.ac.beni.beniprj.ui.custom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.DialogPrivacyBinding

class PrivacyDialog(private var parentContext: Context, private var listener: OnItemClickListener?) : BaseDialog<DialogPrivacyBinding>(parentContext) {
    private var isChecked = false
    private var isChecked2 = false
    override val layoutId: Int get() = R.layout.dialog_privacy

    interface OnItemClickListener{
        fun onClick(flag:Int, isChecked:Boolean)
    }

    override fun setUIEventListener() {
        mBinding?.textCancel?.setOnClickListener {
            listener?.onClick(CANCEL,isChecked)
            dismiss()
        }

        mBinding?.textOk?.setOnClickListener {
            if(isChecked && isChecked2){
                listener?.onClick(CONFIRM,true)
                dismiss()
            }else{
                Toast.makeText(context,context.getString(R.string.privacy_no_check),Toast.LENGTH_SHORT).show()
            }
        }
        mBinding?.checkbox?.setOnCheckedChangeListener { _, b ->
            isChecked = b
        }

        mBinding?.checkbox2?.setOnCheckedChangeListener { _, b ->
            isChecked2 = b
        }

        mBinding?.allSee?.setOnClickListener {
//            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Const.PRIVACY_URL))
//            parentContext.startActivity(browserIntent)
        }
    }

    override fun initView() {
        setCancelable(false)
        //mBinding?.webview?.loadUrl(Const.PRIVACY_URL)
    }
}