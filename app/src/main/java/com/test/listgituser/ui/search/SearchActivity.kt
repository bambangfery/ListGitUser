package com.test.listgituser.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.listgituser.data.pojo.Items
import com.test.listgituser.databinding.ActivitySearchBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.test.listgituser.R
import com.test.listgituser.util.*

class SearchActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: SearchViewModelFactory by instance()

    private lateinit var binding : ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private lateinit var mAdapter: GroupAdapter<ViewHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    private lateinit var adapterLinear: SearchItemsAdapter
    private lateinit var loadMoreItemsCells: ArrayList<Items?>

    var page: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewModel = ViewModelProvider(this,factory).get(SearchViewModel::class.java)

        binding.searchBtn.setOnClickListener {
            page = 1
            binding.rvListUser.adapter = null
            getListUser(binding.editQuery.text.toString().trim())
        }

    }

    private fun getListUser(query: String){
        if (!query.isNullOrEmpty() || !query.isNullOrBlank()){
            binding.progressBar.visibility = View.VISIBLE
            Coroutines.main {
                try {
                    val getList = viewModel.getSearchUser(query,page.toString())
                    if (getList.items.size>0){
                        binding.noresult.visibility = View.GONE
                        binding.rvListUser.visibility = View.VISIBLE
                        initRecyclerView(getList.items)
                    }else{
                        binding.noresult.visibility = View.VISIBLE
                        binding.rvListUser.visibility = View.GONE
                    }

                } catch (e: ApiException) {
                    e.printStackTrace()
                    if (e.message.toString().contains("API rate limit exceeded"))
                        Toast.makeText(
                            this
                            , "Sorry, API rate limit exceeded. Please wait 1 minute " +
                                    "and request again"
                            , Toast.LENGTH_LONG
                        ).show()
                } catch (e: NoInternetException) {
                    e.printStackTrace()
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }



    private fun initRecyclerView(searchItem: ArrayList<Items?>) {
        adapterLinear = SearchItemsAdapter(searchItem)
        adapterLinear.notifyDataSetChanged()
        mLayoutManager = LinearLayoutManager(this)
        binding.rvListUser.apply {
            layoutManager = mLayoutManager
            setHasFixedSize(true)
            removeAllViews()
            adapter = adapterLinear
        }

        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        binding.rvListUser.addOnScrollListener(scrollListener)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                LoadMoreData()
            }
        })

    }

    private fun LoadMoreData() {
        adapterLinear.removeLoadingView()
        adapterLinear.addLoadingView()
        loadMoreItemsCells = ArrayList()
        Handler().postDelayed({
            //Max 50 Page
            if (page <= 50){
                page = page.inc()
            }
            Coroutines.main {
                try {
                    val query = binding.editQuery.text.toString().trim()
                    val getList = viewModel.getSearchUser(query,page.toString())
                    if (getList.items.size>0){
                        loadMoreItemsCells.addAll(getList.items)
                        //Remove the Loading View
                        adapterLinear.removeLoadingView()
                        //We adding the data to our main ArrayList
                        adapterLinear.addData(loadMoreItemsCells)
                        //Change the boolean isLoading to false
                        scrollListener.setLoaded()
                        //Update the recyclerView in the main thread
                        binding.rvListUser.post {
                            adapterLinear.notifyDataSetChanged()
                        }
                    }else{
                        adapterLinear.removeLoadingView()
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                } catch (e: NoInternetException) {
                    e.printStackTrace()
                }

            }
        }, 3000)

    }
}