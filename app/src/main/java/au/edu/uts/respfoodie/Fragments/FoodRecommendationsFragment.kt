package au.edu.uts.respfoodie.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import au.edu.uts.respfoodie.Activities.HowToMakeActivity
import au.edu.uts.respfoodie.Activities.ShoppingActivity
import au.edu.uts.respfoodie.Adapters.FoodAdapter
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.fragment_food_recommendations.*

class FoodRecommendationsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_recommendations, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FoodRecommendationsFragment().apply {
                arguments = Bundle().apply {

                }
            }
        val FOOD_RECOMMENDATION_ID = "FOOD_RECOMMENDATION_ID"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayFood = fetchFoodRecommendations("asd")

        val adapter = FoodAdapter(requireContext(), arrayFood)

        FoodRecommendationsFragment_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        FoodRecommendationsFragment_recyclerview.setHasFixedSize(true)

        adapter.setOnBtnClickCallback(object : FoodAdapter.OnItemClickCallback {
            override fun onBtn1ClickListener(food: Food) {
                val intent = Intent(requireActivity(), HowToMakeActivity::class.java)
                intent.putExtra(FOOD_RECOMMENDATION_ID, food.id)
                startActivity(intent)
            }

            override fun onBtn2ClickListener(food: Food) {
                val intent = Intent(requireActivity(), ShoppingActivity::class.java)
                intent.putExtra(FOOD_RECOMMENDATION_ID, food.id)
                startActivity(intent)
            }

            override fun onBtn3ClickListener(food: Food) {
                Toast.makeText(requireContext(), "Added to Favourite!", Toast.LENGTH_SHORT).show()
            }
        })

        FoodRecommendationsFragment_recyclerview.adapter = adapter
    }

    fun fetchFoodRecommendations (user_id: String): ArrayList<Food>{
        var foods = ArrayList<Food>()
        foods.add(Food("1","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("2","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("3","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("4","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("5","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("6","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("7","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))

        return foods
    }


}