package com.example.archiveandroid.di

import com.example.archiveandroid.feature.record.data.repository.ImageRepository
import com.example.archiveandroid.feature.record.data.repository.ImageRepositoryImpl
import com.example.archiveandroid.feature.record.data.repository.RecordRepository
import com.example.archiveandroid.feature.record.data.repository.RecordRepositoryImpl
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
    abstract fun bindImageRepository(
        impl: ImageRepositoryImpl
    ): ImageRepository

    @Binds
    abstract fun bindRecordRepository(
        impl: RecordRepositoryImpl
    ): RecordRepository
}
