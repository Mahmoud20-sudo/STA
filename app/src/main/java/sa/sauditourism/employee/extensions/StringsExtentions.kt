package sa.sauditourism.employee.extensions

import android.util.Base64
import android.webkit.MimeTypeMap
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.managers.language.LanguageManager
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun String.localizeString(args: Any? = null, params: Any? = null, before: Boolean = false): String {
    return LanguageManager.getTranslationByKey(this, args, params, before)
}

fun String.encryptToken(): String {
    return try {
        val iv = IvParameterSpec(EmployeeApplication.instance.environmentManager.getVariable(
            EnvironmentKeys.ENCRYPT_SECRET_IV).toByteArray())
        val keySpec = SecretKeySpec(EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.ENCRYPT_SECRET_KEY).toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
        val byteArray = cipher.doFinal(toByteArray())
        val encodedByte = Base64.encode(byteArray, Base64.DEFAULT)
        String(encodedByte)
    } catch (e: Exception) {
        ""
    }
}

fun String.getHashMapFromJson(): HashMap<String, String> {
    val gson = Gson()
    return gson.fromJson(
        this,
        object : TypeToken<HashMap<String, String>>() {}.type,
    )
}

fun String.convertArabic(): String {
    val chArr = this.toCharArray()
    val sb = StringBuilder()
    for (ch in chArr) {
        if (Character.isDigit(ch)) {
            sb.append(Character.getNumericValue(ch))
        } else if (ch == '٫') {
            sb.append(".")
        } else {
            sb.append(ch)
        }
    }
    return sb.toString()
}

fun String.isNumeric(): Boolean = toDoubleOrNull() != null

fun String.getMimeType(): String = MimeTypeMap.getFileExtensionFromUrl(this)
        ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase()) }
        ?: "*/*"
