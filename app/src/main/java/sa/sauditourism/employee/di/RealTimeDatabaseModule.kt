package sa.sauditourism.employee.di

import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.extensions.isHmsAvailable
import sa.sauditourism.employee.managers.realtime.FirebaseDatabase
import sa.sauditourism.employee.managers.realtime.HuaweiDatabase
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealTimeDatabaseModule {
    @Provides
    @Singleton
    fun provideRealTimeDatabaseManager(): RealTimeDatabase =
        if(EmployeeApplication.instance.applicationContext.isHmsAvailable()){
            HuaweiDatabase
        } else {
            FirebaseDatabase()
        }

}
