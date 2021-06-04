package com.example.allinone.entity

import org.json.JSONObject

class Page() {
    var pageType = ""
    var pageTitle = ""
    var pageIntro = ""
    var imageUrl = ""
    var pageUrl = ""

    constructor(json: JSONObject) : this() {
        pageType = json.getString("pageType")
        pageTitle = json.getString("title")
        pageIntro = json.getString("intro")
        imageUrl = json.getString("imageUrl")
        pageUrl = json.getString("pageUrl")
    }

    override fun toString(): String {
        return "Page(pageType='$pageType', pageTitle='$pageTitle', pageIntro='$pageIntro', imageUrl='$imageUrl', pageUrl='$pageUrl')"
    }


}