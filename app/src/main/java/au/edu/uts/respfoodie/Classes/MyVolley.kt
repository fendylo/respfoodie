package au.edu.uts.respfoodie.Classes

import android.content.Context
import android.widget.Toast
import au.edu.uts.respfoodie.R
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap


class MyVolley(
    val method: Int,
    val stringRequest: String,
    val my_param: HashMap<String, String>,
    val context: Context
) {
    companion object{
        val GET_METHOD = Request.Method.GET
        val POST_METHOD = Request.Method.POST
        val PUT_METHOD = Request.Method.PUT
        val DELETE_METHOD = Request.Method.DELETE
    }

    var URL_SERVICE: String
    private var callback: MyVolleyInterface? = null

    fun setCallback(newCallback: MyVolleyInterface?) {
        this.callback = newCallback
        this.execute()
    }

    fun execute() {
        if (callback == null) {
            Toast.makeText(context, "Callback is null..", Toast.LENGTH_SHORT).show()
            return
        }
        val stringRequest: StringRequest = object : StringRequest(
            method,
            URL_SERVICE + stringRequest,
            Response.Listener { response -> callback?.onResponse(response) },
            Response.ErrorListener { error -> callback?.onError(error) }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): HashMap<String, String> {
                return my_param
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    interface MyVolleyInterface {
        fun onResponse(response: String?)
        fun onError(err: VolleyError?)
    }

    init {
        URL_SERVICE = context.resources.getString(R.string.urlservice)
    }
}
