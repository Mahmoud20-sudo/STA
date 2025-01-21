package sa.sauditourism.employee.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher

/**
 * The `DispatcherProvider` interface defines the contract for providing coroutine dispatchers
 * for different contexts in the application.
 *
 * Implementing classes are responsible for supplying the appropriate dispatchers to be used
 * in various parts of the application, such as IO-bound tasks and tasks that need to run
 * on the main (UI) thread.
 *
 */
interface DispatcherProvider {
    /**
     * The [CoroutineDispatcher] to be used for offloading tasks that perform IO operations.
     * It is typically used for disk or network IO tasks.
     *
     * @see CoroutineDispatcher
     */
    val io: CoroutineDispatcher

    /**
     * The [MainCoroutineDispatcher] to be used for tasks that update the UI or require
     * execution on the main (UI) thread. This dispatcher should be used when you have
     * coroutines interacting with LiveData or UI components.
     *
     * @see MainCoroutineDispatcher
     */
    val main: MainCoroutineDispatcher

    /**
     * This method is intended to be implemented by concrete classes that extend this interface.
     * It provides the option to reset any state or cached values related to the dispatcher provider.
     * Concrete implementations may choose to reset internal states or perform cleanup operations
     * if required.
     *
     */
    fun reset() {
    }
}
