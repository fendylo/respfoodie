package au.edu.uts.respfoodie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import au.edu.uts.respfoodie.Adapters.DietaryAdapter
import au.edu.uts.respfoodie.Adapters.FoodAdapter
import au.edu.uts.respfoodie.Classes.Dietary
import au.edu.uts.respfoodie.Classes.Food
import au.edu.uts.respfoodie.R
import kotlinx.android.synthetic.main.fragment_dietary.*
import kotlinx.android.synthetic.main.fragment_food_recommendations.*

class DietaryFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dietary, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DietaryFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arrayDietaries = fetchDietaries("asd")

        val adapter = DietaryAdapter(requireContext(), arrayDietaries)

        DietaryFragment_foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        DietaryFragment_foodRecyclerView.setHasFixedSize(true)
        DietaryFragment_foodRecyclerView.adapter = adapter
    }

    fun fetchDietaries (user_id: String): ArrayList<Dietary>{
        var dietaries = ArrayList<Dietary>()
        dietaries.add(Dietary("1","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","100 gr", "Breakfast", "10:00 AM","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        dietaries.add(Dietary("2","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","100 gr", "Lunch", "10:00 AM","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        dietaries.add(Dietary("3","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","100 gr", "Dinner", "10:00 AM","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))
        dietaries.add(Dietary("4","Pizza","Lorem ipsum is derived from the Latin “dolorem ipsum” roughly translated as “pain itself.” Lorem ipsum presents the sample font and orientation of writing on web pages and other software applications","100 gr", "Snack 1", "10:00 AM","https://cdn.loveandlemons.com/wp-content/uploads/2023/02/vegetarian-pizza.jpg"))

        return dietaries
    }
}