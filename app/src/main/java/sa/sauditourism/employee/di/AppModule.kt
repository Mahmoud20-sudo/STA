package sa.sauditourism.employee.di

import android.content.Context
import sa.sauditourism.employee.EmployeeApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: EmployeeApplication): Context {
        return application.applicationContext
    }


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): EmployeeApplication {
        return app as EmployeeApplication
    }

}
