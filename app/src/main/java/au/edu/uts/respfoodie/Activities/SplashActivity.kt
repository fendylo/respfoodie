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
//        editor.putString("PASSWORD", "LHOA")
//        editor.apply()


        SplashActivity_logoImageView.alpha = 0f
        SplashActivity_logoImageView.animate().setDuration(1500).alpha(1f).withEndAction{
            val username = getSharedPreferences().getString("EMAIL", "false")
            val password = getSharedPreferences().getString("PASSWORD", "false")
            Toast.makeText(this, "$username $password", Toast.LENGTH_SHORT).show()
            navigate()
        }
    }

    fun checkSession(){

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