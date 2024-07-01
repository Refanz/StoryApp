package com.refanzzzz.storyapp

import com.refanzzzz.storyapp.data.remote.response.ListStoryItem

object DataDummy {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                id = i.toString(),
                createdAt = "createdAt + $i",
                name = "name $i",
                photoUrl = "photoUrl $i",
                description = "desc $i",
                lon = i.toString().toDouble(),
                lat = i.toString().toDouble(),
            )
            items.add(quote)
        }
        return items
    }
}
