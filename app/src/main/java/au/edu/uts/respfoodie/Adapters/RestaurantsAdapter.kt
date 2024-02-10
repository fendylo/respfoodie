package au.edu.uts.respfoodie.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.Classes.Restaurant
import au.edu.uts.respfoodie.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_foods.view.*
import kotlinx.android.synthetic.main.list_restaurants.view.*

class RestaurantsAdapter(private val context: Context, private val arrayRestaurants: ArrayList<Restaurant>) : RecyclerView.Adapter<RestaurantsAdapter.RestaurantsViewHolder>() {

    class RestaurantsViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    private var onBtnCallback: OnItemClickCallback? = null

    fun setOnBtnClickCallback(onItemClickCallback: OnItemClickCallback?) {
        this.onBtnCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_restaurants, parent, false)

        return RestaurantsViewHolder(v)
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        holder.v.ListRestaurant_Name.text = arrayRestaurants[position].name
        holder.v.ListRestaurant_restaurantAddress.text = arrayRestaurants[position].address
        holder.v.ListRestaurant_ratingNumber.text = arrayRestaurants[position].rating.toString()

        holder.v.ListRestaurant_navigateButton.setOnClickListener(View.OnClickListener {
            onBtnCallback?.onBtnClickListener(
                arrayRestaurants[position]
            )
        })

    }

    override fun getItemCount(): Int {
        return arrayRestaurants.size
    }

    interface OnItemClickCallback {
        fun onBtnClickListener(restaurant: Restaurant)
    }
}