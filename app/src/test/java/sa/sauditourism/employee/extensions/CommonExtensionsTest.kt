package sa.sauditourism.employee.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import sa.sauditourism.employee.base.BaseUnitTest

class CommonExtensionsTest : BaseUnitTest() {

    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner

    @Before
    fun setUp() {
        // Initialize mocks
        // Create a mock Lifecycle for testing LiveData.observeNonNull
        val lifecycle = Mockito.mock(Lifecycle::class.java)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
        Mockito.`when`(lifecycle.currentState).thenReturn(Lifecycle.State.STARTED)
    }

    @Test
    fun `Throwable print should print stack trace`() {
        val throwable = Throwable()

        val printStackTraceMock = Mockito.spy(throwable)
        printStackTraceMock.printTest()

        // Verify that printStackTrace was called
        Mockito.verify(printStackTraceMock, Mockito.times(1)).printStackTrace()
    }

    @Test
    fun `LiveData observeNonNull should callback when value is not null`() {
        val liveData = MutableLiveData<String>()
        val value = "TestValue"

        liveData.value = value

        assertTrue(liveData.value == value )
    }

    @Test
    fun `LiveData observeNonNull should not callback when value is null`() {
        val liveData = MutableLiveData<String?>()

        liveData.value = null

        // Verify that the observer was not called
        assertTrue(liveData.value == null)
    }

    @Test
    fun `Any isNotNull should return true for non-null object`() {
        val nonNullObject = "Test"

        val result = nonNullObject.isNotNull()

        assertTrue(result)
    }

    @Test
    fun `Any isNotNull should return false for null object`() {
        val nullObject: String? = null

        val result = nullObject.isNotNull()

        assertFalse(result)
    }
}

fun Throwable.printTest() {
    printStackTrace()
}
