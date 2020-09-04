package com.hardikmahant.imagesearch.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hardikmahant.imagesearch.R
import com.hardikmahant.imagesearch.models.Data
import kotlinx.android.synthetic.main.image_list_item.view.*

class ImageListAdapter : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // using DiffUtil to improve RecyclerView performance

    private val differCallBack = object : DiffUtil.ItemCallback<Data>() {
        //comparing values of two objects, old and new
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return if (oldItem.images_count > 0 && newItem.images_count > 0) {
                oldItem.images[0].link == newItem.images[0].link
            } else {
                false
            }
        }

        //comparing two objects, old and new
        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.image_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageData = differ.currentList[position]
        holder.itemView.apply {
            if (imageData.images_count > 0) {
                //loading the image
                Glide.with(this)
                    .load(imageData.images[0].link)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_image)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(imageView)
            }
            //setting up the click listener to each item view
            setOnClickListener {
                onItemClickListener?.let {
                    it(imageData)
                }
            }
        }
    }

    /**
     * @return Total no of items being represented inside the RecyclerView
     * */
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    //setting up the click listener for each item of the RecyclerView

    private var onItemClickListener: ((Data) -> Unit)? = null

    /**
     * Sets the local item listener value
     * @param listener: Anonymous function with parameter of type Data
     * */
    fun setOnItemCLickListener(listener: ((Data) -> Unit)?) {
        onItemClickListener = listener
    }
}