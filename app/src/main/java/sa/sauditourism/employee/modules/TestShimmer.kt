package sa.sauditourism.employee.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun TestShimmer() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .background(Color.Gray),
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White, //Card background color
                    contentColor = Color.White  //Card content color,e.g.text
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(all = 8.dp)
                ) {
                    CoilImage(
                        imageModel = { null },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            contentDescription = "",
                        ),
                        component = rememberImageComponent {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .then(Modifier.shimmerModifier(true))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextComponent(
                        "ريدكس برو",
                        style = AppFonts.captionRegular,
                        isLoading = true,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CustomTextComponent(
                        "ريدكس برو",
                        style = AppFonts.tagsRegular,
                        isLoading = true,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}
