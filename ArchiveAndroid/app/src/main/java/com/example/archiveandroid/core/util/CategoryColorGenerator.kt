package com.example.archiveandroid.core.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs

/**
 * 카테고리 이름을 기반으로 일관된 색상을 생성하는 유틸리티
 */
object CategoryColorGenerator {
    
    // 골든 앵글 (황금각) - 색상을 균등하게 분산시키기 위한 비율
    private const val GOLDEN_RATIO_CONJUGATE = 0.618033988749895
    
    /**
     * 카테고리 이름을 seed로 사용하여 배경색과 전경색을 생성
     * 같은 카테고리 이름은 항상 같은 색상을 반환
     */
    fun getCategoryColors(category: String): Pair<Color, Color> {
        if (category.isBlank()) {
            return Pair(Color(0xFFE0E0E0), Color(0xFF666666))
        }
        
        // 카테고리 이름을 시드로 사용해서 일관된 랜덤 색상 생성
        val hash = category.hashCode()
        
        // 골든 앵글을 사용하여 Hue를 균등하게 분산 (0-360도 범위)
        // 이렇게 하면 연속된 해시값들도 색상환에서 멀리 떨어진 색상을 가짐
        val hueBase = (abs(hash) * GOLDEN_RATIO_CONJUGATE) % 1.0
        val hue = (hueBase * 360).toFloat()
        
        // Saturation (채도): 50-85% 범위로 다양화
        val saturationSeed = abs(hash shr 8)
        val saturation = 0.50f + (saturationSeed % 36) / 100f
        
        // 배경색: 높은 명도 (85-95%)
        val bgValueSeed = abs(hash shr 16)
        val bgValue = 0.85f + (bgValueSeed % 11) / 100f
        val bgColor = Color.hsv(hue, saturation * 0.4f, bgValue)
        
        // 전경색: 낮은 명도 (30-60%) - 배경색과 명확한 대비
        val fgValueSeed = abs(hash shr 24)
        val fgValue = 0.30f + (fgValueSeed % 31) / 100f
        val fgColor = Color.hsv(hue, saturation, fgValue)
        
        return Pair(bgColor, fgColor)
    }
}

