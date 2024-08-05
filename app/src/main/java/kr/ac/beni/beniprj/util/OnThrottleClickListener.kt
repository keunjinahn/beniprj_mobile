package kr.ac.beni.beniprj.util

import android.view.View

class OnThrottleClickListener(
    private val clickListener: View.OnClickListener,
    private val interval: Long = 1500 //1.5초
) :
    View.OnClickListener {

    private var clickable = true
    override fun onClick(v: View?) {
        if (clickable) {
            clickable = false
            v?.run {
                postDelayed({
                    clickable = true
                }, interval)
                clickListener.onClick(v)
            }
        } else {
            CommonUtils.commonLog("Waiting for a while")
        }
    }

    fun View.onThrottleClick(action: (v: View) -> Unit) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener))
    }

    // with interval setting
    fun View.onThrottleClick(action: (v: View) -> Unit, interval: Long) {
        val listener = View.OnClickListener { action(it) }
        setOnClickListener(OnThrottleClickListener(listener, interval))
    }
}