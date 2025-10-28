package com.alpha.archiveandroid.feature.home.record.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alpha.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.alpha.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.alpha.archiveandroid.core.network.AppError
import retrofit2.HttpException
import java.io.IOException

/**
 * Record 목록을 위한 PagingSource
 * 단순한 목록 API를 Paging 3에 매핑
 */
class RecordPagingSource(
    private val api: ActivityApi
) : PagingSource<Int, ActivityDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ActivityDto> {
        return try {
            val response = api.getActivities()
            
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data ?: emptyList()
                
                LoadResult.Page(
                    data = data,
                    prevKey = null, // 이전 페이지는 없음
                    nextKey = null // 모든 데이터를 한 번에 로드
                )
            } else {
                LoadResult.Error(
                    AppError.Network("데이터를 불러올 수 없습니다")
                )
            }
        } catch (e: IOException) {
            LoadResult.Error(
                AppError.Network("네트워크 연결을 확인해주세요", e)
            )
        } catch (e: HttpException) {
            LoadResult.Error(
                AppError.Server("서버 오류: ${e.code()}")
            )
        } catch (e: Exception) {
            LoadResult.Error(
                AppError.Unknown("알 수 없는 오류가 발생했습니다", e)
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ActivityDto>): Int? {
        // 새로고침 시 첫 페이지로 이동
        return null
    }
}
