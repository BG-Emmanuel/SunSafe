package com.sunsafe.app.domain.usecase

import com.sunsafe.app.domain.model.*
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Core engine that computes safe sun exposure time based on:
 * - Fitzpatrick skin type base burn time
 * - Current UV Index multiplier
 * - SPF protection factor
 * - Sunscreen effectiveness decay (time since last application)
 * - Vitamin D synthesis minimum dose
 */
class CalculateSafeExposureUseCase @Inject constructor() {

    data class Input(
        val skinType: SkinType,
        val uvIndex: Double,
        val sunscreenStatus: SunscreenStatus,
        val vitaminDDeficient: Boolean = false
    )

    operator fun invoke(input: Input): SafeExposureResult {
        val baseBurnMinutes = input.skinType.baseBurnMinutes.toDouble()
        val uvMultiplier = getUvMultiplier(input.uvIndex)
        val spfMultiplier = getSpfMultiplier(input.sunscreenStatus)
        val effectivenessMultiplier = input.sunscreenStatus.effectiveness

        val safeMinutes = baseBurnMinutes * uvMultiplier * spfMultiplier * effectivenessMultiplier
        val vitaminDMinutes = getVitaminDMinutes(input.skinType, input.uvIndex, input.vitaminDDeficient)

        val recommendations = buildRecommendations(input, safeMinutes, vitaminDMinutes)

        return SafeExposureResult(
            safeMinutes = safeMinutes.coerceAtLeast(0.0),
            vitaminDMinutes = vitaminDMinutes,
            uvMultiplier = uvMultiplier,
            spfMultiplier = spfMultiplier,
            skinBaseBurnMinutes = input.skinType.baseBurnMinutes,
            recommendations = recommendations
        )
    }

    private fun getUvMultiplier(uvIndex: Double): Double = when {
        uvIndex >= 8.0 -> 0.4
        uvIndex >= 6.0 -> 0.6
        uvIndex >= 3.0 -> 0.8
        else -> 1.0
    }

    private fun getSpfMultiplier(status: SunscreenStatus): Double {
        if (!status.applied) return 1.0
        return when {
            status.spf >= 50 -> 25.0
            status.spf >= 30 -> 15.0
            status.spf >= 15 -> 8.0
            status.spf > 0 -> status.spf / 2.0
            else -> 1.0
        }
    }

    private fun getVitaminDMinutes(
        skinType: SkinType,
        uvIndex: Double,
        isDeficient: Boolean
    ): Double {
        if (uvIndex < 3.0) return Double.MAX_VALUE  // Cannot synthesize below UV 3
        val base = when (skinType) {
            SkinType.TYPE_I -> 5.0
            SkinType.TYPE_II -> 10.0
            SkinType.TYPE_III -> 15.0
            SkinType.TYPE_IV -> 20.0
            SkinType.TYPE_V -> 30.0
            SkinType.TYPE_VI -> 40.0
        }
        val uvBonus = (1.0 - (uvIndex / 15.0)).coerceAtLeast(0.5)
        val deficiencyMultiplier = if (isDeficient) 1.5 else 1.0
        return (base * uvBonus * deficiencyMultiplier).coerceAtLeast(3.0)
    }

    private fun buildRecommendations(
        input: Input,
        safeMinutes: Double,
        vitaminDMinutes: Double
    ): List<String> {
        val recs = mutableListOf<String>()
        val severity = UvSeverity.fromIndex(input.uvIndex)

        when (severity) {
            UvSeverity.LOW -> recs.add("UV is low — generally safe without sunscreen for short periods.")
            UvSeverity.MODERATE -> recs.add("Apply SPF 30+ sunscreen before going outside.")
            UvSeverity.HIGH -> recs.add("UV is high. Use SPF 50+ sunscreen and reapply every 2 hours.")
            UvSeverity.VERY_HIGH -> recs.add("⚠️ Very high UV. Minimize exposure 10 AM–4 PM. Use SPF 50+.")
            UvSeverity.EXTREME -> recs.add("🚨 Extreme UV! Avoid outdoor exposure between 10 AM–4 PM.")
        }

        if (!input.sunscreenStatus.applied && input.uvIndex >= 3.0) {
            recs.add("Apply sunscreen now to extend your safe exposure time significantly.")
        }

        if (input.sunscreenStatus.needsReapplication) {
            recs.add("Your sunscreen needs reapplication — effectiveness has reduced.")
        }

        if (vitaminDMinutes < Double.MAX_VALUE && safeMinutes >= vitaminDMinutes) {
            val vdMins = vitaminDMinutes.roundToInt()
            recs.add("Aim for at least $vdMins minutes of exposure for vitamin D synthesis.")
        }

        if (input.vitaminDDeficient && input.uvIndex < 3.0) {
            recs.add("UV is too low for vitamin D synthesis today. Consider a supplement.")
        }

        when (input.skinType) {
            SkinType.TYPE_I, SkinType.TYPE_II ->
                recs.add("Your fair skin is highly sensitive. Seek shade frequently and wear protective clothing.")
            SkinType.TYPE_V, SkinType.TYPE_VI ->
                recs.add("Your skin has natural protection, but sunscreen is still recommended at high UV levels.")
            else -> {}
        }

        return recs
    }
}

/**
 * Calculates remaining safe exposure given elapsed time and current session.
 */
class CalculateRemainingExposureUseCase @Inject constructor(
    private val calculateSafeExposure: CalculateSafeExposureUseCase
) {
    data class Input(
        val skinType: SkinType,
        val uvIndex: Double,
        val sunscreenStatus: SunscreenStatus,
        val elapsedMinutesToday: Double,
        val vitaminDDeficient: Boolean = false
    )

    data class Result(
        val safeExposureResult: SafeExposureResult,
        val elapsedMinutes: Double,
        val remainingMinutes: Double,
        val percentageUsed: Float,
        val status: ExposureStatus
    )

    operator fun invoke(input: Input): Result {
        val safeResult = calculateSafeExposure(
            CalculateSafeExposureUseCase.Input(
                skinType = input.skinType,
                uvIndex = input.uvIndex,
                sunscreenStatus = input.sunscreenStatus,
                vitaminDDeficient = input.vitaminDDeficient
            )
        )

        val remaining = (safeResult.safeMinutes - input.elapsedMinutesToday).coerceAtLeast(0.0)
        val percentage = if (safeResult.safeMinutes > 0)
            (input.elapsedMinutesToday / safeResult.safeMinutes * 100).toFloat().coerceIn(0f, 100f)
        else 100f

        val status = when {
            percentage >= 100f -> ExposureStatus.EXCEEDED
            percentage >= 95f -> ExposureStatus.CRITICAL
            percentage >= 80f -> ExposureStatus.WARNING
            else -> ExposureStatus.SAFE
        }

        return Result(
            safeExposureResult = safeResult,
            elapsedMinutes = input.elapsedMinutesToday,
            remainingMinutes = remaining,
            percentageUsed = percentage,
            status = status
        )
    }
}
