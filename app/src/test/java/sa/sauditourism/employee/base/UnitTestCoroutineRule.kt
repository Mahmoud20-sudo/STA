package sa.sauditourism.employee.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A JUnit TestRule that sets up a custom [DispatcherProvider] for unit testing coroutines.
 * It is marked with the @ExperimentalCoroutinesApi annotation, indicating that it might use
 * experimental APIs from Kotlin Coroutines.
 *
 * This rule allows you to use custom [TestDispatcher] for IO and Main threads during unit testing.
 * By default, it uses the [UnconfinedTestDispatcher] for both IO and Main dispatchers.
 *
 * @param testDispatcher The custom [TestDispatcher] to use for both IO and Main threads.
 * @see TestDispatcher
 * @see UnconfinedTestDispatcher
 * @see DispatcherProvider
 */
@ExperimentalCoroutinesApi
class UnitTestCoroutineRule(private val testDispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        AppDispatchers.dispatcherProvider = UnitTestAppDispatcherProvider(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        AppDispatchers.dispatcherProvider.reset()
    }
}

/**
 * A concrete implementation of the [DispatcherProvider] interface, designed specifically
 * for unit tests. It allows you to use custom [TestDispatcher] for the IO and Main threads.
 * It is marked with the @ExperimentalCoroutinesApi annotation, indicating that it might use
 * experimental APIs from Kotlin Coroutines.
 *
 * @param testDispatcher The custom [TestDispatcher] to use for both IO and Main threads.
 * @see TestDispatcher
 * @see DispatcherProvider
 */
@ExperimentalCoroutinesApi
private class UnitTestAppDispatcherProvider(testDispatcher: TestDispatcher = StandardTestDispatcher()) :
    DispatcherProvider {

    override val io: CoroutineDispatcher = Dispatchers.Main
    override val main: MainCoroutineDispatcher = Dispatchers.Main

    init {
        Dispatchers.setMain(testDispatcher)
    }

    override fun reset() {
        Dispatchers.resetMain()
    }
}

/**
 * A centralized point to access the [DispatcherProvider] used in the application.
 * It holds a [dispatcherProvider] property, which defaults to an instance of [AppDispatcherProvider].
 *
 * The [dispatcherProvider] property is used to access the IO and Main dispatchers as well,
 * making it convenient to use coroutines throughout the application.
 *
 * @see DispatcherProvider
 * @see AppDispatcherProvider
 */
@ExperimentalCoroutinesApi
object AppDispatchers {
    var dispatcherProvider: DispatcherProvider = AppDispatcherProvider()

    val IO = dispatcherProvider.io
    val Main = dispatcherProvider.main
}
