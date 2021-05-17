package main

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.BaseFragment
import com.example.tvmazeschedule.R
import model.ScheduleResponse
import network.ApiClient
import network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : BaseFragment() {
    private var responseAdapter: ResponseAdapter? = null
    private var scheduleResponses: MutableList<ScheduleResponse> = ArrayList()
    private var showRecycler: RecyclerView? = null
    private val navController: NavController? = null
    private var backArrow: ImageView? = null
    private var searchView: SearchView? = null
    private var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, parent, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        v = view
        showRecycler = v!!.findViewById(R.id.scheduleRV)
        backArrow = v!!.findViewById(R.id.back_arrow)
        backArrow!!.setOnClickListener(backClickListener)
        searchView = v!!.findViewById(R.id.search_view)
        initSearchView()
        fetchSchedule()
    }

    private val backClickListener = View.OnClickListener { requireActivity().onBackPressed() }
    private fun initSearchView() {
        val magImage = searchView!!.findViewById<View>(androidx.appcompat.R.id.search_mag_icon) as ImageView
        magImage.visibility = View.GONE
        magImage.setImageDrawable(null)
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (activity as MainActivity?)!!.hideKeyboard()
                if (TextUtils.isEmpty(query)) {
                    Toast.makeText(activity, "No results", Toast.LENGTH_LONG).show()
                    return false
                } else {
                    getShowsFromSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        val clearButton = searchView!!.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        clearButton.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white), PorterDuff.Mode.MULTIPLY)
        clearButton.setOnClickListener { v: View? ->
            (activity as MainActivity?)!!.hideKeyboard()
            if (searchView!!.query.length == 0) {
                searchView!!.isIconified = true
            } else {
                fetchSchedule()
                searchView!!.setQuery("", false)
            }
        }
    }

    fun fetchSchedule() {
        scheduleResponses.clear()
        val pattern = "yyyy-MM-dd"
        val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
        val date = Calendar.getInstance().time
        val strDate = dateFormat.format(date)
        val service = ApiClient.getRetrofit().create(ApiInterface::class.java)
        val call = service.getSchedule("US", strDate)
        call.enqueue(object : Callback<ArrayList<ScheduleResponse>> {
            override fun onResponse(call: Call<ArrayList<ScheduleResponse>>, response: Response<ArrayList<ScheduleResponse>>) {
                scheduleResponses = response.body()!!
                Log.d(TAG, "response code: " + response.code() + "/ response.body: " + response.body().toString())
                responseAdapter = ResponseAdapter(scheduleResponses, activity, navController)
                val lm = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                showRecycler!!.layoutManager = lm
                showRecycler!!.adapter = responseAdapter
            }

            override fun onFailure(call: Call<ArrayList<ScheduleResponse>>, t: Throwable) {
                Log.d(TAG, t.toString())
            }
        })
    }

    fun getShowsFromSearch(query: String?) {
        scheduleResponses.clear()
        val service = ApiClient.getRetrofit().create(ApiInterface::class.java)
        val call = service.searchShows(query)
        call.enqueue(object : Callback<ArrayList<ScheduleResponse>> {
            override fun onResponse(call: Call<ArrayList<ScheduleResponse>>, response: Response<ArrayList<ScheduleResponse>>) {
                scheduleResponses = response.body()!!
                Log.d(TAG, "response code: " + response.code() + "/ response.body: " + response.body())
                responseAdapter = ResponseAdapter(scheduleResponses, activity, navController)
                val lm = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                showRecycler!!.layoutManager = lm
                showRecycler!!.adapter = responseAdapter
            }

            override fun onFailure(call: Call<ArrayList<ScheduleResponse>>, t: Throwable) {
                Log.d(TAG, t.toString())
            }
        })
    }

    companion object {
        private val TAG = MainFragment::class.java.simpleName
    }
}