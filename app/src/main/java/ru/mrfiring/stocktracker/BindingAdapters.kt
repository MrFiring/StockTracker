package ru.mrfiring.stocktracker

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mrfiring.stocktracker.home.HomeRecyclerViewAdapter
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol

@BindingAdapter("listData")
fun RecyclerView.setListData(list: List<DomainStockSymbol>?){
    val adpt = adapter as HomeRecyclerViewAdapter
    adpt.submitList(list)
}

@BindingAdapter("imageUrl")
fun ImageView.bindImage(imgUrl: String?){
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(context)
                .load(imgUri)
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(android.R.drawable.ic_dialog_alert))
                .into(this)
    }
}