package sa.sauditourism.employee.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

/**
 * BaseUnitTest is an abstract class used for setting up common configurations and rules
 * for writing unit tests in a Coroutine-based environment with AndroidX LiveData support.
 * This class is marked with the @ExperimentalCoroutinesApi annotation, indicating that
 * it might use experimental APIs from Kotlin Coroutines.
 *
 * To use this class as a base for your unit tests, create a subclass and implement the
 * actual test cases in it.
 *
 * @see UnitTestCoroutineRule
 * @see InstantTaskExecutorRule
 *
 */
@ExperimentalCoroutinesApi
abstract class BaseUnitTest {

    /**
     * A JUnit TestRule that sets up the necessary coroutine scope for testing coroutines.
     * Use this rule to test suspend functions and other coroutine-related code.
     *
     * @see UnitTestCoroutineRule
     */
    @get:Rule
    val unitTestCoroutineRule = UnitTestCoroutineRule()

    /**
     * A JUnit TestRule that configures LiveData to work on the main thread when running tests.
     * This is useful when you have LiveData observers that update the UI and need to run
     * the tests on the main thread.
     *
     * @see InstantTaskExecutorRule
     */
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var autoCloseable: AutoCloseable

    @Before
    fun openMocks() {
        autoCloseable = MockitoAnnotations.openMocks(this)
    }

    @After
    fun releaseMocks() {
        autoCloseable.close()
    }
}
