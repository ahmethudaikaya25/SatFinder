package com.ahk.satfinder.core.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "satellite_detail")
@Parcelize
data class SatelliteDetail(
    @PrimaryKey val id: Int? = 0,
    @ColumnInfo(name = "name") var name: String? = "",
    @ColumnInfo(name = "cost_per_launch") @SerializedName("cost_per_launch") val costPerLaunch: Long? = 0,
    @ColumnInfo(name = "first_flight") @SerializedName("first_flight") val firstFlight: String? = "",
    @ColumnInfo(name = "height") val height: Int? = 0,
    @ColumnInfo(name = "mass") val mass: Int? = 0,
) : Parcelable {
    fun costPerLaunchAsString(): String {
        return costPerLaunch.toString()
            .reversed()
            .chunked(3)
            .joinToString(".")
            .reversed()
    }
}
