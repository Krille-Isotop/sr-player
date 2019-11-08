package com.example.playah

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ApiClient {
    companion object {
        fun getEpisodes(context: Context, successBlock: (String) -> Unit, errorBlock: (VolleyError) -> Unit) {
            val queue = Volley.newRequestQueue(context)

            val stringRequest = StringRequest(
                Request.Method.GET, "http://api.sr.se/api/v2/episodes/index?programid=3718&fromdate=2012-08-01&todate=2012-08-31&urltemplateid=3&audioquality=hi&format=json",
                Response.Listener<String>(successBlock),
                Response.ErrorListener(errorBlock))

            queue.add(stringRequest)
        }
    }
}