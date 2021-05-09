package app.naum.myapplication.di

import app.naum.myapplication.networking.SearchUserNetworkMapper
import app.naum.myapplication.networking.UserService
import app.naum.myapplication.repository.UserRepository
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
        service: UserService,
        mapper: SearchUserNetworkMapper
    ): UserRepository{
        return UserRepository(
            service,
            mapper
        )
    }
}
