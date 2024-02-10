package au.edu.uts.respfoodie.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initButtons()
    }

    fun initButtons(){
        LoginActivity_loginButton.setOnClickListener{
            // get the input data
            val email = LoginActivity_emailEditText.text
            val password = LoginActivity_passwordEditText.text

            // check the credentials
//            checkCredentials(email, password)
            Toast.makeText(this, "$email $password", Toast.LENGTH_SHORT).show()

            // store all the informations (user_id, token) to shared preference
//            val editor: SharedPreferences.Editor = SplashActivity.shared_preference.edit()

            // check if day changes or new user
            val intent = Intent(this, PersonalisationActivity::class.java) // Replace YourActivityName with the name of your activity
            startActivity(intent)
            finish()

//            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, HomeActivity::class.java) // Replace YourActivityName with the name of your activity
//            startActivity(intent)
//            finish()
        }

        LoginActivity_signupButton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java) // Replace YourActivityName with the name of your activity
            startActivity(intent)
        }
    }

    fun checkCredentials(email: String, password: String){

    }
}