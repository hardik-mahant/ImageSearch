package com.hardikmahant.imagesearch.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardikmahant.imagesearch.R
import com.hardikmahant.imagesearch.adapters.ImageListAdapter
import com.hardikmahant.imagesearch.models.Data
import com.hardikmahant.imagesearch.models.Image
import com.hardikmahant.imagesearch.ui.ImageDetailActivity
import com.hardikmahant.imagesearch.ui.ImagesViewModel
import com.hardikmahant.imagesearch.ui.MainActivity
import com.hardikmahant.imagesearch.util.Constants.LIST_SPAN_COUNT
import kotlinx.android.synthetic.main.fragment_saved_images.*

class SavedImagesFragment : Fragment(R.layout.fragment_saved_images) {

    private lateinit var viewModel: ImagesViewModel
    private lateinit var imageListAdapter: ImageListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.getSavedImages().observe(viewLifecycleOwner, { images ->
            imageListAdapter.differ.submitList(images)
        })
    }

    private fun setupRecyclerView() {
        imageListAdapter = ImageListAdapter()
        listSavedImages.apply {
            adapter = imageListAdapter
            layoutManager = GridLayoutManager(activity, LIST_SPAN_COUNT)
        }

        imageListAdapter.setOnItemCLickListener {
            activity?.startActivity(
                Intent(activity, ImageDetailActivity::class.java)
                    .putExtra("imageData", it)
            )
        }
    }
}