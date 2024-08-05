package kr.ac.beni.beniprj.ui.custom

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kr.ac.beni.beniprj.Const
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.DialogPermissionBinding
import kr.ac.beni.beniprj.util.CommonUtils
import kr.ac.beni.beniprj.util.PreferenceUtil

class PermissionDialog(parentContext: Context, private var listener: OnItemClickListener?) :
    BaseDialog<DialogPermissionBinding>(parentContext) {

    private var isChecked = false
    override val layoutId: Int get() = R.layout.dialog_permission

    interface OnItemClickListener{
        fun onClick(flag:Int, isChecked:Boolean)
    }

    private fun contentView(bleMode: Boolean){
        CommonUtils.commonLog("bleMode ==> $bleMode")
        if(bleMode){
            mBinding?.textContent?.text = context.getString(R.string.dialog_permission_content_bluetooth,Const.BASE_URL)
        }else{
            mBinding?.textContent?.text = context.getString(R.string.dialog_permission_content,Const.BASE_URL)
        }
    }

    override fun setUIEventListener() {
        //val bleMode = PreferenceUtil.getBleMode()
        //contentView(bleMode)
        contentView(false)
//        mBinding?.bleMode?.setOnLongClickListener {
//            Handler(Looper.getMainLooper()).postDelayed({
//                Toast.makeText(context, "BLE 모드를 활성화 합니다.", Toast.LENGTH_SHORT).show()
//                CommonUtils.restart(context)
//            }, 800)
//            true
//        }

        mBinding?.textCancel?.setOnClickListener {
            listener?.onClick(CANCEL,false)
            dismiss()
        }

        mBinding?.textOk?.setOnClickListener {
            if(isChecked){
                listener?.onClick(CONFIRM,true)
                dismiss()
            }else{
                Toast.makeText(context,context.getString(R.string.privacy_no_check), Toast.LENGTH_SHORT).show()
            }
        }

        mBinding?.checkbox?.setOnCheckedChangeListener { _, b ->
            isChecked = b
            PreferenceUtil.setPrefPermission(isChecked)
        }
    }

    override fun initView() {
        setCancelable(false)
    }
}