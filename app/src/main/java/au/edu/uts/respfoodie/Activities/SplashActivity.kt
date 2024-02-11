package au.edu.uts.respfoodie.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val sharedPrefName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        val editor: SharedPreferences.Editor = getSharedPreferencesEditor()
//        editor.putString("EMAIL", "HaiNAN")
//        editor.putString("USER_ID", "LHOA")
//        editor.putString("TOKEN", "LHOA")
////        editor.remove("PASSWORD")
////        editor.clear()
//        editor.apply()


        SplashActivity_logoImageView.alpha = 0f
        SplashActivity_logoImageView.animate().setDuration(1500).alpha(1f).withEndAction{

            val shared = getSharedPreferences()
            val user_id = shared.getString("USER_ID", "false")
            val email = shared.getString("EMAIL", "false")
            val token = shared.getString("TOKEN", "false")
            val password = shared.getString("PASSWORD", "false")
            Toast.makeText(this, "$user_id $email $password $token", Toast.LENGTH_SHORT).show()

            checkSession()
            navigate()
        }
    }

    fun checkSession() : Boolean{
        val shared = getSharedPreferences()
        if(shared.contains("USER_ID")){
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

    fun getSharedPreferences(): SharedPreferences {
        return getSharedPreferences(getString(R.string.shared_preferrence_key), Context.MODE_PRIVATE)
    }

    fun getSharedPreferencesEditor(): SharedPreferences.Editor {
        return getSharedPreferences(getString(R.string.shared_preferrence_key), Context.MODE_PRIVATE).edit()
    }
}