package sa.sauditourism.employee.managers.realtime

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import sa.sauditourism.employee.configuration.AppConfig

object FirebaseAuthenticationManager {

    fun authenticateDatabase(onResult: (Boolean) -> Unit) {
        val auth = Firebase.auth
        if (auth.currentUser == null) {
            auth.signInWithEmailAndPassword(AppConfig.FIREBASE_EMAIL, AppConfig.FIREBASE_PASS)
                .addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            onResult(true)
                        } else {
                            onResult(false)
                        }
                    } catch (e: Exception) {
                        onResult(false)

                    }
                }
        } else {
            onResult(true)
        }
    }
}
