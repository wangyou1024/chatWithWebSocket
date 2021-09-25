package com.wangyou.chatwithwebsocket.adapter

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.wangyou.chatwithwebsocket.conf.Const
import com.wangyou.chatwithwebsocket.util.DateTimeUtil

object ImageViewBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["image", "placeholder"], requireAll = false)
    fun setImage(imageView: ImageView?, url: String?, placeholder: Int) {
        Glide.with(imageView!!.context)
            .load("${Const.imageUrl}${url}")
             .skipMemoryCache(true)
             .diskCacheStrategy(DiskCacheStrategy.NONE)
            // 默认变形为圆需要单独处理
            .thumbnail(
                Glide.with(imageView.context)
                    .load(placeholder)
                    .apply(
                        RequestOptions()
                            .transform(CircleCrop())
                    )
            )
            // 正常加载的图片变形为圆
            .apply(
                RequestOptions()
                    .transform(CircleCrop())
            )
            .into(imageView)
    }
}