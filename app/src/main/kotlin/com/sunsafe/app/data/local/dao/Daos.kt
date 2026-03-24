package com.sunsafe.app.data.local.dao

import androidx.room.*
import com.sunsafe.app.data.local.entity.*
import com.sunsafe.app.domain.model.DailySummary
import com.sunsafe.app.domain.model.ExposureStatus
import kotlinx.coroutines.flow.Flow

// ─────────────────────────────────────────────────────────────────────────────
// User DAO
// ─────────────────────────────────────────────────────────────────────────────
@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1")
    fun getUserProfile(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Long)
}

// ─────────────────────────────────────────────────────────────────────────────
// Exposure DAO
// ─────────────────────────────────────────────────────────────────────────────
@Dao
interface ExposureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ExposureEntity): Long

    @Update
    suspend fun updateSession(session: ExposureEntity)

    @Query("DELETE FROM exposure_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)

    @Query("SELECT * FROM exposure_sessions WHERE endTime IS NULL LIMIT 1")
    suspend fun getActiveSession(): ExposureEntity?

    @Query("SELECT * FROM exposure_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<ExposureEntity>>

    @Query("SELECT * FROM exposure_sessions WHERE startTime >= :startTime AND startTime <= :endTime ORDER BY startTime DESC")
    fun getSessionsInRange(startTime: Long, endTime: Long): Flow<List<ExposureEntity>>

    @Query("""
        SELECT * FROM exposure_sessions 
        WHERE startTime >= :dayStart AND startTime <= :dayEnd
        ORDER BY startTime ASC
    """)
    suspend fun getSessionsForDay(dayStart: Long, dayEnd: Long): List<ExposureEntity>

    @Query("""
        SELECT 
            (startTime / 86400000) * 86400000 as date,
            SUM(durationMinutes) as totalMinutes,
            MAX(uvIndex) as peakUv,
            SUM(CASE WHEN sunscreenApplied THEN 1 ELSE 0 END) as sunscreenCount,
            MAX(CASE WHEN burned THEN 1 ELSE 0 END) as wasBurned,
            COUNT(*) as sessionsCount
        FROM exposure_sessions
        WHERE startTime >= :since
        GROUP BY (startTime / 86400000)
        ORDER BY date DESC
    """)
    fun getDailyAggregates(since: Long): Flow<List<DailyAggregateRow>>

    @Query("""
        SELECT COALESCE(SUM(durationMinutes), 0) 
        FROM exposure_sessions 
        WHERE startTime >= :dayStart AND startTime <= :dayEnd
    """)
    suspend fun getTodayTotalMinutes(dayStart: Long, dayEnd: Long): Double

    @Query("UPDATE exposure_sessions SET burned = 1 WHERE id = :id")
    suspend fun markAsBurned(id: Long)

    @Query("""
        UPDATE exposure_sessions 
        SET endTime = :endTime, avgLux = :avgLux, maxLux = :maxLux, durationMinutes = :duration
        WHERE id = :id
    """)
    suspend fun closeSession(id: Long, endTime: Long, avgLux: Float, maxLux: Float, duration: Double)
}

data class DailyAggregateRow(
    val date: Long,
    val totalMinutes: Double,
    val peakUv: Double,
    val sunscreenCount: Int,
    val wasBurned: Boolean,
    val sessionsCount: Int
)

// ─────────────────────────────────────────────────────────────────────────────
// Skin Photo DAO
// ─────────────────────────────────────────────────────────────────────────────
@Dao
interface SkinPhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhoto(photo: SkinPhotoEntity): Long

    @Query("SELECT * FROM skin_photos WHERE userId = :userId ORDER BY timestamp DESC")
    fun getPhotosForUser(userId: Long): Flow<List<SkinPhotoEntity>>

    @Query("DELETE FROM skin_photos WHERE id = :id")
    suspend fun deletePhoto(id: Long)
}
