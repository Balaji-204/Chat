package com.example.data.model

enum class MessageType {
    TEXT,
    PHOTO,
    AUDIO_NOTE,
    HEARTBEAT_TAP,
    PROMPT_ANSWER,
    LOVE_STICKER
}

data class ChatMessage(
    val id: String,
    val senderId: String, // "user" or "partner"
    val senderName: String,
    val text: String,
    val timestamp: Long,
    val type: MessageType = MessageType.TEXT,
    val reactionEmoji: String? = null,
    val mediaUrl: String? = null,
    val audioDurationSeconds: Int = 0,
    val isDelivered: Boolean = true,
    val isRead: Boolean = true
)
