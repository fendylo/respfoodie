package au.edu.uts.respfoodie.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import au.edu.uts.respfoodie.Activities.SplashActivity
import au.edu.uts.respfoodie.Adapters.DietaryAdapter
import au.edu.uts.respfoodie.Classes.Dietary
import au.edu.uts.respfoodie.Classes.Helper
import au.edu.uts.respfoodie.Classes.MyVolley
import au.edu.uts.respfoodie.R
import com.android.volley.VolleyError
import kotlinx.android.synthetic.main.fragment_dietary.*
import kotlinx.android.synthetic.main.fragment_food_recommendations.*
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class DietaryFragment : Fragment() {

    private val timing = arrayOf("breakfast","snack1", "lunch", "snack2", "dinner")

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
        fetchDietaries()
    }

    fun loadRecylerView(arrayDietaries : ArrayList<Dietary>){
        val adapter = DietaryAdapter(requireContext(), arrayDietaries)

        DietaryFragment_foodRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        DietaryFragment_foodRecyclerView.setHasFixedSize(true)
        DietaryFragment_foodRecyclerView.adapter = adapter
    }

    fun fetchDietaries (){
        Helper.showLoadingAnimation(requireActivity(), "Tailoring Your Menu",
            "Arranging a selection of foods perfectly matched for your dietary success. Almost there!",
            "writing_animation.json", 0.25f)

        val shared = SplashActivity.sharedPreferences
        val URL = "/users?id=" + shared.getString("USER_ID", "false").toString()
        val volley = MyVolley(MyVolley.GET_METHOD, URL, HashMap<String,String>(), requireContext())
        volley.setCallback(object : MyVolley.MyVolleyInterface {
            override fun onResponse(response: String?) {
                try {
                    if (response != null) {
                        val jsonObject = JSONObject(response)
                        val data = jsonObject.getJSONObject("data")
                        val dietary_control = data.getJSONObject("dietary_control")
                        val recommendations = data.getJSONArray("food_recommendations")

                        val dietaries = ArrayList<Dietary>()
                        for (t in timing) {
                            if(dietary_control.has(t)){
                                val dietary = dietary_control.getJSONObject(t)

                                for (j in 0 until recommendations.length()) {
                                    val r = recommendations.getJSONObject(j)
                                    if(r.getString("id") == dietary.getString("inner_id")){
                                        dietaries.add(Dietary(dietary.getString("id"),
                                            if (dietary.has("name")) dietary.getString("name") else "Undefined",
                                            if (dietary.has("description")) dietary.getString("description") else "...",
                                            r.getString("portion"),
                                            t.capitalize(),
                                            dietary.getString("time"),
                                            dietary.getString("image_url"))
                                        )
                                        break
                                    }
                                }
                            }
                        }
                        loadRecylerView(dietaries)
                        if(dietaries.size <= 0){
                            DietaryFragment_notFound.visibility = View.VISIBLE
                        }
                        Helper.hideLoadingAnimation()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    DietaryFragment_notFound.visibility = View.VISIBLE
                    Helper.hideLoadingAnimation()
                }
            }
            override fun onError(error: VolleyError?) {
                Toast.makeText(requireContext(), error?.message.toString(), Toast.LENGTH_SHORT).show()
                DietaryFragment_notFound.visibility = View.VISIBLE
                Helper.hideLoadingAnimation()
            }
        })
    }
}