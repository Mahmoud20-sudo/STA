package sa.sauditourism.employee.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sa.sauditourism.employee.managers.errorhandeling.ErrorHandlingManager
import sa.sauditourism.employee.network.status.NetworkConnectivityService
import sa.sauditourism.employee.network.status.NetworkConnectivityServiceImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ManagersModule {
    @Provides
    @Singleton
    fun provideErrorHandlingManager(): ErrorHandlingManager =
        ErrorHandlingManager()

    @Provides
    @Singleton
    fun provideNetworkConnectivityService(@ApplicationContext context: Context): NetworkConnectivityService =
        NetworkConnectivityServiceImpl(context)
}
