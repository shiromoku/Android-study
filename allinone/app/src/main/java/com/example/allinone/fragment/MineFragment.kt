package com.example.allinone.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.allinone.R

class MineFragment : Fragment() {
    private val REQUEST_LOGIN = 0x001
    companion object {
        val RESULT_LOGIN_SUCCESSFUL = 0x001
    }

    var isLogin = false
    lateinit var btnLogout: Button
    lateinit var rlUserInfo: RelativeLayout
    lateinit var ivUserAvatar: ImageView
    lateinit var tvUserName: TextView
    lateinit var spEdit: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.home_fragment,null)

        return view
    }
}