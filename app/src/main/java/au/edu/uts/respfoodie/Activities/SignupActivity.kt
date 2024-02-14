package au.edu.uts.respfoodie.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import au.edu.uts.respfoodie.Adapters.FoodAdapter
import au.edu.uts.respfoodie.Classes.DatePickerFragment
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Classes.MyVolley
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import au.edu.uts.respfoodie.R
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        initButtons()
    }

    fun initButtons(){
        SignupActivity_birthdateButton.setOnClickListener{
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
            newFragment.setOnDateSelected(object : DatePickerFragment.OnDateSelected {
                override fun onDateSelected(year: Int, month: Int, day: Int) {
                    SignupActivity_birthdateEditText.setText(month.toString().padStart(2,'0') + "-" +
                            day.toString().padStart(2,'0') + "-" +
                            year.toString())
                }
            })
        }

        SignupActivity_submitSignUpButton.setOnClickListener{
            val email = SignupActivity_emailEditText.text.toString()
            val password = SignupActivity_passwordEditText.text.toString()
            val name = SignupActivity_nameEditText.text.toString()
            val birthDate = SignupActivity_birthdateEditText.text.toString()
            val weight = SignupActivity_weightEditText.text.toString()
            val height = SignupActivity_heightEditText.text.toString()
            val checked_id = SignupActivity_GenderRadioGroup.checkedRadioButtonId

            if(email == "" || password == "" || name == "" || birthDate == "" || weight == "" || height == "" || checked_id == -1){
                Toast.makeText(this, "Make sure all the inputs are filled..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!"^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex().matches(email)){
                Toast.makeText(this, "Make sure the email is in correct format..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var gender = findViewById<RadioButton>(checked_id).text.toString()
            gender = if (gender == "M") "Male" else "Female"

            val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.substring(6,10).toInt()

//            Sign up process
            var map = HashMap<String, String>()
            map["email"] = email
            map["password"] = password

            map["age"] = age.toString()
            map["birthdate"] = birthDate
            map["gender"] = gender
            map["height"] = height
            map["name"] = name
            map["weight"] = weight

            val volley = MyVolley(MyVolley.POST_METHOD,"/users/register", map, this)

            volley.setCallback(object : MyVolley.MyVolleyInterface {
                override fun onResponse(response: String?) {
                    try {
                        if (response != null) {
                            Toast.makeText(this@SignupActivity, "Sign Up Successful..", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onError(error: VolleyError?) {
                    Toast.makeText(this@SignupActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })


        }
    }
}