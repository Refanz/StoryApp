package com.refanzzzz.storyapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var name: String? = "",
    var desc: String? = "",
    var imgUrl: String? = "",
    var createdAt: String? = ""
) : Parcelable