package sa.sauditourism.employee.managers

import android.util.Base64
import android.util.Log
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.CommonConstants.CIPHER_ALGORITHM
import sa.sauditourism.employee.constants.CommonConstants.CIPHER_TRANSFORMATION
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionManager {

    fun encrypt(message: String): String? {
        try {
            Log.e("encrypt", "message =$message")
            val iv = IvParameterSpec(EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.ENCRYPT_SECRET_IV).toByteArray())
            val keySpec = SecretKeySpec(EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.ENCRYPT_SECRET_KEY).toByteArray(), CIPHER_ALGORITHM)
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
            val crypted = cipher.doFinal(message.toByteArray())
            val encodedByte = Base64.encode(crypted, Base64.DEFAULT)
            return String(encodedByte)
        } catch (e: Exception) {
            Log.e("Exception", "encrypt Exception =" + e.message)
            return null
        }
    }

    fun decrypt(encryptedMessage: String?): String? {
        try {
            Log.e("decrypt", "encryptedMessage =$encryptedMessage")
            val iv = IvParameterSpec(EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.ENCRYPT_SECRET_IV).toByteArray())
            val keySpec = SecretKeySpec(EmployeeApplication.instance.environmentManager.getVariable(
                EnvironmentKeys.ENCRYPT_SECRET_KEY).toByteArray(), CIPHER_ALGORITHM)
            val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
            val decodedByte = Base64.decode(encryptedMessage, Base64.DEFAULT)
            val decrypted = cipher.doFinal(decodedByte)
            return String(decrypted)
        } catch (e: Exception) {
            Log.e("Exception", "decrypt Exception =" + e.message)
            return null
        }
    }
}
