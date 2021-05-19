package com.example.allinone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.allinone.R
import com.example.allinone.entity.Page

class MainAdapter(private val context: Context, private val pageList: MutableList<Page>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return pageList.size
    }

    override fun getItem(position: Int): Any {
        return pageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //    @SuppressLint("ResourceType", "ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view : View
        val holder:PageViewHolder
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_page,parent,false)
            val ivImage = view.findViewById<ImageView>(R.id.iv_image)
            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
            holder = PageViewHolder(ivImage,tvTitle,tvIntro)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as PageViewHolder
        }
        val page = pageList[position]
        holder.tvTitle.text = page.pageTitle
        holder.tvIntro.text = page.pageIntro
//        holder.ivImage.let {
//            Glide.with(view)
//                .load(page.imageUrl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(it)
//        }
        Glide.with(view)
            .load(page.imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.ivImage)
        return view
    }

    inner class PageViewHolder(val ivImage: ImageView,
                               val tvTitle: TextView,
                               val tvIntro: TextView ) {
    }
}