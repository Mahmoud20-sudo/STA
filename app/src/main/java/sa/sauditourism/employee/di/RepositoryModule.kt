package sa.sauditourism.employee.di

import sa.sauditourism.employee.modules.login.domain.SSIDRepository
import sa.sauditourism.employee.modules.login.domain.SSIDRepositoryImpl
import sa.sauditourism.employee.network.remote.SSIDEndPoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sa.sauditourism.employee.modules.services.datasource.remote.RequestFormEndpoints
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.modules.services.datasource.remote.RequestTypeEndPoints
import sa.sauditourism.employee.modules.account.datasource.AccountEndPoints
import sa.sauditourism.employee.modules.account.datasource.FiltersEndPoints
import sa.sauditourism.employee.modules.account.datasource.PaySlipsEndPoints
import sa.sauditourism.employee.modules.account.datasource.ProfileImageEndPoints
import sa.sauditourism.employee.modules.account.domain.AccountRepository
import sa.sauditourism.employee.modules.account.domain.AccountRepositoryImpl
import sa.sauditourism.employee.modules.account.domain.FiltersRepository
import sa.sauditourism.employee.modules.account.domain.FiltersRepositoryImpl
import sa.sauditourism.employee.modules.account.domain.PaySlipsRepository
import sa.sauditourism.employee.modules.account.domain.PaySlipsRepositoryImpl
import sa.sauditourism.employee.modules.account.domain.ProfileImageRepository
import sa.sauditourism.employee.modules.account.domain.ProfileImageRepositoryImpl
import sa.sauditourism.employee.modules.activities.datasource.remote.ActivitiesEndpoints
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepositoryImpl
import sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource.AvailableMeetingRoomsEndPoints
import sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource.BookMeetingRoomEndPoints
import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.AvailableMeetingRoomsRepository
import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.AvailableMeetingRoomsRepositoryImpl
import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.BookMeetingRoomRepository
import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.BookMeetingRoomRepositoryImpl
import sa.sauditourism.employee.modules.home.myDayMeetings.datasource.remote.MyMeetingsEndPoints
import sa.sauditourism.employee.modules.home.myDayMeetings.domain.MyDayMeetingsRepository
import sa.sauditourism.employee.modules.home.myDayMeetings.domain.MyDayMeetingsRepositoryImpl
import sa.sauditourism.employee.modules.home.datasource.remote.HomeEndpoints
import sa.sauditourism.employee.modules.home.domain.HomeRepository
import sa.sauditourism.employee.modules.home.domain.HomeRepositoryImpl
import sa.sauditourism.employee.modules.login.datasource.ProfileEndPoints
import sa.sauditourism.employee.modules.login.domain.ProfileRepository
import sa.sauditourism.employee.modules.login.domain.ProfileRepositoryImpl
import sa.sauditourism.employee.modules.services.datasource.remote.ParticipantsEndpoints
import sa.sauditourism.employee.modules.services.datasource.remote.RequestDetailsEndpoints
import sa.sauditourism.employee.modules.services.datasource.remote.ServicesEndpoints
import sa.sauditourism.employee.modules.services.domain.ParticipantsRepositoryImpl
import sa.sauditourism.employee.modules.services.domain.ParticipantsRepository
import sa.sauditourism.employee.modules.services.domain.RequestDetailsRepository
import sa.sauditourism.employee.modules.services.domain.RequestDetailsRepositoryImpl
import sa.sauditourism.employee.modules.services.domain.RequestFormRepository
import sa.sauditourism.employee.modules.services.domain.RequestFormRepositoryImpl
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepository
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepositoryImpl
import sa.sauditourism.employee.modules.services.domain.ServicesRepository
import sa.sauditourism.employee.modules.services.domain.ServicesRepositoryImpl
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSSIDRepository(
        endpoints: SSIDEndPoints,
    ): SSIDRepository =
        SSIDRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideServicesRepository(
        endpoints: ServicesEndpoints,
    ): ServicesRepository =
        ServicesRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideRequestFormRepository(
        endpoints: RequestFormEndpoints,
    ): RequestFormRepository =
        RequestFormRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideRequestTypeRepository(endpoints: RequestTypeEndPoints): RequestTypeRepository =
        RequestTypeRepositoryImpl(
            endpoints, SharedPreferencesModule.provideSharedPreferencesManager(
                EmployeeApplication.instance.applicationContext
            )
        )

    @Provides
    @Singleton
    fun provideAccountRepository(
        endpoints: AccountEndPoints,
    ): AccountRepository =
        AccountRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideProfileRepository(
        endpoints: ProfileEndPoints,
    ): ProfileRepository =
        ProfileRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideActivitiesRepository(
        endpoints: ActivitiesEndpoints,
    ): ActivitiesRepository = ActivitiesRepositoryImpl(
        endpoints,
        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)
    )

    @Provides
    @Singleton
    fun provideHomeRepository(
        endpoints: HomeEndpoints,
    ): HomeRepository = HomeRepositoryImpl(
        endpoints,
        SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)
    )

    @Provides
    @Singleton
    fun provideRequestDetailsRepository(
        endpoints: RequestDetailsEndpoints,
    ): RequestDetailsRepository =
        RequestDetailsRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun providePaySlipsRepository(
        endpoints: PaySlipsEndPoints,
    ): PaySlipsRepository =
        PaySlipsRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideFiltersRepository(
        endpoints: FiltersEndPoints,
    ): FiltersRepository =
        FiltersRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideParticipantsRepository(
        endpoints: ParticipantsEndpoints,
    ): ParticipantsRepository =
        ParticipantsRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideProfileImageRepository(
        endpoints: ProfileImageEndPoints,
    ): ProfileImageRepository =
        ProfileImageRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideMyDayMeetingsRepository(
        endpoints: MyMeetingsEndPoints,
    ): MyDayMeetingsRepository =
        MyDayMeetingsRepositoryImpl(endpoints)

    @Provides
    @Singleton
    fun provideAvailableMeetingRoomsRepository(
        endpoints: AvailableMeetingRoomsEndPoints,

        ): AvailableMeetingRoomsRepository =
        AvailableMeetingRoomsRepositoryImpl(
            endpoints,

            )

    @Provides
    @Singleton
    fun provideBookMeetingRoomRepository(
        endpoints: BookMeetingRoomEndPoints,
    ): BookMeetingRoomRepository =
        BookMeetingRoomRepositoryImpl(
            endpoints,
        )
}
