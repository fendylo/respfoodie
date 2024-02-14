package au.edu.uts.respfoodie.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import au.edu.uts.respfoodie.Activities.PersonalisationActivity
import au.edu.uts.respfoodie.Activities.ShoppingActivity
import au.edu.uts.respfoodie.Activities.SplashActivity
import au.edu.uts.respfoodie.R
import com.google.firebase.auth.FirebaseAuth
import dev.shreyaspatil.MaterialDialog.AbstractDialog
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.RuntimeException

class ProfileFragment : Fragment() {
    var onFragmentInteractionListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateInfo()
        initButtons()
    }

    fun updateInfo(){
        val shared = SplashActivity.sharedPreferences
        val name = shared.getString("NAME","")
        val email = shared.getString("EMAIL","")
        val gender = shared.getString("GENDER","")
        val birthdate = shared.getString("BIRTHDATE","")
        val weight = shared.getString("WEIGHT","")
        val height = shared.getString("HEIGHT","")

        val prohibited = shared.getString("PROHIBITED_INGREDIENTS","")
        val taste = shared.getString("TASTE_PREFERENCES","")
        val background = shared.getString("BACKGROUND","")

        ProfileFragment_profile_name.text = "$name's Profile"
        ProfileFragment_profile_email.text = "$email"
        ProfileFragment_profile_gender.text = "$gender"
        ProfileFragment_profile_weight.text = "$birthdate"
        ProfileFragment_profile_height.text = "$weight"
        ProfileFragment_profile_birthdate.text = "$height"

        ProfileFragment_prohibited_ingredients.text = "$prohibited"
        ProfileFragment_tastePreference.text = "$taste"
        ProfileFragment_background.text = "$background"
    }

    fun initButtons(){
        ProfileFragment_logoutButton.setOnClickListener{
            val mAnimatedDialog = MaterialDialog.Builder(requireActivity())
                .setTitle("Message")
                .setMessage("Are you sure to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_check,
                    AbstractDialog.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                        onFragmentInteractionListener?.onFragmentInteraction()
                    })
                .setNegativeButton("Cancel", R.drawable.ic_close,
                    AbstractDialog.OnClickListener { dialogInterface, which -> dialogInterface.dismiss() })
                .setAnimation("logout_animation.json")
                .build()
            mAnimatedDialog.getAnimationView().setScaleType(ImageView.ScaleType.CENTER)
            mAnimatedDialog.getAnimationView().setScale(0.35f)
            mAnimatedDialog.show()

//            val h = Handler()
//            h.postDelayed(Runnable {
//                mAnimatedDialog.dismiss()
//            }, 2000)
        }

        ProfileFragment_editProhibited.setOnClickListener{
            navigateToPersonalisation()
        }

        ProfileFragment_editTastePreference.setOnClickListener{
            navigateToPersonalisation()
        }

        ProfileFragment_editBackground.setOnClickListener{
            navigateToPersonalisation()
        }
    }

    fun navigateToPersonalisation(){
        val shared = SplashActivity.sharedPreferences
        val prohibited = shared.getString("PROHIBITED_INGREDIENTS","")
        val taste = shared.getString("TASTE_PREFERENCES","")
        val background = shared.getString("BACKGROUND","")

        val intent = Intent(requireActivity(), PersonalisationActivity::class.java)

        intent.putExtra(PersonalisationActivity.MODE, "edit")
        intent.putExtra(PersonalisationActivity.PROHIBITED, prohibited)
        intent.putExtra(PersonalisationActivity.TASTE, taste)
        intent.putExtra(PersonalisationActivity.BACKGROUND, background)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onFragmentInteractionListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentInteractionListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }
}