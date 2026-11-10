package vn.edu.vlu.lab09

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import vn.edu.vlu.lab09.databinding.ItemPhotoBinding

class PhotoAdapter(private val photos: List<PhotoItem>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoHolder>() {

    class PhotoHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoHolder(binding)
    }

    override fun getItemCount(): Int = photos.size

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val photo = photos[position]
        holder.binding.tvTitle.text = photo.title
        holder.binding.tvAuthor.text = photo.author
        Glide.with(holder.itemView)
            .load(photo.imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_broken_image)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.ivPhoto)
    }

    override fun onViewRecycled(holder: PhotoHolder) {
        Glide.with(holder.binding.ivPhoto).clear(holder.binding.ivPhoto)
        super.onViewRecycled(holder)
    }
}
