package com.sunsafe.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sunsafe.app.domain.model.*

// ─────────────────────────────────────────────────────────────────────────────
// User Entity
// ─────────────────────────────────────────────────────────────────────────────
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val skinTypeOrdinal: Int,
    val defaultSPF: Int,
    val vitaminDDeficient: Boolean,
    val age: Int,
    val name: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): UserProfile = UserProfile(
        id = id,
        skinType = SkinType.fromOrdinal(skinTypeOrdinal),
        defaultSPF = defaultSPF,
        vitaminDDeficient = vitaminDDeficient,
        age = age,
        name = name,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(profile: UserProfile) = UserEntity(
            id = profile.id,
            skinTypeOrdinal = profile.skinType.ordinal,
            defaultSPF = profile.defaultSPF,
            vitaminDDeficient = profile.vitaminDDeficient,
            age = profile.age,
            name = profile.name,
            createdAt = profile.createdAt
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Exposure Session Entity
// ─────────────────────────────────────────────────────────────────────────────
@Entity(
    tableName = "exposure_sessions",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("startTime")]
)
data class ExposureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val startTime: Long,
    val endTime: Long?,
    val durationMinutes: Double,
    val avgLux: Float,
    val maxLux: Float,
    val uvIndex: Double,
    val latitude: Double?,
    val longitude: Double?,
    val locationName: String,
    val sunscreenApplied: Boolean,
    val sunscreenSPF: Int,
    val burned: Boolean,
    val notes: String,
    val synced: Boolean = false
) {
    fun toDomain(): ExposureSession = ExposureSession(
        id = id,
        userId = userId,
        startTime = startTime,
        endTime = endTime,
        durationMinutes = durationMinutes,
        avgLux = avgLux,
        maxLux = maxLux,
        uvIndex = uvIndex,
        latitude = latitude,
        longitude = longitude,
        locationName = locationName,
        sunscreenApplied = sunscreenApplied,
        sunscreenSPF = sunscreenSPF,
        burned = burned,
        notes = notes,
        synced = synced
    )

    companion object {
        fun fromDomain(session: ExposureSession) = ExposureEntity(
            id = session.id,
            userId = session.userId,
            startTime = session.startTime,
            endTime = session.endTime,
            durationMinutes = session.durationMinutes,
            avgLux = session.avgLux,
            maxLux = session.maxLux,
            uvIndex = session.uvIndex,
            latitude = session.latitude,
            longitude = session.longitude,
            locationName = session.locationName,
            sunscreenApplied = session.sunscreenApplied,
            sunscreenSPF = session.sunscreenSPF,
            burned = session.burned,
            notes = session.notes,
            synced = session.synced
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Skin Photo Entity
// ─────────────────────────────────────────────────────────────────────────────
@Entity(
    tableName = "skin_photos",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class SkinPhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val timestamp: Long = System.currentTimeMillis(),
    val imagePath: String,
    val bodyPart: String,
    val note: String
) {
    fun toDomain() = SkinPhoto(
        id = id, userId = userId, timestamp = timestamp,
        imagePath = imagePath, bodyPart = bodyPart, note = note
    )

    companion object {
        fun fromDomain(p: SkinPhoto) = SkinPhotoEntity(
            id = p.id, userId = p.userId, timestamp = p.timestamp,
            imagePath = p.imagePath, bodyPart = p.bodyPart, note = p.note
        )
    }
}
