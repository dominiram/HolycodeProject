package app.naum.myapplication.di

import app.naum.myapplication.networking.SearchUserNetworkMapper
import app.naum.myapplication.networking.SearchUserService
import app.naum.myapplication.repository.SearchUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideSearchUserRepository(
        service: SearchUserService,
        mapper: SearchUserNetworkMapper
    ): SearchUserRepository{
        return SearchUserRepository(
            service,
            mapper
        )
    }
}
