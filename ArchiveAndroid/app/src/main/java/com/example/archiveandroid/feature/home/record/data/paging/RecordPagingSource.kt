package com.example.archiveandroid.feature.home.record.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.archiveandroid.feature.home.record.data.remote.ActivityApi
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityDto
import com.example.archiveandroid.feature.home.record.data.remote.dto.ActivityListResponse
import com.example.archiveandroid.core.network.AppError
import retrofit2.HttpException
import java.io.IOException

/**
 * Record 목록을 위한 PagingSource
 * limit/cursor 프로토콜을 Paging 3에 매핑
 */
class RecordPagingSource(
    private val api: ActivityApi
) : PagingSource<String, ActivityDto>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ActivityDto> {
        return try {
            val cursor = params.key ?: "" // 첫 로드시 빈 문자열
            val limit = params.loadSize
            
            val response = api.getActivities(
                limit = limit,
                cursor = cursor
            )
            
            if (response.isSuccessful && response.body()?.success == true) {
                val listResponse = response.body()?.data
                val data = listResponse?.activities ?: emptyList()
                val nextCursor = listResponse?.nextCursor
                
                LoadResult.Page(
                    data = data,
                    prevKey = null, // 이전 페이지는 없음 (무한 스크롤)
                    nextKey = nextCursor // 다음 커서
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

    override fun getRefreshKey(state: PagingState<String, ActivityDto>): String? {
        // 새로고침 시 첫 페이지로 이동
        return null
    }
}
