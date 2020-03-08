package com.arch.search.presentation.dialog



import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.WindowManager
import com.arch.search.R
import com.arch.search.constant.Constant


class DialogLoading(context: Context) : AlertDialog(context) {
    private val mHandler:Handler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        window?.let {w ->
            w.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            w.attributes.apply {
                gravity= Gravity.CENTER
            }
        }

        setCancelable(false)
    }

    override fun show() {
        try {
            super.show()
            mHandler.removeCallbacks(mRunnableDismiss)
            mHandler.postDelayed(mRunnableDismiss, Constant.LOADING_MAX_TIME)

        } catch (e: Exception) {
        }
    }

    fun changeDelay(delay:Long){
        mHandler.removeCallbacks(mRunnableDismiss)
        mHandler.postDelayed(mRunnableDismiss, delay)
    }

    override fun dismiss() {
        try {
            mHandler.removeCallbacks(mRunnableDismiss)
            mHandler.removeMessages(0)
            super.dismiss()
        } catch (e: Exception) {
        }catch (e: IllegalStateException) {
        }
    }

    private val mRunnableDismiss = Runnable {
        try {
            dismiss()
        } catch (e: Exception) {
        }catch (e: IllegalStateException) {
        }
    }
}
