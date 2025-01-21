@file:Suppress("DEPRECATION")

package sa.sauditourism.employee.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.skydoves.landscapist.ImageOptions
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.dashedBorder
import sa.sauditourism.employee.extensions.getFilename
import sa.sauditourism.employee.extensions.getMimeType
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.prepareImageUri
import sa.sauditourism.employee.modules.services.model.form.response.Attachment
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts
import sa.sauditourism.employee.utils.GetCustomContents
import java.io.File


@Composable
fun AttachmentComponent(
    title: String? = null,
    informativeMessage: String? = null,
    readOnly: Boolean = false,
    modifier: Modifier= Modifier,
    isOptional: Boolean = false,
    attachmentsList: List<Uri> = mutableListOf(),
    defaultAttachmentsList: List<Attachment>? = null,
    attachmentListener: (Uri, Boolean) -> Unit,
    onItemClickListener: (Attachment) -> Unit = {}
) {
    val context = LocalContext.current

    var cameraAccessDenied by remember { mutableStateOf(false) }
    var chooserPopup by remember { mutableStateOf(false) }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val output = File(File(context.filesDir, "photos"), "sta_${System.currentTimeMillis()}.jpg");

    if (output.exists()) {
        output.delete();
    } else {
        output.parentFile?.mkdirs()
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { flag ->
            if (flag) {
                imageUri?.let { attachmentListener.invoke(it, true) }
            }
        }

    val documentPicker = rememberLauncherForActivityResult(
        contract = GetCustomContents(isMultiple = true),
        onResult = { uris ->
            uris.forEach { uri ->
                attachmentListener.invoke(uri, true)
            }
        })

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { flag->
        if (flag) {
            imageUri = context.prepareImageUri()
            imageUri?.let { cameraLauncher.launch(it) }
        } else {
            cameraAccessDenied = true
        }
    }


    val startForResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                //do something here
                intent?.data?.let {
                    attachmentListener.invoke(it, true)
                }
            }
        }

    val mediaContent by remember {
        mutableStateOf(
            MediaContent(
                type = GalleryType.IMAGE,
                source = R.drawable.ic_attachment,
            )
        )
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val borderColor = when (isPressed) {
        true -> AppColors.OnSecondaryDark
        else -> if (attachmentsList.isNotEmpty()) AppColors.OnPrimaryDark else AppColors.OnSurfaceVariantDark1
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Column(
            Modifier
                .fillMaxSize(),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                title?.let {
                    CustomTextComponent(
                        text = it,
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.ctaMedium,
                        modifier = Modifier,
                        lineHeight = 20.79.sp
                    )
                }
                if (isOptional)
                    CustomTextComponent(
                        text = LanguageConstants.OPTIONAL.localizeString().lowercase(),
                        color = AppColors.ColorTextSubdued,
                        style = AppFonts.labelSemiBold
                    )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(tween())
                    .wrapContentHeight()
            ) {
                if (readOnly && !defaultAttachmentsList.isNullOrEmpty())
                    defaultAttachmentsList.forEachIndexed { _, item ->

                        val thumbnail by remember {
                            mutableStateOf(
                                MediaContent(
                                    type = GalleryType.IMAGE,
                                    source = item.thumbnail
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(6.dp),
                                    color = AppColors.ColorsPrimitivesFour
                                )
                                .padding(horizontal = 12.dp, vertical = 13.dp)
                                .clickableWithoutRipple { onItemClickListener.invoke(item) },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            MediaComponent(
                                mediaContent = thumbnail,
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.FillBounds,
                                    alignment = Alignment.Center,
                                ),
                                showPlaceHolder = true,
                                placeHolder = if (item.fileName.getMimeType()
                                        .startsWith("image")
                                ) R.drawable.ic_thumbnal_image else R.mipmap.ic_document,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            CustomTextComponent(
                                text = item.fileName,
                                color = AppColors.BLACK90,
                                style = AppFonts.body2Regular,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 20.79.sp
                            )

                            IconButton(
                                onClick = { onItemClickListener.invoke(item) },
                                modifier = Modifier
                                    .size(18.dp),
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_view),
                                    tint = Color.Unspecified,
                                    contentDescription = null
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }



                if (attachmentsList.isNotEmpty())
                    attachmentsList.forEachIndexed { _, item ->

                        val thumbnail by remember {
                            mutableStateOf(
                                MediaContent(
                                    type = GalleryType.IMAGE,
                                    source = item
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .background(color = Color.Transparent)
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(6.dp),
                                    color = AppColors.AccentGreen2
                                )
                                .padding(horizontal = 12.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            MediaComponent(
                                mediaContent = thumbnail,
                                imageOptions = ImageOptions(
                                    contentScale = ContentScale.FillBounds,
                                    alignment = Alignment.Center,
                                ),
                                showPlaceHolder = true,
                                placeHolder = R.mipmap.ic_document,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )

                            CustomTextComponent(
                                text = item.getFilename(context),
                                color = AppColors.BLACK90,
                                style = AppFonts.body2Regular,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                lineHeight = 20.79.sp
                            )

                            IconButton(
                                onClick = { attachmentListener.invoke(item, false) },
                                modifier = Modifier
                                    .size(18.dp),
                            ) {
                                Icon(
                                    painterResource(id = R.drawable.ic_delete),
                                    tint = Color.Unspecified,
                                    contentDescription = null
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }
            }


            if (!readOnly)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(Color.White)
                        .dashedBorder(width = 1.dp, radius = 6.dp, color = borderColor)
                        .clickableWithoutRipple(
                            enabled = true,
                            interactionSource = interactionSource,
                            onClick = {
                                chooserPopup = true
                            })
                        .padding(vertical = 26.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    MediaComponent(
                        mediaContent = mediaContent,
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.Inside,
                            alignment = Alignment.Center,
                        ),
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(AppColors.OnSecondaryLight)
                            .padding(all = 10.dp),
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextComponent(
                        text = LanguageConstants.ATTACHEMNT_DESCRIPTION.localizeString(),
                        style = AppFonts.body1Medium,
                        lineHeight = 24.sp,
                        color = AppColors.SwitchButtonTrackColorDisabled
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    CustomTextComponent(
                        text = LanguageConstants.ATTACHEMNT_MAX_SIZE.localizeString(),
                        style = AppFonts.tagsRegular,
                        lineHeight = 21.sp,
                        color = AppColors.ColorBorders
                    )

                }

            if (!informativeMessage.isNullOrEmpty())
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_information_icon),
                            "info",
                            tint = AppColors.ColorsPrimitivesTwo,
                            modifier = Modifier
                                .size(16.dp),
                        )
                        CustomTextComponent(
                            text = informativeMessage,
                            color = AppColors.ColorsPrimitivesTwo,
                            style = sa.sauditourism.employee.resources.AppFonts.tagsSemiBold,
                        )
                    }

                }
        }

        AnimatedVisibility(visible = cameraAccessDenied) {
            ToastView(
                presentable = ToastPresentable(
                    type = ToastType.Error,
                    title = LanguageConstants.CAMERA_USER_PERMISSION.localizeString(),
                ),
                toastDuration = 5000L,
                onDismiss = {
                    cameraAccessDenied = false
                },
            )
        }

        if (chooserPopup)
            CustomAlertDialog(
                type = DialogType.Normal,
                imageResId = R.drawable.ic_attachment,
                tintColor = AppColors.OnSecondaryDark,
                title = LanguageConstants.ATTACHEMNT_SELECT_UPLOAD_OPTION.localizeString(),
                primaryButtonText = LanguageConstants.ATTACHEMNT_SELECT_CAMERA.localizeString(),
                secondaryButtonText = LanguageConstants.ATTACHEMNT_SELECT_PHOTO_LIBRARY.localizeString(),
                thirdButtonText = LanguageConstants.ATTACHEMNT_SELECT_DOCUMENT_LIBRARY.localizeString(),
                shouldShowCloseIcon = true,
                onPrimaryButtonClick = {
                    // Handle primary button click
                    chooserPopup = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onSecondaryButtonClick = {
                    chooserPopup = false
                    val intent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    )
                    startForResult.launch(intent)
                },
                onThirdButtonClick = {
                    chooserPopup = false
                    documentPicker.launch("*/*")
                },
                onDismiss = {
                    // Handle alert dismiss
                    chooserPopup = false
                }
            )
    }
}
