package com.example.archiveandroid.di

import com.example.archiveandroid.feature.home.recorddetail.data.repository.RecordDetailRepository
import com.example.archiveandroid.feature.home.recorddetail.data.repository.RecordDetailRepositoryImpl
import com.example.archiveandroid.feature.home.record.input.data.repository.RecordInputRepository
import com.example.archiveandroid.feature.home.record.input.data.repository.RecordInputRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 인터페이스와 구현체 매핑
 * Repository(interface) <-> RepositoryImpl(구현체)
 */


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {


    @Binds
    abstract fun bindRecordRepository(
        impl: RecordDetailRepositoryImpl
    ): RecordDetailRepository

    @Binds
    abstract fun bindRecordInputRepository(
        impl: RecordInputRepositoryImpl
    ): RecordInputRepository
}
