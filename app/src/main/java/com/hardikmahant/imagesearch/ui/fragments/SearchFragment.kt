package com.hardikmahant.imagesearch.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hardikmahant.imagesearch.R
import com.hardikmahant.imagesearch.adapters.ImageListAdapter
import com.hardikmahant.imagesearch.ui.ImageDetailActivity
import com.hardikmahant.imagesearch.ui.ImagesViewModel
import com.hardikmahant.imagesearch.ui.MainActivity
import com.hardikmahant.imagesearch.util.*
import com.hardikmahant.imagesearch.util.Constants.LIST_SPAN_COUNT
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var viewModel: ImagesViewModel
    private lateinit var imageListAdapter: ImageListAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchView.clearFocus()
                    searchForImages(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

        viewModel.imageSearchData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Loading -> {
                    logD("Loading")
                    showProgressBar()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { imageResponse ->
                        imageListAdapter.differ.submitList(imageResponse.data.toList())
                        val totalPages =
                            imageResponse.data.size / Constants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchPageCount == totalPages
                        if (isLastPage) {
                            imageList.setPadding(0, 0, 0, 0)
                        }
                    }
                    logD("Success: ${response.data}")
                }
                is Resource.Error -> {
                    logD("Error: ${response.message}")
                    activity?.showToast("Error: ${response.message}")
                    hideProgressBar()
                }
            }
        })

        imageListAdapter.setOnItemCLickListener {
            activity?.startActivity(
                Intent(activity, ImageDetailActivity::class.java)
                    .putExtra("imageData", it)
            )
        }
    }

    private fun showProgressBar() {
        progressBar.show()
    }

    private fun hideProgressBar() {
        progressBar.makeInvisible()
    }

    private fun searchForImages(query: String) {
        if (query.isNotEmpty()) {
            viewModel.searchForImages(query)
        } else {
            activity?.showToast("Query is empty!")
        }
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItem + visibleItemCount >= totalCount
            val isNotAtBeginning = firstVisibleItem >= 0
            val isTotalMoreThanVisible = totalCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.searchForImages(searchView.query.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        imageListAdapter = ImageListAdapter()
        imageList.apply {
            adapter = imageListAdapter
            layoutManager = GridLayoutManager(activity, LIST_SPAN_COUNT)
            addOnScrollListener(this@SearchFragment.scrollListener)
        }
    }
}