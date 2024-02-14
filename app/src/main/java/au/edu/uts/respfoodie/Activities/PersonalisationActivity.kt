package au.edu.uts.respfoodie.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_personalisation.*
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.widget.EditText
import android.widget.Toast
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

        initButtons()

        val mode = intent.getStringExtra(MODE)

        if(mode != null && mode == "edit") {
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
    }

    fun initButtons(){
        PersonalisationActivity_submitPreferencesButton.setOnClickListener{
            val prohibited = PersonalisationActivity_prohibitedIngredientsEditText.text.toString()
            val preference = PersonalisationActivity_tastePreferenceEditText.text.toString()
            val background = PersonalisationActivity_backgroundEditText.text.toString()

            if(prohibited == "" || preference == "" || background == ""){
                Toast.makeText(this, "Make sure all the inputs are filled..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//             post the data

//            var map = HashMap<String, String>()
//            map["forbidden_ingredients"] = prohibited
//            map["taste_preferences"] = preference
//            map["past_prompts"] = background

//            val volley = MyVolley(MyVolley.POST_METHOD,"/users/personalisation", map, this)
//            volley.setCallback(object : MyVolley.MyVolleyInterface {
//                override fun onResponse(response: String?) {
//                    try {
//                        if (response != null) {
//                            // update the shared preference
//                            navigate()
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//                override fun onError(error: VolleyError?) {
//                    Toast.makeText(this@PersonalisationActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
//                }
//            })

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

    fun navigate(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}