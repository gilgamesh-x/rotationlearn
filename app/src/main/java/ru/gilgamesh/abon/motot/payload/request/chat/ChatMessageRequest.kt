package ru.gilgamesh.abon.motot.payload.request.chat

class ChatMessageRequest (
    val msg: String,
    val chatId: Long,
    var replayId: Long?
)