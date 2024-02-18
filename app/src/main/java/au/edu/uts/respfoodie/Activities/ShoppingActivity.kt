package au.edu.uts.respfoodie.Activities

import android.content.Context
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import au.edu.uts.respfoodie.Adapters.RestaurantsAdapter
import au.edu.uts.respfoodie.Classes.Restaurant
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.activity_shopping.*
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Classes.Helper
import au.edu.uts.respfoodie.Classes.MyVolley
import au.edu.uts.respfoodie.Fragments.FoodRecommendationsFragment
import com.android.volley.VolleyError
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class ShoppingActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private var latitude = 0.0f
    private var longitude = 0.0f
    private val locationPermissionCode = 2
    private lateinit var food : Food
    lateinit var mainHandler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping)

        food = intent.getParcelableExtra<Food>(FoodRecommendationsFragment.FOOD_RECOMMENDATION_ID)!!

        if(food != null){
            mainHandler = Handler(Looper.getMainLooper())

            Helper.showLoadingAnimation(this, "Locating Eateries",
                "Scouting nearby restaurants that serve your favorite flavors. Stay tuned!",
                "restaurant_animation.json")

            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
            }
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            latitude = location.latitude.toFloat()
            longitude = location.longitude.toFloat()
            fetchRestaurants(latitude, longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
                }
            }
        }
    }

    fun fetchRestaurants(latitude : Float, longitude : Float){
        val arrRestaurants = ArrayList<Restaurant>()
        val URL = "/users/get_nearby_restaurants?food_name=" + food.name + "&latitude="+latitude.toString()+"&longitude="+longitude.toString()+"&category=" + food.category

        val volley = MyVolley(MyVolley.GET_METHOD, URL, HashMap<String,String>(), this)

        volley.setCallback(object : MyVolley.MyVolleyInterface {
            override fun onResponse(response: String?) {
                try {
                    if (response != null) {
                        val jsonObject = JSONObject(response)

                        val msg = jsonObject.getString("msg")
                        val data = jsonObject.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val restaurant = data.getJSONObject(i)
                            arrRestaurants.add(Restaurant("",restaurant.getString("name"),
                                restaurant.getString("address"),
                                restaurant.getDouble("rating").toFloat(),
                                restaurant.getString("gmaps_url")))
                        }
                        Toast.makeText(this@ShoppingActivity, msg, Toast.LENGTH_SHORT).show()
                        loadRecyclerView(arrRestaurants)
                        if(arrRestaurants.size <= 0){
                            ShoppingActivity_notFound.visibility = View.VISIBLE
                        }
                        Helper.hideLoadingAnimation()
                        mainHandler.removeCallbacks(refreshLocation)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    if(arrRestaurants.size <= 0){
                        ShoppingActivity_notFound.visibility = View.VISIBLE
                    }
                    Helper.hideLoadingAnimation()
                }
            }
            override fun onError(error: VolleyError?) {
                Toast.makeText(this@ShoppingActivity, error?.message.toString(), Toast.LENGTH_SHORT).show()
                if(arrRestaurants.size <= 0){
                    ShoppingActivity_notFound.visibility = View.VISIBLE
                }
                Helper.hideLoadingAnimation()
            }
        })
    }

    fun loadRecyclerView(arrRestaurants: ArrayList<Restaurant>){
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

    override fun onPause() {
        super.onPause()
        if(::mainHandler.isInitialized){
            mainHandler.removeCallbacks(refreshLocation)
        }
    }

    override fun onResume() {
        super.onResume()
        if(::mainHandler.isInitialized){
            mainHandler.post(refreshLocation)
        }
    }

    private val refreshLocation = object : Runnable {
        override fun run() {
            mainHandler.postDelayed(this, 1000)
            if ((ContextCompat.checkSelfPermission(this@ShoppingActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
            }
        }
    }
}