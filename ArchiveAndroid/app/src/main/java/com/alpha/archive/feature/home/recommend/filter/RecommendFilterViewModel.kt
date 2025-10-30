package com.alpha.archive.feature.home.recommend.filter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecommendFilterViewModel @Inject constructor() : ViewModel() {
    
    // 필터 상태 관리는 RecommendFilterSheet에서 직접 처리
    // 필요시 여기에 비즈니스 로직 추가 가능
}
