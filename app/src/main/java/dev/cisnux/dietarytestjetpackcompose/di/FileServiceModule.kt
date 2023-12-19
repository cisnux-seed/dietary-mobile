package dev.cisnux.dietarytestjetpackcompose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.cisnux.dietarytestjetpackcompose.data.locals.FileService
import dev.cisnux.dietarytestjetpackcompose.data.locals.FileServiceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FileServiceModule {

    @Singleton
    @Binds
    abstract fun bindFileServiceImpl(fileServiceImpl: FileServiceImpl): FileService
}