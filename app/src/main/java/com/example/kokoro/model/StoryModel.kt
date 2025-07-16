package com.example.kokoro.model

import android.os.Parcel
import android.os.Parcelable
data class StoryModel(
    var storyId: String = "",
    var storyTitle: String = "",
    var storyDesc: String = "",
    var imageUrl: String = "",
    var timestamp: Long = System.currentTimeMillis()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong() // Read timestamp from parcel
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(storyId)
        parcel.writeString(storyTitle)
        parcel.writeString(storyDesc)
        parcel.writeString(imageUrl)
        parcel.writeLong(timestamp) // Write timestamp to parcel
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoryModel> {
        override fun createFromParcel(parcel: Parcel): StoryModel {
            return StoryModel(parcel)
        }

        override fun newArray(size: Int): Array<StoryModel?> {
            return arrayOfNulls(size)
        }
    }
}
