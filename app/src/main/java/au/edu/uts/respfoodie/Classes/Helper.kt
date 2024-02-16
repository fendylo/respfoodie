package au.edu.uts.respfoodie.Classes

import android.app.Activity
import android.widget.ImageView
import au.edu.uts.respfoodie.R
import dev.shreyaspatil.MaterialDialog.AbstractDialog
import dev.shreyaspatil.MaterialDialog.MaterialDialog

class Helper {
    companion object{
        lateinit var mAnimatedDialog : MaterialDialog

        fun showLoadingAnimation(activity: Activity, title: String, message: String, animation: String, scale : Float = 0.35f){
            mAnimatedDialog = MaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setAnimation(animation)
                .build()
            mAnimatedDialog.getAnimationView().setScaleType(ImageView.ScaleType.CENTER)
            mAnimatedDialog.getAnimationView().setScale(scale)
            mAnimatedDialog.show()
        }

        fun showPopup(activity: Activity, title: String, message: String, animation: String, scale : Float = 0.35f){
            mAnimatedDialog = MaterialDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Okay!", R.drawable.ic_check,
                    AbstractDialog.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                .setAnimation(animation)
                .build()
            mAnimatedDialog.getAnimationView().setScaleType(ImageView.ScaleType.CENTER)
            mAnimatedDialog.getAnimationView().setScale(scale)
            mAnimatedDialog.show()
        }

        fun hideLoadingAnimation(){
            mAnimatedDialog.dismiss()
        }
    }
}