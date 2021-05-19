package com.example.allinone.entity

import org.json.JSONObject

class Page() {
    var pageTitle = ""
    var pageIntro = ""
    var imageUrl = ""
    constructor(json:JSONObject):this(){
        pageTitle = json.getString("title")
        pageIntro = json.getString("intro")
        imageUrl = json.getString("imageUrl")
    }

    override fun toString(): String {
        return "Page(pageTitle='$pageTitle', pageIntro='$pageIntro', imageUrl='$imageUrl')"
    }


}