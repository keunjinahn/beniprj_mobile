package kr.ac.beni.beniprj.ui.custom

import android.content.Context
import android.widget.Toast
import kr.ac.beni.beniprj.R
import kr.ac.beni.beniprj.databinding.DialogChatRegisterBinding


class CustomChatRegisterDialog(
    parentContext: Context,
    listener: OnChatRegisterListener?
) : BaseDialog<DialogChatRegisterBinding>(parentContext) {

    override val layoutId: Int get() = R.layout.dialog_chat_register
    private var onRegisterListener: OnChatRegisterListener? = null

    interface OnChatRegisterListener{
        fun onConfirmClick(title: String, senderNumber: String, receiverNumber: String, receiverAddress: String = "")
    }

    init {
        this.onRegisterListener = listener
    }

    override fun setUIEventListener() {

        mBinding?.textCancel?.setOnClickListener {
            dismiss()
        }

        mBinding?.textRegister?.setOnClickListener {

            if (mBinding?.editChatTitle?.text.toString().isEmpty()) {
                Toast.makeText(context, "제목을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mBinding?.editSenderNumber?.text.toString().isEmpty()) {
                Toast.makeText(context, "발신자 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mBinding?.editReceiverNumber?.text.toString().isEmpty()) {
                Toast.makeText(context, "수신자 번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            onRegisterListener?.onConfirmClick(
                title = mBinding?.editChatTitle?.text.toString(),
                senderNumber = mBinding?.editSenderNumber?.text.toString(),
                receiverNumber = mBinding?.editReceiverNumber?.text.toString(),
                receiverAddress = mBinding?.editReceiverAddress?.text.toString()
            )


//            onRegisterListener?.onConfirmClick(
//                title = mBinding?.editChatTitle?.text.toString(),
//                senderNumber = mBinding?.editSenderNumber?.text.toString(),
//                receiverNumber = mBinding?.editReceiverNumber?.text.toString(),
//                receiverAddress = mBinding?.editReceiverAddress?.text.toString()
//            )

            dismiss()
        }
    }

    override fun initView() {
        setCancelable(false)
    }

}