package au.edu.uts.respfoodie.Activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import au.edu.uts.respfoodie.Classes.Helper
import au.edu.uts.respfoodie.Classes.MyVolley
import au.edu.uts.respfoodie.R
import com.android.volley.VolleyError
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

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
            val email = LoginActivity_emailEditText.text.toString()
            val password = LoginActivity_passwordEditText.text.toString()

            if(email == "" || password == ""){
                Toast.makeText(this, "Make sure all the inputs are filled..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!"^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex().matches(email)){
                Toast.makeText(this, "Make sure the email is in correct format..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Helper.showLoadingAnimation(this@LoginActivity, "Hello Again!",
                "We're authenticating your account to ensure a secure and personalized experience. Just a sec!",
                "login_animation.json", 0.2f)
            checkCredentials(email, password)
        }

        LoginActivity_signupButton.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java) // Replace YourActivityName with the name of your activity
            startActivity(intent)
        }
    }

    fun checkCredentials(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                // token
                val editor = SplashActivity.sharedPreferencesEditor
                editor.putString("TOKEN", task.result?.user?.getIdToken(false)?.result?.token.toString())
                editor.putString("USER_ID", task.result?.user?.uid.toString())
                editor.apply()

                // get the details of user
                val shared = SplashActivity.sharedPreferences
                val URL = "/users?id=" + shared.getString("USER_ID", "false").toString()

                val volley = MyVolley(MyVolley.GET_METHOD, URL, HashMap<String,String>(), this)

                volley.setCallback(object : MyVolley.MyVolleyInterface {
                    override fun onResponse(response: String?) {
                        try {
                            if (response != null) {
                                val editor = SplashActivity.sharedPreferencesEditor
                                val jsonObject = JSONObject(response)

                                val msg = jsonObject.getString("msg")
                                val data = jsonObject.getJSONObject("data")

                                editor.putString("BIRTHDATE", data.getString("birthdate"))
                                editor.putString("GENDER", data.getString("gender"))
                                editor.putBoolean("NEW_USER", data.getBoolean("newUser"))
                                editor.putString("NAME", data.getString("name"))
                                editor.putString("WEIGHT", data.getString("weight"))
                                editor.putString("AGE", data.getString("age"))
                                editor.putString("EMAIL", data.getString("email"))
                                editor.putString("HEIGHT", data.getString("height"))
                                editor.putString("BACKGROUND", data.getJSONArray("past_prompts").join("$"))
                                editor.putString("PROHIBITED_INGREDIENTS", data.getJSONArray("forbidden_ingredients").join("$"))
                                editor.putString("TASTE_PREFERENCES", data.getJSONArray("taste_preferences").join("$"))
                                editor.apply()

                                Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_SHORT).show()
                                Helper.hideLoadingAnimation()
                                if(data.getBoolean("newUser")){
                                    navigate("Personalisation")
                                }
                                else{
                                    navigate("Home")
                                }

                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Helper.hideLoadingAnimation()
                        }
                    }
                    override fun onError(error: VolleyError?) {
                        Toast.makeText(this@LoginActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
                        Helper.hideLoadingAnimation()
                    }
                })
            }
            else{
                Helper.hideLoadingAnimation()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "This user is not registered..",Toast.LENGTH_SHORT).show()
        }
    }

    fun navigate(activity: String){
        if(activity == "Home"){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent = Intent(this, PersonalisationActivity::class.java)
            startActivity(intent)
        }
        finish()
    }
}