package ru.gilgamesh.abon.motot.payload.response.rating

import ru.gilgamesh.abon.motot.payload.response.PageResponse

class PageableBrandModelRatingResponse (val content: List<BrandModelRatingResponse>) : PageResponse()