package sa.sauditourism.employee.di

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.mockito.Mock
import sa.sauditourism.employee.base.BaseUnitTest

@ExperimentalCoroutinesApi
class RealTimeDatabaseModuleTest : BaseUnitTest(){

    @Mock
    private lateinit var context: Context

    @Test
    fun testProvideFirebaseNotificationManager() {
        val firebaseNotificationManager = RealTimeDatabaseModule.provideRealTimeDatabaseManager()
        assertNotNull(firebaseNotificationManager)
    }
}
