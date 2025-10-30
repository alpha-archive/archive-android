package com.alpha.archive.di

import com.alpha.archive.feature.home.chatbot.data.repository.ChatbotRepository
import com.alpha.archive.feature.home.chatbot.data.repository.ChatbotRepositoryImpl
import com.alpha.archive.feature.home.record.input.data.repository.RecordInputRepository
import com.alpha.archive.feature.home.record.input.data.repository.RecordInputRepositoryImpl
import com.alpha.archive.feature.home.recorddetail.data.repository.RecordDetailRepository
import com.alpha.archive.feature.home.recorddetail.data.repository.RecordDetailRepositoryImpl
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

    @Binds
    abstract fun bindChatbotRepository(
        impl: ChatbotRepositoryImpl
    ): ChatbotRepository

}
