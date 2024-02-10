package au.edu.uts.respfoodie.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import au.edu.uts.respfoodie.Adapters.RestaurantsAdapter
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Classes.Restaurant
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_shopping.*
import kotlinx.android.synthetic.main.fragment_food_recommendations.*

class ShoppingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        val (latitude, longitude) = getLocation()
        val arrRestaurants = fetchRestaurants(latitude, longitude)

        val adapter = RestaurantsAdapter(this, arrRestaurants)

        ShoppingActivity_recyclerview.layoutManager = LinearLayoutManager(this)
        ShoppingActivity_recyclerview.setHasFixedSize(true)

        adapter.setOnBtnClickCallback(object : RestaurantsAdapter.OnItemClickCallback {
            override fun onBtnClickListener(restaurant: Restaurant) {
                val gmmIntentUri = Uri.parse(restaurant.gmaps_url)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        })

        ShoppingActivity_recyclerview.adapter = adapter
    }

    fun getLocation(): Pair<Float, Float>{
        return Pair(32.1f, 21.2f)
    }

    fun fetchRestaurants(latitude : Float, longitude : Float) : ArrayList<Restaurant>{
        val arrRestaurants = ArrayList<Restaurant>()
        arrRestaurants.add(Restaurant("1", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("2", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("3", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("4", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("5", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("6", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("7", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        arrRestaurants.add(Restaurant("8", "Side Bar", "509 Pitt St, Haymarket NSW 2000",3.9f, "https://www.google.com/maps/place/Side+Bar/@-33.8822569,151.2020429,17z/data=!3m2!4b1!5s0x6b12ae23f82b26b3:0xd009d32917117973!4m6!3m5!1s0x6b12ae23f9db0f51:0x7c5aabdb87f31e8a!8m2!3d-33.8822614!4d151.2046178!16s%2Fg%2F1twyzpyd?entry=ttu"))
        return arrRestaurants
    }
}