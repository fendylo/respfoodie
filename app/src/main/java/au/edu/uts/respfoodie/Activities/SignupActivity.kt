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
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*

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

//            Sign up

            Toast.makeText(this, "Sign Up Successful..", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}