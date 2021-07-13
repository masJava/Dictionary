package com.mas.dictionary.view.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mas.dictionary.R
import com.mas.dictionary.databinding.ActivityMainRecyclerviewItemBinding
import com.mas.model.DataModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class MainAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private val applicationContext: Context
) :
    RecyclerView.Adapter<MainAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = arrayListOf()

    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder =
        RecyclerItemViewHolder(
            ActivityMainRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(private val vb: ActivityMainRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(vb.root) {

        fun bind(data: DataModel) = with(vb) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                val previewUrl = data.meanings?.get(0)?.previewUrl
                if (!previewUrl.isNullOrBlank()) {
                    ivPreview?.let { usePicassoToLoadPhoto(it, previewUrl) }
                }
                headerTextviewRecyclerItem.text =
                    "${data.text} [${data.meanings?.get(0)?.transcription}]"
                var translation = ""
                data.meanings?.forEach { translation += "${it.translation?.translation}, " }
                descriptionTextviewRecyclerItem.text =
                    translation.substring(0, translation.length - 2)

                itemView.setOnClickListener { openInNewWindow(data) }
            }
        }
    }

    private fun usePicassoToLoadPhoto(imageView: ImageView, imageLink: String) {
        Picasso.with(applicationContext).load("https:$imageLink")
            .placeholder(R.drawable.ic_no_photo_vector).fit().centerCrop()
            .into(imageView, object : Callback {
                override fun onSuccess() {
                }

                override fun onError() {
                    imageView.setImageResource(R.drawable.ic_baseline_error_24)
                }
            })
    }

    private fun openInNewWindow(listItemData: DataModel) {
        onListItemClickListener.onItemClick(listItemData)
    }

    interface OnListItemClickListener {
        fun onItemClick(data: DataModel)
    }
}
