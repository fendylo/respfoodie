package au.edu.uts.respfoodie.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_personalisation.*
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.view.View
import android.widget.EditText
import android.widget.Toast
import au.edu.uts.respfoodie.Classes.Dietary
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Classes.MyVolley
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class PersonalisationActivity : AppCompatActivity() {

    companion object{
        val MODE = "MODE"
        val PROHIBITED = "PROHIBITED"
        val TASTE = "TASTE"
        val BACKGROUND = "BACKGROUND"
    }

    private var mode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalisation)

        val prohibited_ingredients = fetchIngredients()
        val selectedIng = BooleanArray(prohibited_ingredients.size)
        val selectedIngredients = ArrayList<Int>()
        // setting the multiple spinner
        initMultipleSpinner(PersonalisationActivity_prohibitedIngredientsEditText,prohibited_ingredients,selectedIng,selectedIngredients, "Select Ingredients")

        val taste_preferences = fetchTaste()
        val selectedTas = BooleanArray(taste_preferences.size)
        val selectedTastes = ArrayList<Int>()
        // setting the multiple spinner
        initMultipleSpinner(PersonalisationActivity_tastePreferenceEditText,taste_preferences,selectedTas,selectedTastes, "Adjust your Taste")

        mode = intent.getStringExtra(MODE).toString()

        if(mode != null && mode == "edit") {
            PersonalisationActivity_additionalInputLayout.visibility = View.VISIBLE
            PersonalisationActivity_backgroundEditText.setLines(6)
//            fetch all avoided dishes
            fetchUnwanted()

//            fill all inputted information
            val prohibited = intent.getStringExtra(PROHIBITED)
            val taste = intent.getStringExtra(TASTE)
            val background = intent.getStringExtra(BACKGROUND)

            PersonalisationActivity_prohibitedIngredientsEditText.setText(prohibited)
            PersonalisationActivity_tastePreferenceEditText.setText(taste)
            PersonalisationActivity_backgroundEditText.setText(background)

//            check all the prohibited ingredients
            selectedIng.forEachIndexed{i, element ->
                selectedIng[i] = false
            }
            val ingredients = prohibited?.split(", ")
            if (ingredients != null) {
                for (ing in ingredients) {
                    val idx = prohibited_ingredients.indexOf(ing)
                    if(idx >= 0){
                        selectedIng[prohibited_ingredients.indexOf(ing)] = true
                        selectedIngredients.add(prohibited_ingredients.indexOf(ing))
                    }
                }
            }

            selectedTas.forEachIndexed{i, element ->
                selectedTas[i] = false
            }
//            check all the taste taste_preferences
            val preferences = taste?.split(", ")
            if (preferences != null) {
                for (pref in preferences) {
                    val idx = taste_preferences.indexOf(pref)
                    if(idx >= 0){
                        selectedTas[taste_preferences.indexOf(pref)] = true
                        selectedTastes.add(taste_preferences.indexOf(pref))
                    }
                }
            }
        }

        initButtons()
    }

    fun initButtons(){
        PersonalisationActivity_submitPreferencesButton.setOnClickListener{
            val prohibited = PersonalisationActivity_prohibitedIngredientsEditText.text.toString()
            val preference = PersonalisationActivity_tastePreferenceEditText.text.toString()
            val background = PersonalisationActivity_backgroundEditText.text.toString()
            val avoided = PersonalisationActivity_avoidedDishes.text.toString()

            if(background == ""){
                Toast.makeText(this, "Make sure the background input is filled..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//             post the data in background
            val shared = SplashActivity.sharedPreferences

            var map = HashMap<String, String>()
            map["id"] = shared.getString("USER_ID", "").toString()
            map["forbiddenFood"] = prohibited
            map["favoriteFood"] = preference
            map["UserInformation"] = background
            map["avoidDishes"] = avoided

            val volley = MyVolley(MyVolley.POST_METHOD,"/users/create_recommendations", map, this)
            volley.setCallback(object : MyVolley.MyVolleyInterface {
                override fun onResponse(response: String?) {
                    try {
                        if (response != null) {
                            // update the shared preference
//                            val msg = JSONObject(response).getString("msg")
//                            Toast.makeText(this@PersonalisationActivity, msg, Toast.LENGTH_SHORT).show()
//                            Toast.makeText(this@PersonalisationActivity, "TIMEOUT", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                override fun onError(error: VolleyError?) {
//                    Toast.makeText(this@PersonalisationActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            val editor = SplashActivity.sharedPreferencesEditor
            editor.putBoolean("NEW_USER", false)
            editor.putString("BACKGROUND", SplashActivity.sharedPreferences.getString("BACKGROUND","") + "$" + background)
            editor.putString("PROHIBITED_INGREDIENTS", prohibited.replace(',','$'))
            editor.putString("TASTE_PREFERENCES", preference.replace(',', '$'))
            editor.putBoolean("PERSONALISATION_PROCESS", true)
            editor.apply()

            navigate()
        }
    }

    fun initMultipleSpinner(et: EditText, arrOptions: ArrayList<String>, selectedOpt : BooleanArray, selectedOptions: ArrayList<Int>, header: String){
        et.setOnClickListener {
            // Initialize alert dialog
            val builder = AlertDialog.Builder(this)

            // Set title
            builder.setTitle(header)

            // Set dialog non cancelable
            builder.setCancelable(false)

            builder.setMultiChoiceItems(arrOptions.toTypedArray(), selectedOpt,
                OnMultiChoiceClickListener { dialogInterface, i, b ->
                    // check condition
                    if (b) {
                        // when checkbox selected
                        // Add position  in lang list
                        selectedOptions.add(i)
                        // Sort array list
                        Collections.sort(selectedOptions)
                    } else {
                        // when checkbox unselected
                        // Remove position from langList
                        selectedOptions.remove(Integer.valueOf(i))
                    }
                })

            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, i -> // Initialize string builder
                val stringBuilder = StringBuilder()
                // use for loop
                for (j in 0 until selectedOptions.size) {
                    // concat array value
                    stringBuilder.append(arrOptions.get(selectedOptions.get(j)))
                    // check condition
                    if (j != selectedOptions.size - 1) {
                        // When j value  not equal
                        // to lang list size - 1
                        // add comma
                        stringBuilder.append(", ")
                    }
                }
                // set text on textView
                et.setText(stringBuilder.toString())
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.setNeutralButton(
                "Clear All"
            ) { dialogInterface, i ->
                // use for loop
                for (j in 0 until selectedOpt.size) {
                    // remove all selection
                    selectedOpt[j] = false
                    // clear language list
                    selectedOptions.clear()
                    // clear text view value
                    et.setText("")
                }
            }
            // show dialog
            builder.show()
        }
    }

    fun fetchIngredients() : ArrayList<String>{
        // fetch the prohibited ingredients
        val arrIngredients = ArrayList<String>()
        arrIngredients.add("Pork")
        arrIngredients.add("Seafood")
        arrIngredients.add("Vegies")
        arrIngredients.add("Dairy")
        arrIngredients.add("Eggs")
        arrIngredients.add("Peanuts")
        arrIngredients.add("Spices")
        return arrIngredients
    }

    fun fetchTaste() : ArrayList<String>{
        // fetch the prohibited ingredients
        val arrTaste = ArrayList<String>()
        arrTaste.add("Sweet")
        arrTaste.add("Sour")
        arrTaste.add("Salty")
        arrTaste.add("Bitter")
        arrTaste.add("Umami")
        return arrTaste
    }

    fun fetchUnwanted(){
        // fetch all recommended food names for this user
        val arrDishes = ArrayList<String>()

        val shared = SplashActivity.sharedPreferences
        val URL = "/users?id=" + shared.getString("USER_ID", "false").toString()
        val volley = MyVolley(MyVolley.GET_METHOD, URL, HashMap<String,String>(), this)
        volley.setCallback(object : MyVolley.MyVolleyInterface {
            override fun onResponse(response: String?) {
                try {
                    if (response != null) {
                        val jsonObject = JSONObject(response)
                        val data = jsonObject.getJSONObject("data")
                        val food_recommendations = data.getJSONArray("food_recommendations")
                        for (i in 0 until food_recommendations.length()) {
                            val dataObject = food_recommendations.getJSONObject(i)

                            val food_recommendation =
                                dataObject.getJSONObject("food_recommendation")
                            val name = food_recommendation.getString("name")
                            arrDishes.add(name)
                        }

                        val selectedDish = BooleanArray(arrDishes.size)
                        val selectedDishes = ArrayList<Int>()
                        initMultipleSpinner(PersonalisationActivity_avoidedDishes,arrDishes,selectedDish,selectedDishes, "Choose your unwanted food..")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            override fun onError(error: VolleyError?) {
                Toast.makeText(this@PersonalisationActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun navigate(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}