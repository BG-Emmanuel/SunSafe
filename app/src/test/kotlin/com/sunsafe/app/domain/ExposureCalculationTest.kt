package com.sunsafe.app.domain

import com.sunsafe.app.domain.model.*
import com.sunsafe.app.domain.usecase.CalculateRemainingExposureUseCase
import com.sunsafe.app.domain.usecase.CalculateSafeExposureUseCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExposureCalculationTest {

    private lateinit var calculateSafeExposure: CalculateSafeExposureUseCase
    private lateinit var calculateRemaining: CalculateRemainingExposureUseCase

    @Before
    fun setup() {
        calculateSafeExposure = CalculateSafeExposureUseCase()
        calculateRemaining = CalculateRemainingExposureUseCase(calculateSafeExposure)
    }

    // ─── Safe exposure baseline tests ──────────────────────────────────────

    @Test
    fun `type I skin, UV 8+, no sunscreen burns in 4 minutes`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_I,
            uvIndex = 10.0,
            sunscreenStatus = noSunscreen()
        ))
        // 10min * 0.4 (UV 8+) * 1.0 (no SPF) * 1.0 (effectiveness) = 4.0
        assertEquals(4.0, result.safeMinutes, 0.01)
    }

    @Test
    fun `type I skin with SPF 50 extends time 25x`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_I,
            uvIndex = 10.0,
            sunscreenStatus = freshSunscreen(50)
        ))
        // 10 * 0.4 * 25.0 = 100 min
        assertEquals(100.0, result.safeMinutes, 0.01)
    }

    @Test
    fun `type VI skin at low UV 2 baseline`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_VI,
            uvIndex = 2.0,
            sunscreenStatus = noSunscreen()
        ))
        // 120 * 1.0 * 1.0 = 120 min
        assertEquals(120.0, result.safeMinutes, 0.01)
    }

    @Test
    fun `SPF 30 gives 15x multiplier`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_II,
            uvIndex = 5.0,
            sunscreenStatus = freshSunscreen(30)
        ))
        // 20 * 0.8 * 15 = 240
        assertEquals(240.0, result.safeMinutes, 0.01)
    }

    @Test
    fun `SPF 15 gives 8x multiplier`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_III,
            uvIndex = 5.0,
            sunscreenStatus = freshSunscreen(15)
        ))
        // 30 * 0.8 * 8.0 = 192
        assertEquals(192.0, result.safeMinutes, 0.01)
    }

    @Test
    fun `sunscreen after 80 minutes has 70 percent effectiveness`() {
        val appliedAt = System.currentTimeMillis() - (90 * 60_000L)
        val status = SunscreenStatus(applied = true, spf = 30, appliedAt = appliedAt, minutesSinceApplication = 90L)
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_III,
            uvIndex = 5.0,
            sunscreenStatus = status
        ))
        // 30 * 0.8 * 15 * 0.7 = 252 * 0.7 = 134.4... actually 192 * 0.7 = 134.4
        assertEquals(134.4, result.safeMinutes, 0.5)
    }

    @Test
    fun `sunscreen after 120 minutes has 50 percent effectiveness`() {
        val appliedAt = System.currentTimeMillis() - (130 * 60_000L)
        val status = SunscreenStatus(applied = true, spf = 30, appliedAt = appliedAt, minutesSinceApplication = 130L)
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(
            skinType = SkinType.TYPE_III,
            uvIndex = 5.0,
            sunscreenStatus = status
        ))
        // 30 * 0.8 * 15 * 0.5 = 96
        assertEquals(96.0, result.safeMinutes, 0.5)
    }

    // ─── UV multiplier boundaries ─────────────────────────────────────────

    @Test
    fun `UV below 3 has no multiplier penalty`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(SkinType.TYPE_III, 1.0, noSunscreen()))
        assertEquals(1.0, result.uvMultiplier, 0.001)
    }

    @Test
    fun `UV 6 to 7 has 0_6 multiplier`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(SkinType.TYPE_III, 7.0, noSunscreen()))
        assertEquals(0.6, result.uvMultiplier, 0.001)
    }

    @Test
    fun `UV 11 is extreme and has 0_4 multiplier`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(SkinType.TYPE_III, 11.0, noSunscreen()))
        assertEquals(0.4, result.uvMultiplier, 0.001)
    }

    // ─── Remaining exposure ───────────────────────────────────────────────

    @Test
    fun `exceeded status when elapsed exceeds safe limit`() {
        val result = calculateRemaining(CalculateRemainingExposureUseCase.Input(
            skinType = SkinType.TYPE_I,
            uvIndex = 10.0,
            sunscreenStatus = noSunscreen(),
            elapsedMinutesToday = 100.0
        ))
        assertEquals(ExposureStatus.EXCEEDED, result.status)
        assertEquals(0.0, result.remainingMinutes, 0.001)
    }

    @Test
    fun `warning status at 80 percent`() {
        // Safe = 4 min (type I, UV 10), 80% = 3.2 min elapsed
        val result = calculateRemaining(CalculateRemainingExposureUseCase.Input(
            skinType = SkinType.TYPE_I,
            uvIndex = 10.0,
            sunscreenStatus = noSunscreen(),
            elapsedMinutesToday = 3.5
        ))
        assertEquals(ExposureStatus.WARNING, result.status)
    }

    @Test
    fun `safe status when well under limit`() {
        val result = calculateRemaining(CalculateRemainingExposureUseCase.Input(
            skinType = SkinType.TYPE_III,
            uvIndex = 5.0,
            sunscreenStatus = noSunscreen(),
            elapsedMinutesToday = 5.0
        ))
        assertEquals(ExposureStatus.SAFE, result.status)
    }

    @Test
    fun `recommendations are not empty for high UV`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(SkinType.TYPE_I, 10.0, noSunscreen()))
        assertTrue(result.recommendations.isNotEmpty())
    }

    @Test
    fun `vitamin D synthesis unavailable below UV 3`() {
        val result = calculateSafeExposure(CalculateSafeExposureUseCase.Input(SkinType.TYPE_III, 2.0, noSunscreen()))
        assertEquals(Double.MAX_VALUE, result.vitaminDMinutes, 0.0)
    }

    // ─── Helpers ────────────────────────────────────────────────────────────

    private fun noSunscreen() = SunscreenStatus(applied = false, spf = 0, appliedAt = null, minutesSinceApplication = null)
    private fun freshSunscreen(spf: Int) = SunscreenStatus(applied = true, spf = spf, appliedAt = System.currentTimeMillis(), minutesSinceApplication = 5L)
}

// ─────────────────────────────────────────────────────────────────────────────
// Sunscreen Status Model Tests
// ─────────────────────────────────────────────────────────────────────────────
class SunscreenStatusTest {

    @Test
    fun `no sunscreen needs no reapplication`() {
        val status = SunscreenStatus(false, 0, null, null)
        assertFalse(status.needsReapplication)
    }

    @Test
    fun `sunscreen after 90 minutes does not yet need reapplication`() {
        val status = SunscreenStatus(true, 30, System.currentTimeMillis() - 90 * 60_000L, 90L)
        assertFalse(status.needsReapplication)
    }

    @Test
    fun `sunscreen after 121 minutes needs reapplication`() {
        val status = SunscreenStatus(true, 30, System.currentTimeMillis() - 130 * 60_000L, 130L)
        assertTrue(status.needsReapplication)
    }

    @Test
    fun `unapplied sunscreen has effectiveness 1`() {
        val status = SunscreenStatus(false, 0, null, null)
        assertEquals(1.0, status.effectiveness, 0.001)
    }

    @Test
    fun `fresh sunscreen has full effectiveness`() {
        val status = SunscreenStatus(true, 50, System.currentTimeMillis(), 0L)
        assertEquals(1.0, status.effectiveness, 0.001)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// UV Severity Tests
// ─────────────────────────────────────────────────────────────────────────────
class UvSeverityTest {

    @Test fun `uv 0 is low`()       { assertEquals(UvSeverity.LOW, UvSeverity.fromIndex(0.0)) }
    @Test fun `uv 2_9 is low`()     { assertEquals(UvSeverity.LOW, UvSeverity.fromIndex(2.9)) }
    @Test fun `uv 3 is moderate`()  { assertEquals(UvSeverity.MODERATE, UvSeverity.fromIndex(3.0)) }
    @Test fun `uv 6 is high`()      { assertEquals(UvSeverity.HIGH, UvSeverity.fromIndex(6.0)) }
    @Test fun `uv 8 is very high`() { assertEquals(UvSeverity.VERY_HIGH, UvSeverity.fromIndex(8.0)) }
    @Test fun `uv 11 is extreme`()  { assertEquals(UvSeverity.EXTREME, UvSeverity.fromIndex(11.0)) }
    @Test fun `uv 15 is extreme`()  { assertEquals(UvSeverity.EXTREME, UvSeverity.fromIndex(15.0)) }
}
