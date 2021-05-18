package com.example.allinone.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.allinone.R
import com.example.allinone.entity.Page

class MainAdapter(val context:Context, val pageList:MutableList<Page>): BaseAdapter() {
    override fun getCount(): Int {
        return  pageList.size
    }

    override fun getItem(position: Int): Any {
        return pageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        if(pageList.isNotEmpty()) {
            val page = pageList[position]
            val view = View.inflate(context, R.layout.layout_page, null)
            val tvTitle = view.findViewById<TextView>(R.id.tv_title)
            val tvIntro = view.findViewById<TextView>(R.id.tv_intro)
            tvTitle.text = page.pageTitle
            tvIntro.text = page.pageIntro
            return view
//        }else{
//            val view = View.inflate(context, R.layout.layout_page, null)
//            return view
//        }
    }
}