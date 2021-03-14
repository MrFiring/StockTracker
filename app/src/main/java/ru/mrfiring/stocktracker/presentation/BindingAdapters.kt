package ru.mrfiring.stocktracker

//import android.widget.ImageView
//import androidx.core.net.toUri
//import androidx.databinding.BindingAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.avatarfirst.avatargenlib.AvatarConstants
//import com.avatarfirst.avatargenlib.AvatarGenerator
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import ru.mrfiring.stocktracker.presentation.home.HomeRecyclerViewAdapter
//import ru.mrfiring.stocktracker.domain.DomainStockSymbol

//@BindingAdapter("listData")
//fun RecyclerView.setListData(list: List<DomainStockSymbol>?){
//    val adpt = adapter as HomeRecyclerViewAdapter
//    adpt.submitList(list)
//}
//
//@BindingAdapter("imageUrl", "name")
//fun ImageView.bindImage(imgUrl: String?, name: String?){
//    imgUrl?.let {
//
//        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
//        Glide.with(context)
//                .load(imgUri)
//                .apply(RequestOptions()
//                    .placeholder(AvatarGenerator.avatarImage(context, 150, AvatarConstants.CIRCLE, name ?: ""))
//                )
//                .into(this)
//    }
//}