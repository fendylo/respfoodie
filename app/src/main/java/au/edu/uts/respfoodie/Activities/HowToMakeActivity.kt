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

        val food = intent.getParcelableExtra<Food>(FoodRecommendationsFragment.FOOD_RECOMMENDATION_ID)
        if(food != null){
            updateWidgets(food)
        }
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
                text = "- " +  ingredient["quantity"] + " " + ingredient["name"]
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