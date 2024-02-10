package au.edu.uts.respfoodie.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import au.edu.uts.respfoodie.Adapters.FoodAdapter
import au.edu.uts.respfoodie.Classes.DatePickerFragment
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        SignupActivity_birthdateButton.setOnClickListener{
            val newFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
            newFragment.setOnDateSelected(object : DatePickerFragment.OnDateSelected {
                override fun onDateSelected(year: Int, month: Int, day: Int) {
                    Toast.makeText(this@SignupActivity, year.toString() +" " + month.toString() + " " + day.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }

        SignupActivity_submitSignUpButton.setOnClickListener{
            Toast.makeText(this, "Sign Up Successful!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}