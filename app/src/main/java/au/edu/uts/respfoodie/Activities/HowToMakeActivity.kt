package au.edu.uts.respfoodie.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import au.edu.uts.respfoodie.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_how_to_make.*
import kotlinx.android.synthetic.main.list_foods.view.*

class HowToMakeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_how_to_make)

        val food_id = intent.getStringExtra(FoodRecommendationsFragment.FOOD_RECOMMENDATION_ID)
        if(food_id != null){
            val food = fetchFood(food_id)
            updateWidgets(food)
        }
    }

    fun fetchFood(id: String): Food {
        val f =  Food(id,"Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg")
        f.addIngredients("Onion","1","slice")
        f.addIngredients("Oil","2","cup")
        f.addIngredients("Garlic","1","clove")
        f.addIngredients("Flour","100","gr")

        f.addProcedure("Lorem ipsum is derived from the Latin dolorem ipsum")
        f.addProcedure("Lorem ipsum is derived from the Latin dolorem ")
        f.addProcedure("Lorem ipsum is derived from the Latin")
        f.addProcedure("Lorem ipsum is derived from the Latindolorem ipsum")
        f.addProcedure("Lorem ipsum is derived from the Latin")
        f.addProcedure("Lorem ipsum is derived from the Latindolorem ipsum")
        return f
    }

    fun updateWidgets(food: Food){
        HowToMakeActivity_recipeName.text = food.name
        HowToMakeActivity_recipeDescription.text = food.description
        Glide.with(this).load(food.image_url).into(HowToMakeActivity_recipeImage)

        var idx = 1
        for (step: String in food.procedures){
            val tv = TextView(this).apply {
                text = idx.toString() + ". " + step
                textSize = 16f // Set text size as required
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8 // Margin as required
                }
            }
            HowToMakeActivity_stepsContainer.addView(tv)
            idx += 1
        }

        for (ingredient: HashMap<String, String> in food.ingredients){
            val tv = TextView(this).apply {
                text = "- " +  ingredient["quantity"] + " " + ingredient["quantifier"] + " " + ingredient["name"]
                textSize = 16f // Set text size as required
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8 // Margin as required
                }
            }
            HowToMakeActivity_ingredientsContainer.addView(tv)
        }
    }
}