package au.edu.uts.respfoodie.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import au.edu.uts.respfoodie.Fragments.DietaryFragment
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import au.edu.uts.respfoodie.Fragments.ProfileFragment
import au.edu.uts.respfoodie.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), ProfileFragment.OnFragmentInteractionListener {
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        initFragments(savedInstanceState)
    }

    fun initFragments(savedInstanceState: Bundle?){
        HomeActivity_navigation.setOnItemSelectedListener { item ->
            val f: Fragment
            if (item == 0) {
                f = FoodRecommendationsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.HomeActivity_container, f).commit()
                true
            } else if (item == 1) {
                f = DietaryFragment()
                supportFragmentManager.beginTransaction().replace(R.id.HomeActivity_container, f).commit()
                true
            }
            else if (item == 2) {
                f = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.HomeActivity_container, f).commit()
                true
            }
            false
        }

        if (savedInstanceState == null) {
            // initial showed fragment
            val f: Fragment
            f = FoodRecommendationsFragment()
            supportFragmentManager.beginTransaction().replace(R.id.HomeActivity_container, f).commit()
        }
    }

    override fun onFragmentInteraction() {
        auth = FirebaseAuth.getInstance()
        auth.signOut()

        val editor = SplashActivity.sharedPreferencesEditor
        editor.clear()
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}