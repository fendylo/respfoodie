package au.edu.uts.respfoodie.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import au.edu.uts.respfoodie.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.RemoteMessage







class SplashActivity : AppCompatActivity() {

    private var destination = ""
    private lateinit var  auth: FirebaseAuth
    companion object{
        lateinit var sharedPreferences : SharedPreferences
        lateinit var sharedPreferencesEditor : SharedPreferences.Editor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FirebaseApp.initializeApp(this)

        sharedPreferences = getSharedPreferences(getString(R.string.shared_preferrence_key), Context.MODE_PRIVATE)
        sharedPreferencesEditor = getSharedPreferences(getString(R.string.shared_preferrence_key), Context.MODE_PRIVATE).edit()

        println("TOKEN: " + FirebaseInstanceId.getInstance().getToken())

        checkSession()

        SplashActivity_logoImageView.alpha = 0f
        SplashActivity_logoImageView.animate().setDuration(1500).alpha(1f).withEndAction{
            navigate()
        }
    }

    fun checkSession(){
        auth = FirebaseAuth.getInstance()
        if(sharedPreferences.contains("USER_ID") && auth.currentUser != null){
            auth.currentUser!!.getIdToken(true).addOnSuccessListener{
                sharedPreferencesEditor.putString("TOKEN", it.token.toString())
                sharedPreferencesEditor.apply()
            }
            if (sharedPreferences.getBoolean("NEW_USER", true)){
                destination = "Personalisation"
            }
            else{
                destination = "Home"
            }
        }
        else{
            destination = "Login"
        }
    }

    fun navigate(){
        if(destination == "Home"){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        else if(destination == "Login"){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent = Intent(this, PersonalisationActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}