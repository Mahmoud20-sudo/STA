package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.R
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.modules.account.model.AccountSetting
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppColors.OnPrimaryColor
import sa.sauditourism.employee.ui.theme.AppColors.PrimaryGray
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun AccountRowComponent(
    model: AccountSetting,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showSeparator: Boolean = true,
    onClick: () -> Unit = {},
) {
    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                model.image
            )
        )
    }
    Column(modifier = Modifier
        .background(Color.White)
        .clickableWithoutRipple {
            onClick.invoke()
        }) {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(32.dp)
                        .height(32.dp)
                        .background(
                            color = if (enabled) OnPrimaryColor else PrimaryGray,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                ) {
                    MediaComponent(
                        mediaContent = mediaContent,
                        modifier = Modifier.size(24.dp),
                        imageOptions = ImageOptions(
                            colorFilter = if (enabled) null else ColorFilter.tint(AppColors.White),
                        )
                    )
                }
                Spacer(Modifier.width(20.dp))
                CustomTextComponent(
                    text = model.name,
                    style = AppFonts.body1Medium,
                    color = if (enabled) AppColors.ColorsPrimitivesOne else AppColors.ColorsPrimitivesTwo,
                    lineHeight = 18.sp
                )
            }


            if (enabled)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = AppColors.ColorsPrimitivesThree,
                    modifier = Modifier
                        .size(20.dp),
                )

        }

        if (showSeparator) {
            HorizontalDivider(thickness = 1.dp, color = AppColors.ColorsPrimitivesFive)
        }
    }
}
