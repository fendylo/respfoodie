package au.edu.uts.respfoodie.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import au.edu.uts.respfoodie.R
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val sharedPrefName = ""
    companion object{
        lateinit var sharedPreferences : SharedPreferences
        lateinit var sharedPreferencesEditor : SharedPreferences.Editor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferrence_key), Context.MODE_PRIVATE)
        sharedPreferencesEditor = getSharedPreferences(getString(R.string.shared_preferrence_key), Context.MODE_PRIVATE).edit()

        sharedPreferencesEditor.putString("EMAIL", "fendycoba8@gmail.com")
        sharedPreferencesEditor.putString("USER_ID", "123")
        sharedPreferencesEditor.putString("TOKEN", "This is token")
        sharedPreferencesEditor.putString("NAME", "Fendy")
        sharedPreferencesEditor.putString("GENDER", "Male")
        sharedPreferencesEditor.putString("BIRTHDATE", "2001-04-07")
        sharedPreferencesEditor.putString("WEIGHT", "65 Kg")
        sharedPreferencesEditor.putString("HEIGHT", "173 Cm")
        sharedPreferencesEditor.putString("PROHIBITED_INGREDIENTS", "Fish, Vegies, Pork")
        sharedPreferencesEditor.putString("TASTE_PREFERENCES", "Sweet, Savoury, Spicy")
        sharedPreferencesEditor.putString("BACKGROUND", "Lorem ipsum is the filler text that typically demonstrates the font and style of a text in a document or visual demonstration. It serves as a place holder indicating where the text will be in the final iteration. Originally from Latin, Lorem ipsum has no intelligible meaning.")
        sharedPreferencesEditor.apply()

        FirebaseApp.initializeApp(this)

        SplashActivity_logoImageView.alpha = 0f
        SplashActivity_logoImageView.animate().setDuration(1500).alpha(1f).withEndAction{
//            val user_id = sharedPreferences.getString("USER_ID", "false")
//            val email = sharedPreferences.getString("EMAIL", "false")
//            val token = sharedPreferences.getString("TOKEN", "false")
//            val password = sharedPreferences.getString("PASSWORD", "false")

            checkSession()
            navigate()
        }
    }

    fun checkSession() : Boolean{
        if(sharedPreferences.contains("USER_ID")){
            // check and refresh token in shared preference
            return true
        }
        return false
    }

    fun navigate(){
        // check whether user has logined or not.
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}