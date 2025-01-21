package sa.sauditourism.employee.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

/**
 * AppDispatcherProvider is an implementation of the [DispatcherProvider] interface,
 * providing coroutine dispatchers for different contexts in the application.
 * This class maps specific dispatchers to the corresponding contexts like IO and Main.
 *
 * @see DispatcherProvider
 */
class AppDispatcherProvider : DispatcherProvider {

    /**
     * The [CoroutineDispatcher] to be used for offloading tasks that perform IO operations.
     * It is typically used for disk or network IO tasks.
     *
     * @see Dispatchers.IO
     */
    override val io: CoroutineDispatcher = Dispatchers.IO

    /**
     * The [MainCoroutineDispatcher] to be used for tasks that update the UI or require
     * execution on the main (UI) thread. This dispatcher should be used when you have
     * coroutines interacting with LiveData or UI components.
     *
     * @see Dispatchers.Main
     */
    override val main: MainCoroutineDispatcher = Dispatchers.Main
}
