package au.edu.uts.respfoodie.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import au.edu.uts.respfoodie.Classes.Dietary
import au.edu.uts.respfoodie.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_dietary.view.*
import kotlinx.android.synthetic.main.list_foods.view.*
import kotlinx.android.synthetic.main.list_restaurants.view.*

class DietaryAdapter(private val context: Context, private val arrDietaries: ArrayList<Dietary>) : RecyclerView.Adapter<DietaryAdapter.DietaryViewHolder>() {

    class DietaryViewHolder(val v: View) : RecyclerView.ViewHolder(v)

//    private var onBtnCallback: OnItemClickCallback? = null

//    fun setOnBtnClickCallback(onItemClickCallback: OnItemClickCallback?) {
//        this.onBtnCallback = onItemClickCallback
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietaryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_dietary, parent, false)

        return DietaryViewHolder(v)
    }

    override fun onBindViewHolder(holder: DietaryViewHolder, position: Int) {
        holder.v.listDietary_foodNameTextView.text = arrDietaries[position].food_name
        holder.v.listDietary_foodDescriptionTextView.text = arrDietaries[position].food_description
        holder.v.listDietary_foodPortionTextView.text = arrDietaries[position].portion
        holder.v.listDietary_foodTimingTextView.text = arrDietaries[position].timing
        holder.v.listDietary_timeTextView.text = arrDietaries[position].time

        Glide.with(holder.v)
            .load(arrDietaries[position].image_url)
            .into(holder.v.listDietary_foodImageView)

//        holder.v.ListRestaurant_navigateButton.setOnClickListener(View.OnClickListener {
//            onBtnCallback?.onBtnClickListener(
//                arrayRestaurants[position]
//            )
//        })
    }

    override fun getItemCount(): Int {
        return arrDietaries.size
    }

//    interface OnItemClickCallback {
//        fun onBtnClickListener(restaurant: Restaurant)
//    }
}