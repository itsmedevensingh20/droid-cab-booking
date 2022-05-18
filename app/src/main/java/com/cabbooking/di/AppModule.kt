package com.cabbooking.di

/**Created by Devendra singh **/
import android.content.Context
import com.cabbooking.retrofit.ApiDataSource
import com.cabbooking.retrofit.ServicesInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class) // for hilt
object AppModule {

    @Provides
    fun provideAuthApi(
        @ApplicationContext context: Context,
        apiDataSource: ApiDataSource
    ): ServicesInterface {
        return apiDataSource.create()
    }

}