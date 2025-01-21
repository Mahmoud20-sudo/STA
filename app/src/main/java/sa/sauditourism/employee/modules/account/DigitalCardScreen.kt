package sa.sauditourism.employee.modules.account

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import android.text.BidiFormatter
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.skydoves.landscapist.ImageOptions
import ir.kaaveh.sdpcompose.sdp
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.ModalBottomSheet
import sa.sauditourism.employee.components.ProfileDetailsHeaderComponentModel
import sa.sauditourism.employee.components.QrCodeComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.LanguageConstants.ACCOUNT_SHARE_MY_BUSINESS_CARD
import sa.sauditourism.employee.constants.LanguageConstants.DONE_BUTTON_TITLE
import sa.sauditourism.employee.constants.LanguageConstants.QRCODE
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppFonts
import java.io.File

@Composable
fun DigitalCardScreen(
    navController: NavController,
    viewModel: AccountViewModel
) {
    val event by viewModel.event.collectAsState()
    var accountDetails by remember { mutableStateOf(sharedPreferencesManager.accountDetails) }

    var userImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val selectedAvatarId by rememberSaveable { mutableIntStateOf(sharedPreferencesManager.userAvatar) }
    var profileData by remember {
        val value = avatarList.firstOrNull { it.id == selectedAvatarId }

        mutableStateOf(
            ProfileDetailsHeaderComponentModel(
                imageUri = Uri.parse(value?.imageUrl ?: ""),
                isAvatar = accountDetails?.image.isNullOrEmpty(),
            )
        )
    }

    val originalImageOverride: (String?) -> Unit = {
        accountDetails?.image = it
        userImageBitmap =
            if (it.isNullOrEmpty()) null else accountDetails?._delegatedImage?.let { byteArray ->
                BitmapFactory.decodeByteArray(
                    byteArray,
                    0,
                    byteArray.size
                )
            }
        val userImage = runCatching {
            Uri.parse(
                accountDetails?.image
                    ?: avatarList.firstOrNull { it.id == selectedAvatarId }?.imageUrl
            )
        }.getOrNull()

        profileData = ProfileDetailsHeaderComponentModel(
            userImage,
            accountDetails?.image.isNullOrEmpty()
        )
    }

    LaunchedEffect(true) {
        originalImageOverride.invoke(accountDetails?.image)
        if (event is UiState.Success) {
            event.data?.let { account ->
                accountDetails = account
                userImageBitmap = accountDetails?._delegatedImage?.let { byteArray ->
                    BitmapFactory.decodeByteArray(
                        byteArray,
                        0,
                        byteArray.size
                    )
                }
            }
        }
    }

    val context = LocalContext.current
    var showQrCode by remember { mutableStateOf(false) }

    val accountLogo by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                R.mipmap.ic_account_logo,
            )
        )
    }
    val placeHolder by remember {
        mutableStateOf(
            MediaContent(
                GalleryType.IMAGE,
                accountDetails?.image,
            )
        )
    }

    val backgroundPattern by remember {
        mutableStateOf(
            MediaContent(
                type = GalleryType.IMAGE,
                source = R.drawable.background_pattern
            )
        )
    }

    val txtDirection =
        if (sharedPreferencesManager.preferredLocale.orEmpty() == "ar") TextDirection.Rtl else TextDirection.Ltr

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
            .background(Color.Transparent)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top
    ) {

        HeaderComponent(
            HeaderModel(
                title = LanguageConstants.ACCOUNT_BUSINESS_CARD_SCREEN_TITLE
                    .localizeString()
            ),
            isStandAlone = true,
            showShadow = false,
            backgroundColor = Color.Transparent,
            backHandler = {
                navController.popBackStack()
                true
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(450.dp)
                .clip(RoundedCornerShape(24.dp))

        ) {
            val (card, pattern) = createRefs()

            // Info Card
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        clip = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .zIndex(5f)
                    .constrainAs(card) {
                        top.linkTo(pattern.top, margin = 15.dp)
                        start.linkTo(pattern.start, margin = 10.dp)
                        end.linkTo(pattern.end, margin = 10.dp)
                        bottom.linkTo(pattern.bottom, margin = 15.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    },
                colors = CardDefaults.cardColors(Color.White)

            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ) {

                    MediaComponent(
                        mediaContent = accountLogo,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(131.dp)
                            .height(36.dp),
                        showPlaceHolder = false
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MediaComponent(
                        mediaContent = MediaContent(
                            GalleryType.IMAGE,
                            source = userImageBitmap ?: profileData.imageUri
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .clip(RoundedCornerShape(16.dp))
                            .size(113.dp),
                        placeHolder = if (accountDetails?.gender == "M") R.drawable.male_avatar_rect else R.drawable.ic_female_avatar,
                        showPlaceHolder = true,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.FillBounds,
                            contentDescription = placeHolder.contentDescription,
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextComponent(
                        text = accountDetails?.name?.replace("+", " "),
                        color = AppColors.BLACK90,
                        style = AppFonts.heading7SemiBold,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextComponent(
                        text = "${LanguageConstants.ACCOUNT_ID.localizeString()}${accountDetails?.id}",
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.body2Regular.copy(lineHeight = 20.79.sp),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_call),
                                "",
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            CustomTextComponent(
                                text = BidiFormatter.getInstance()
                                    .unicodeWrap(accountDetails?.phone),
                                color = AppColors.ColorTextSubdued,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = AppFonts.capsSmallRegular.copy(
                                    lineHeight = 17.82.sp,
                                    textDirection = txtDirection
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mail),
                                "",
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(5.dp))

                            CustomTextComponent(
                                text = accountDetails?.email,
                                color = AppColors.ColorTextSubdued,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = AppFonts.capsSmallRegular.copy(lineHeight = 17.82.sp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    CustomTextComponent(
                        text = LanguageConstants.ACCOUNT_JOB_TITLE.localizeString(),
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.capsSmallRegular.copy(lineHeight = 17.82.sp),
                        modifier = Modifier
                            .align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    CustomTextComponent(
                        text = accountDetails?.jobTitle?.replace("+", " "),
                        color = AppColors.BLACK90,
                        style = AppFonts.capsSmallSemiBold.copy(lineHeight = 17.82.sp),
                        modifier = Modifier
                            .align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CustomTextComponent(
                        text = LanguageConstants.ACCOUNT_DEPARTMENT.localizeString(),
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.capsSmallRegular.copy(lineHeight = 17.82.sp),
                        modifier = Modifier
                            .align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    CustomTextComponent(
                        text = accountDetails?.department?.replace("+", " "),
                        color = AppColors.BLACK90,
                        style = AppFonts.capsSmallSemiBold.copy(lineHeight = 17.82.sp),
                        modifier = Modifier
                            .align(Alignment.Start)
                    )
                }
            }

            MediaComponent(
                mediaContent = backgroundPattern,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .constrainAs(pattern) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds,
                    contentDescription = placeHolder.contentDescription,
                )
            )

        }

        ButtonComponent(
            text = ACCOUNT_SHARE_MY_BUSINESS_CARD.localizeString(),
            clickListener = {
                val employeeName = accountDetails?.name?.replace("+", " ")
                    ?: LanguageConstants.NONE.localizeString()
                val employeeEmail = accountDetails?.email?.replace("+", " ")
                    ?: LanguageConstants.NONE.localizeString()
                val employeePhone = accountDetails?.phone?.replace("+", " ")
                    ?: LanguageConstants.NONE.localizeString()
                val employeeJobTitle = accountDetails?.jobTitle?.replace("+", " ")
                    ?: LanguageConstants.NONE.localizeString()

                // Format the data as a string
                val employeeDetails = """
       ${LanguageConstants.ACCOUNT_EMPLOYEE_DETAILS.localizeString()}
        ${LanguageConstants.ACCOUNT_EMPLOYEE_NAME.localizeString()} $employeeName
        ${LanguageConstants.ACCOUNT_EMPLOYEE_EMAIL.localizeString()} $employeeEmail
        ${LanguageConstants.ACCOUNT_EMPLOYEE_PHONE.localizeString()} $employeePhone
        ${LanguageConstants.ACCOUNT_EMPLOYEE_JOB_TITLE.localizeString()} $employeeJobTitle
                       
    """.trimIndent()

//                val shareIntent = Intent(Intent.ACTION_SEND).apply {
//                    type = "text/plain"
//                    putExtra(
//                        Intent.EXTRA_TEXT,
//                        employeeDetails
//                    )
//                }
//
//                if (shareIntent.resolveActivity(context.packageManager) != null) {
//                    context.startActivity(
//                        Intent.createChooser(
//                            shareIntent,
//                            "Share via"
//                        )
//                    )
//                }

                val vcfFile = createVCard(context, accountDetails)
                vcfFile?.let { shareVCard(context, it) }


            },
            modifier = Modifier
                .padding(top = 33.5.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .zIndex(2f)
                .shadow(
                    2.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = AppColors.ColorsPrimitivesTwo,
                    clip = true
                )
                .height(48.dp),
            buttonType = ButtonType.WHITE_BACKGROUND,
            buttonSize = ButtonSize.LARGE,
            startIcon = R.drawable.ic_share,
            startIconModifier = Modifier.size(20.dp)
        )

        ButtonComponent(
            text = QRCODE.localizeString(),
            clickListener = {
                showQrCode = true
            },
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .zIndex(2f)
                .shadow(
                    2.dp,
                    shape = RoundedCornerShape(8.dp),
                    ambientColor = AppColors.ColorsPrimitivesTwo,
                    clip = true
                )
                .height(48.dp),
            buttonType = ButtonType.WHITE_BACKGROUND,
            buttonSize = ButtonSize.LARGE,
            startIcon = R.drawable.ic_eye,
            startIconModifier = Modifier.size(20.dp)
        )

        if (showQrCode)
            ModalBottomSheet(
                topPadding = 180.sdp,
                content = { onDismiss ->
                    Box {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 56.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Spacer(modifier = Modifier.height(68.dp))

                            QrCodeComponent(
                                content = accountDetails?.digitalCardUrl ?: "",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .size(268.dp)
                                    .padding(24.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomTextComponent(
                                text = LanguageConstants.SCANQR.localizeString(),
                                color = AppColors.BLACK,
                                textAlign = TextAlign.Center,
                                style = AppFonts.heading4SemiBold.copy(lineHeight = 29.4.sp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            CustomTextComponent(
                                text = "",
                                color = AppColors.ColorTextSubdued,
                                style = AppFonts.body2Medium.copy(lineHeight = 16.98.sp),
                                maxLines = 2,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(60.dp))
                        }
                        ButtonComponent(
                            DONE_BUTTON_TITLE.localizeString(),
                            clickListener = {
                                onDismiss.invoke()
                                showQrCode = false
                            },
                            modifier = Modifier
                                .padding(vertical = 22.dp, horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(48.dp)
                                .align(Alignment.BottomCenter),
                            buttonType = ButtonType.PRIMARY,
                            buttonSize = ButtonSize.LARGE
                        )
                    }
                },
                onDismissed = { showQrCode = false }
            )
    }
}

fun createVCard(context: Context, accountDetails: AccountDetails?): File? {
    val vCard = """
    BEGIN:VCARD
    VERSION:3.0
    N:${accountDetails?.name ?: ""};;;
    FN:${accountDetails?.firstName ?: ""}
    EMAIL;TYPE=EMAIL:${accountDetails?.email ?: ""}
    TEL;TYPE=CELL:${accountDetails?.phone ?: ""}
    TITLE:${accountDetails?.jobTitle ?: ""}
    ORG:${accountDetails?.department ?: ""}
    DEPARTMENT:${accountDetails?.department ?: ""}
    NOTE:Employee ID: ${accountDetails?.id ?: ""}
    END:VCARD
""".trimIndent()

    return try {
        val file = File(context.cacheDir, "contact.vcf")
        if (!file.exists()) {
            file.createNewFile()
        }

        file.writeText(vCard)
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun shareVCard(context: Context, file: File) {
    // Ensure file exists
    if (!file.exists()) {
        Log.e("ShareVCard", "File does not exist: ${file.absolutePath}")
        return
    }

    // Get URI for the file
//    val uri = FileProvider.getUriForFile(context, "${context.packageName}.", file)
    val uri = FileProvider.getUriForFile(
        context,
        BuildConfig.APPLICATION_ID + ".provider",
        file
    );

    // Create and launch share intent
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/x-vcard"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share Contact"))
}
