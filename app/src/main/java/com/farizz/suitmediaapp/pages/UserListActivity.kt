package com.farizz.suitmediaapp.pages

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farizz.suitmediaapp.User
import com.farizz.suitmediaapp.UserAdapter
import com.farizz.suitmediaapp.databinding.ActivityUserListBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class UserListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserListBinding
    private lateinit var adapter: UserAdapter
    private var isLoading = false
    private var page = 1
    private val perPage = 6
    private var totalPages = Int.MAX_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter(mutableListOf()) { user ->
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.putExtra(WelcomeActivity.EXTRA_SELECTED_USER, "${user.firstName} ${user.lastName}")

            startActivity(intent)
        }

        binding.toolbarThird.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(this)
        binding.rvUsers.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            page = 1
            adapter.clearUsers()
            fetchUsers()
        }

        binding.rvUsers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val lm = rv.layoutManager as LinearLayoutManager
                if (!isLoading && lm.findLastVisibleItemPosition() == adapter.itemCount - 1 && page <= totalPages) {
                    fetchUsers()
                }
            }
        })

        fetchUsers()
    }

    private fun fetchUsers() {
        isLoading = true
        binding.swipeRefreshLayout.isRefreshing = true

        val request = Request.Builder()
            .url("https://reqres.in/api/users?page=$page&per_page=$perPage")
            .addHeader("x-api-key", "reqres-free-v1")
            .build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    binding.swipeRefreshLayout.isRefreshing = false
                    isLoading = false
                    Toast.makeText(this@UserListActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.string()?.let {
                    val jsonObject = JSONObject(it)
                    val usersJson = jsonObject.optJSONArray("data") ?: JSONArray()
                    totalPages = jsonObject.optInt("total_pages", totalPages)

                    val users = mutableListOf<User>()
                    for (i in 0 until usersJson.length()) {
                        val u = usersJson.getJSONObject(i)
                        users.add(
                            User(
                                u.getInt("id"),
                                u.getString("email"),
                                u.getString("first_name"),
                                u.getString("last_name"),
                                u.getString("avatar")
                            )
                        )
                    }

                    runOnUiThread {
                        binding.swipeRefreshLayout.isRefreshing = false
                        isLoading = false

                        if (users.isEmpty() && adapter.itemCount == 0) {
                            binding.emptyText.visibility = View.VISIBLE
                        } else {
                            binding.emptyText.visibility = View.GONE
                        }

                        adapter.addUsers(users)
                        page++
                    }
                }
            }
        })
    }
}
