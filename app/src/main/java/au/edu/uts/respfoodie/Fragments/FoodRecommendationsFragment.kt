package au.edu.uts.respfoodie.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import au.edu.uts.respfoodie.Classes.Helper
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
    private var num_request = 0
    lateinit var mainHandler: Handler
    var showed = "Trending"

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

        Helper.showLoadingAnimation(requireActivity(), "Trending Treats",
            "Just a moment, we're gathering the hottest food trends for your culinary adventure!",
            "cooking_animation.json", 0.45f)
        trending_foods = fetchTrending()

//        post the data in background
        val shared = SplashActivity.sharedPreferences
        if(shared.contains("PERSONALISATION_PROCESS") && shared.getBoolean("PERSONALISATION_PROCESS", false)){
            mainHandler = Handler(Looper.getMainLooper())
        }
        else{
            fetchUser()
        }
    }

    override fun onPause() {
        super.onPause()
        if(::mainHandler.isInitialized){
            mainHandler.removeCallbacks(refreshRecommendations)
        }
    }

    override fun onResume() {
        super.onResume()
        if(::mainHandler.isInitialized){
            mainHandler.post(refreshRecommendations)
        }

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
                    URL = "/users/add_to_favourites"
                    method = MyVolley.POST_METHOD
                    favourites_foods.add(food)
                    map["id"] = SplashActivity.sharedPreferences.getString("USER_ID","").toString()
                    map["recommendationId"] = food.id

                    // if found in other array, set favourite to true
                    var selectedFood: List<Food> = food_recommendations.filter { s -> s.id == food.id }
                    if(selectedFood.size > 0){
                        selectedFood.single().setFavorite(true)
                    }

                    selectedFood = trending_foods.filter { s -> s.id == food.id }
                    if(selectedFood.size > 0){
                        selectedFood.single().setFavorite(true)
                    }

                    food.setFavorite(true)
                }
                else{
                    URL = "/users/remove_from_favourites?id=" + SplashActivity.sharedPreferences.getString("USER_ID","").toString() + "&recommendationId=" + food.id
                    method = MyVolley.DELETE_METHOD
                    favourites_foods = favourites_foods.filter { f -> f.id != food.id} as ArrayList<Food>

                    // if found in other array, set favourite to false
                    var selectedFood: List<Food> = food_recommendations.filter { s -> s.id == food.id }
                    if(selectedFood.size > 0){
                        selectedFood.single().setFavorite(false)
                    }

                    selectedFood = trending_foods.filter { s -> s.id == food.id }
                    if(selectedFood.size > 0){
                        selectedFood.single().setFavorite(false)
                    }
                    food.setFavorite(false)
                }

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
            FoodRecommendationsFragment_notFound.visibility = View.GONE
            when (checkedId) {
                R.id.FoodRecommendationsFragment_radioButton_nonPersonalized -> {
                    loadRecyclerView(trending_foods)
                    shown_foods = trending_foods
                    showed = "Trending"
                }
                R.id.FoodRecommendationsFragment_radioButton_personalized -> {
                    loadRecyclerView(food_recommendations)
                    shown_foods = food_recommendations
                    showed = "Personalized"
                    if(food_recommendations.size <= 0){
                        Helper.showPopup(requireActivity(), "Fetching Recommendations",
                        "Hang tight! We're cooking up the best food recommendations for you.",
                        "cooking_animation.json")
                    }
                }
                R.id.FoodRecommendationsFragment_radioButton_favorites -> {
                    loadRecyclerView(favourites_foods)
                    shown_foods = favourites_foods
                    showed = "Favorites"
                }
            }
            if(shown_foods.size <= 0){
                FoodRecommendationsFragment_notFound.visibility = View.VISIBLE
            }
        })
    }

    fun fetchTrending (): ArrayList<Food>{
        var foods = ArrayList<Food>()
        val URL = "/users/trending_foods"
        val volley = MyVolley(MyVolley.GET_METHOD, URL, HashMap<String,String>(), requireContext())
        volley.setCallback(object : MyVolley.MyVolleyInterface {
            override fun onResponse(response: String?) {
                try {
                    if (response != null) {
                        val jsonArray = JSONArray(response)
                        for (i in 0 until jsonArray.length()) {
                            val f = jsonArray.getJSONObject(i)
                            val id = f.getString("id")
                            val image_url = f.getString("image_url")
                            val name = f.getString("name")
                            val description = f.getString("description")
                            val food = Food(id,name,description,image_url)

                            val procedures = f.getJSONArray("procedure")
                            for (j in 0 until procedures.length()) {
                                food.addProcedure(procedures.getString(j))
                            }
                            val ingredients = f.getJSONArray("ingredients")
                            for (j in 0 until ingredients.length()) {
                                val ing = ingredients.getJSONObject(j)
                                food.addIngredients(ing.getString("name"), ing.getString("quantity"))
                            }
                            trending_foods.add(food)
                        }
                        shown_foods = trending_foods
                        Helper.hideLoadingAnimation()
                        loadRecyclerView(shown_foods)
                        initRadioButtons()
                    }
                } catch (e: JSONException) {
                    Helper.hideLoadingAnimation()
                    e.printStackTrace()
                }
            }
            override fun onError(error: VolleyError?) {
                Helper.hideLoadingAnimation()
                Toast.makeText(requireContext(), error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        num_request += 1
        if(num_request >= 2){
            setFavouriteTrending()
        }
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
        val shared = SplashActivity.sharedPreferences
        if(food_recommendations.size > 0 && shared.contains("PERSONALISATION_PROCESS") && shared.getBoolean("PERSONALISATION_PROCESS", false)){
            mainHandler.removeCallbacks(refreshRecommendations)
            val editor = SplashActivity.sharedPreferencesEditor
            editor.remove("PERSONALISATION_PROCESS")
            editor.apply()

            postDietaryControl()
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
            var selectedFood: List<Food> = food_recommendations.filter { s -> s.id == id }
            if(selectedFood.size > 0){
                selectedFood.single().setFavorite(true)
            }
        }

        num_request += 1
        if(num_request >= 2){
            setFavouriteTrending()
        }
    }

    fun setFavouriteTrending(){
        // set trending foods favourite
        for(f in favourites_foods){
            val selectedFood = trending_foods.filter { s -> s.id == f.id }
            if(selectedFood.size > 0){
                selectedFood.single().setFavorite(true)
                if(showed == "Trending"){
                    FoodRecommendationsFragment_recyclerview.adapter!!.notifyItemChanged(trending_foods.indexOf(selectedFood.single()))
                }
            }
        }

        num_request = 0
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

    private val refreshRecommendations = object : Runnable {
        override fun run() {
            mainHandler.postDelayed(this, 5000)
            fetchUser()
        }
    }

    fun postDietaryControl(){
        val shared = SplashActivity.sharedPreferences

        var map = HashMap<String, String>()
        map["id"] = shared.getString("USER_ID", "").toString()

        val volley = MyVolley(MyVolley.POST_METHOD,"/users/create_dietary_control_gemini", map, requireContext())
        volley.setCallback(object : MyVolley.MyVolleyInterface {
            override fun onResponse(response: String?) {
                try {

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            override fun onError(error: VolleyError?) {
//                    Toast.makeText(this@PersonalisationActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}