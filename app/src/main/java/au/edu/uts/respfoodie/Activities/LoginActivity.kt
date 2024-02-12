package au.edu.uts.respfoodie.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import au.edu.uts.respfoodie.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        initButtons()
    }

    fun initButtons(){
        LoginActivity_loginButton.setOnClickListener{
//            get the input data
            val email = LoginActivity_emailEditText.text.toString()
            val password = LoginActivity_passwordEditText.text.toString()

//            if(email == "" || password == ""){
//                Toast.makeText(this, "Make sure all the inputs are filled..", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            if(!"^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex().matches(email)){
//                Toast.makeText(this, "Make sure the email is in correct format..", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

//            check the credentials
            if(checkCredentials(email, password)){
//                store all the informations (user_id, token) to shared preference
//                val editor: SharedPreferences.Editor = SplashActivity.shared_preference.edit()

                // check if day changes or new user
                val intent = Intent(this, PersonalisationActivity::class.java) // Replace YourActivityName with the name of your activity
                startActivity(intent)
                finish()

//            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, HomeActivity::class.java) // Replace YourActivityName with the name of your activity
//            startActivity(intent)
//            finish()
            }
        }

        LoginActivity_signupButton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java) // Replace YourActivityName with the name of your activity
            startActivity(intent)
        }
    }

    fun checkCredentials(email: String, password: String) : Boolean{
        var success = true
//        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
//            if(task.isSuccessful){
//                Toast.makeText(this, task.result?.user?.getIdToken(false)?.result?.token.toString() ,Toast.LENGTH_SHORT).show()
//                // token
//                println(task.result?.user?.getIdToken(false)?.result?.token.toString())
//                // uid
//                println(task.result?.user?.uid.toString())
//                success = true
//            }
//        }.addOnFailureListener { exception ->
//            Toast.makeText(this, "This user is not registered..",Toast.LENGTH_SHORT).show()
//        }
        return success
    }
}