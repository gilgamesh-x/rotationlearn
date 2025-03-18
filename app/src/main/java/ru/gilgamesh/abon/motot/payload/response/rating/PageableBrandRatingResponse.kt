package ru.gilgamesh.abon.motot.payload.response.rating

import ru.gilgamesh.abon.motot.payload.response.PageResponse

class PageableBrandRatingResponse (val content: List<BrandRatingResponse>) : PageResponse()