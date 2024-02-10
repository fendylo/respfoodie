package au.edu.uts.respfoodie.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_foods.view.*

class FoodAdapter(private val context: Context, private val arrayFood: ArrayList<Food>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(val v: View) : RecyclerView.ViewHolder(v)

    private var onBtnCallback: OnItemClickCallback? = null

    fun setOnBtnClickCallback(onItemClickCallback: OnItemClickCallback?) {
        this.onBtnCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_foods, parent, false)

        return FoodViewHolder(v)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.v.ListFood_name.text = arrayFood[position].name
        holder.v.ListFood_description.text = arrayFood[position].description

        Glide.with(holder.v)
            .load(arrayFood[position].image_url)
            .into(holder.v.ListFood_imageview)

        holder.v.ListFood_btn1.setOnClickListener(View.OnClickListener {
            onBtnCallback?.onBtn1ClickListener(
                arrayFood[position]
            )
        })

        holder.v.ListFood_btn2.setOnClickListener(View.OnClickListener {
            onBtnCallback?.onBtn2ClickListener(
                arrayFood[position]
            )
        })

        holder.v.ListFood_btn3.setOnClickListener(View.OnClickListener {
            onBtnCallback?.onBtn3ClickListener(
                arrayFood[position]
            )
        })

    }

    override fun getItemCount(): Int {
        return arrayFood.size
    }

    interface OnItemClickCallback {
        fun onBtn1ClickListener(food: Food)
        fun onBtn2ClickListener(food: Food)
        fun onBtn3ClickListener(food: Food)
    }
}