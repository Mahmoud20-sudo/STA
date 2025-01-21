package sa.sauditourism.employee.utils

import android.annotation.SuppressLint
import com.bumptech.glide.request.RequestOptions
import sa.sauditourism.employee.EmployeeApplication


@SuppressLint("CheckResult")
fun customRequestOption(): RequestOptions {
    val context = EmployeeApplication().applicationContext
    val requestOptions = RequestOptions()
//    val circularProgressDrawable = CircularProgressDrawable(context)
//    circularProgressDrawable.strokeWidth = 5f
//    circularProgressDrawable.centerRadius = 30f
//    circularProgressDrawable.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
//    circularProgressDrawable.start()
//    requestOptions.error(R.drawable.new_place_holder)
//    requestOptions.placeholder(circularProgressDrawable)
    return requestOptions
}
