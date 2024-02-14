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

import android.widget.RadioGroup
import au.edu.uts.respfoodie.Activities.SplashActivity
import au.edu.uts.respfoodie.Classes.MyVolley
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.activity_signup.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap


class FoodRecommendationsFragment : Fragment() {
    private var food_recommendations = ArrayList<Food>()
    private var favourites_foods = ArrayList<Food>()
    private var trending_foods = ArrayList<Food>()
    private var shown_foods = ArrayList<Food>()

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

        shown_foods = fetchTrending("asd")
        fetchUser()
        loadRecyclerView(shown_foods)
        initRadioButtons()
    }

    fun loadRecyclerView(arrayFood: ArrayList<Food>){
        val adapter = FoodAdapter(requireContext(), arrayFood)

        FoodRecommendationsFragment_recyclerview.layoutManager = LinearLayoutManager(requireContext())
        FoodRecommendationsFragment_recyclerview.setHasFixedSize(true)

        adapter.setOnBtnClickCallback(object : FoodAdapter.OnItemClickCallback {
            override fun onBtn1ClickListener(food: Food) {
                val intent = Intent(requireActivity(), HowToMakeActivity::class.java)
                intent.putExtra(FOOD_RECOMMENDATION_ID, food)
                startActivity(intent)
            }

            override fun onBtn2ClickListener(food: Food) {
                val intent = Intent(requireActivity(), ShoppingActivity::class.java)
                intent.putExtra(FOOD_RECOMMENDATION_ID, food)
                startActivity(intent)
            }

            override fun onBtn3ClickListener(food: Food) {
                var URL = ""
                var method = -1
                var map = HashMap<String, String>()

                if(!food.favourite){
                    URL = "/users/add_to_favorites"
                    method = MyVolley.POST_METHOD
                    favourites_foods.add(food)
                    map["id"] = SplashActivity.sharedPreferences.getString("USER_ID","").toString()
                    map["recommendationId"] = food.id
                }
                else{
                    URL = "/users/remove_from_favorites?id=" + SplashActivity.sharedPreferences.getString("USER_ID","").toString() + "&recommendationId=" + food.id
                    method = MyVolley.DELETE_METHOD
                    favourites_foods = favourites_foods.filter { f -> f.id != food.id} as ArrayList<Food>
                }
                food.setFavorite(!food.favourite)
                adapter.notifyItemChanged(arrayFood.indexOf(food))

                val volley = MyVolley(method,URL, map, requireContext())

                volley.setCallback(object : MyVolley.MyVolleyInterface {
                    override fun onResponse(response: String?) {
                        try {
                            if (response != null) {
                                val jsonObject = JSONObject(response)
                                val msg = jsonObject.getString("msg")
                                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(error: VolleyError?) {
                        Toast.makeText(requireContext(), error?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })

        FoodRecommendationsFragment_recyclerview.adapter = adapter
    }

    fun initRadioButtons(){
        FoodRecommendationsFragment_radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.FoodRecommendationsFragment_radioButton_nonPersonalized -> {
                    loadRecyclerView(trending_foods)
                    shown_foods = trending_foods
                }
                R.id.FoodRecommendationsFragment_radioButton_personalized -> {
                    loadRecyclerView(food_recommendations)
                    shown_foods = food_recommendations
                }
                R.id.FoodRecommendationsFragment_radioButton_favorites -> {
                    loadRecyclerView(favourites_foods)
                    shown_foods = favourites_foods
                }
            }
        })
    }

    fun fetchTrending (user_id: String): ArrayList<Food>{
        var foods = ArrayList<Food>()
        foods.add(Food("1","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("2","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("3","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("4","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        foods.add(Food("5","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))

        return foods
    }

    fun fetchPersonalized (recommendations: JSONArray){
        food_recommendations = ArrayList<Food>()
        for (i in 0 until recommendations.length()) {
            val dataObject = recommendations.getJSONObject(i)

            val food_recommendation = dataObject.getJSONObject("food_recommendation")
            val id = food_recommendation.getString("food_id")
            val image_url = food_recommendation.getString("image_url")
            val name = food_recommendation.getString("name")
            val description = food_recommendation.getString("description")
            val food = Food(id,name,description,image_url)

            val procedures = food_recommendation.getJSONArray("procedure")
            for (j in 0 until procedures.length()) {
                food.addProcedure(procedures.getString(j))
            }
            val ingredients = food_recommendation.getJSONArray("ingredients")
            for (j in 0 until ingredients.length()) {
                val ing = ingredients.getJSONObject(j)
                food.addIngredients(ing.getString("name"), ing.getString("quantity"))
            }
            food_recommendations.add(food)
        }
    }

    fun fetchFavourites (favourites: JSONArray){
        favourites_foods = ArrayList<Food>()
        for (i in 0 until favourites.length()) {
            val dataObject = favourites.getJSONObject(i)

            val id = dataObject.getString("food_id")
            val image_url = dataObject.getString("image_url")
            val name = dataObject.getString("name")
            val description = dataObject.getString("description")
            val food = Food(id,name,description,image_url)

            val procedures = dataObject.getJSONArray("procedure")
            for (j in 0 until procedures.length()) {
                food.addProcedure(procedures.getString(j))
            }
            val ingredients = dataObject.getJSONArray("ingredients")
            for (j in 0 until ingredients.length()) {
                val ing = ingredients.getJSONObject(j)
                food.addIngredients(ing.getString("name"), ing.getString("quantity"))
            }
            food.setFavorite(true)
            favourites_foods.add(food)

            // set food_recommendations favourite
            var selectedFood: Food = food_recommendations.filter { s -> s.id == id }.single()
            selectedFood.setFavorite(true)

            // set trending foods favourite
        }
    }

    fun fetchUser(){
        val shared = SplashActivity.sharedPreferences
        val URL = "/users?id=" + shared.getString("USER_ID", "false").toString()
        val volley = MyVolley(MyVolley.GET_METHOD, URL, HashMap<String,String>(), requireContext())
        volley.setCallback(object : MyVolley.MyVolleyInterface {
            override fun onResponse(response: String?) {
                try {
                    if (response != null) {
                        val jsonObject = JSONObject(response)
                        val msg = jsonObject.getString("msg")
                        val data = jsonObject.getJSONObject("data")
                        val food_recommendations = data.getJSONArray("food_recommendations")
                        val favourites = data.getJSONArray("favourites")

                        fetchPersonalized(food_recommendations)
                        fetchFavourites(favourites)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            override fun onError(error: VolleyError?) {
                Toast.makeText(requireContext(), error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}