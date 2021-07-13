package com.mas.model

import com.google.gson.annotations.SerializedName

class Meanings(
    @field:SerializedName("translation") val translation: Translation?,
    @field:SerializedName("transcription") val transcription: String?,
    @field:SerializedName("previewUrl") val previewUrl: String?,
    @field:SerializedName("imageUrl") val imageUrl: String?
)
