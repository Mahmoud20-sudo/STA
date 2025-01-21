package sa.sauditourism.employee.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun Loader(
    modifier: Modifier = Modifier,
    iterations: Int = 1,
    lottie: Int
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottie))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = iterations)

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}