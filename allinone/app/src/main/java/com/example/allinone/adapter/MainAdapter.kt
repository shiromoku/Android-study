package com.example.allinone.adapter

import android.content.Context
import android.util.Log
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

class MainAdapter(private val context: Context?, private val pageList: MutableList<Page>) :
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
        var view: View
        val page = pageList[position]

        when (page.pageType) {
            "page" -> {
                val holder: PageViewHolder
                if (convertView == null) {
                    view = LayoutInflater.from(context).inflate(R.layout.layout_page, parent, false)
                    val ivImage = view.findViewById<ImageView>(R.id.iv_image)
                    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                    val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
                    holder = PageViewHolder(ivImage, tvTitle, tvIntro)
                    view.tag = holder
                } else {
                    view = convertView
                    if (view.tag is PageViewHolder) {
                        holder = view.tag as PageViewHolder
                    }else{
                        view = LayoutInflater.from(context).inflate(R.layout.layout_page, parent, false)
                        val ivImage = view.findViewById<ImageView>(R.id.iv_image)
                        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                        val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
                        holder = PageViewHolder(ivImage, tvTitle, tvIntro)
                        view.tag = holder
                    }
                }

                holder.tvTitle.text = page.pageTitle
                holder.tvIntro.text = page.pageIntro
                Glide.with(view)
                    .load(page.imageUrl)
                    //            .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivImage)
            }
            "text" -> {
                val holder: TextPageViewHolder
                if (convertView == null) {
                    view = LayoutInflater.from(context)
                        .inflate(R.layout.layout_text_page, parent, false)
                    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                    val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
                    holder = TextPageViewHolder(tvTitle, tvIntro)
                    view.tag = holder
                } else {
                    view = convertView
                    if(view.tag is TextPageViewHolder){
                        holder = view.tag as TextPageViewHolder
                    }else{
                        view = LayoutInflater.from(context)
                            .inflate(R.layout.layout_text_page, parent, false)
                        val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                        val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
                        holder = TextPageViewHolder(tvTitle, tvIntro)
                        view.tag = holder
                    }
                }

                holder.tvTitle.text = page.pageTitle
                holder.tvIntro.text = page.pageIntro
            }
            else -> {
                val holder: TextPageViewHolder
                if (convertView == null) {
                    view = LayoutInflater.from(context)
                        .inflate(R.layout.layout_text_page, parent, false)
                    val tvTitle = view.findViewById<TextView>(R.id.tv_title)
                    val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
                    holder = TextPageViewHolder(tvTitle, tvIntro)
                    view.tag = holder
                } else {
                    view = convertView
                    holder = view.tag as TextPageViewHolder
                }

                holder.tvTitle.text = "该新闻存在数据错误,请联系管理员"
                holder.tvIntro.text = "该新闻存在数据错误,请联系管理员"
            }

        }
        return view
    }


    inner class PageViewHolder(
        val ivImage: ImageView,
        val tvTitle: TextView,
        val tvIntro: TextView
    )

    inner class TextPageViewHolder(
        val tvTitle: TextView,
        val tvIntro: TextView
    )
}
/*

    open inner class PageViewHolder(open val ivImage: ImageView?, val tvTitle: TextView, val tvIntro: TextView)

    inner class TextPageViewHolder( tvTitle: TextView, tvIntro: TextView):PageViewHolder(null,tvTitle,tvIntro)


inner class PageViewHolder(
        val ivImage: ImageView,
        val tvTitle: TextView,
        val tvIntro: TextView
    )

    inner class TextPageViewHolder(
        val tvTitle: TextView,
        val tvIntro: TextView
    )
 */