package au.edu.uts.respfoodie.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_personalisation.*
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.widget.EditText
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class PersonalisationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalisation)

        initButtons()

        // setting the multiple spinner
        val prohibited_ingredients = fetchIngredients()
        val selectedIng = BooleanArray(prohibited_ingredients.size)
        val selectedIngredients = ArrayList<Int>()
        initMultipleSpinner(PersonalisationActivity_prohibitedIngredientsEditText,prohibited_ingredients,selectedIng,selectedIngredients, "Select Ingredients")

        // setting the multiple spinner
        val taste_preferences = fetchTaste()
        val selectedTas = BooleanArray(taste_preferences.size)
        val selectedTastes = ArrayList<Int>()
        initMultipleSpinner(PersonalisationActivity_tastePreferenceEditText,taste_preferences,selectedTas,selectedTastes, "Adjust your Taste")
    }

    fun initButtons(){
        PersonalisationActivity_submitPreferencesButton.setOnClickListener{
            // submit the data

            val intent = Intent(this, HomeActivity::class.java) // Replace YourActivityName with the name of your activity
            startActivity(intent)
            finish()
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
}