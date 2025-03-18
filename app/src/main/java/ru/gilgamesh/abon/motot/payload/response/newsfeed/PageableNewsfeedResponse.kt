package ru.gilgamesh.abon.motot.payload.response.newsfeed

import ru.gilgamesh.abon.motot.payload.response.PageResponse

class PageableNewsfeedResponse : PageResponse() {
    var content: List<NewsResponse>? = null
}
