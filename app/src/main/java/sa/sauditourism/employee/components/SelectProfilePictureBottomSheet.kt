package sa.sauditourism.employee.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import sa.sauditourism.employee.BuildConfig
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.getFileFromUri
import sa.sauditourism.employee.extensions.getFilename
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.prepareImageUri
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.utils.GetCustomContents
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SelectProfilePictureBottomSheet(
    isAvatar: Boolean,
    avatarList: List<AvatarData>,
    modifier: Modifier = Modifier,
    selectedAvatarId: Int = 1,
    onAvatarSelected: ((AvatarData) -> Unit)? = null,
    onImageSelected: ((Uri) -> Unit)? = null,
    onImageDeleted: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
    onPermissionDenied: (() -> Unit)? = null,
) {
    val context = LocalContext.current
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var chooseAnAvatarClicked by remember { mutableStateOf(false) }

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUri = it
                onImageSelected?.invoke(imageUri!!)
                onDismiss.invoke()
            }
        }
    )

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onImageSelected?.invoke(imageUri!!)
                onDismiss.invoke()
            } else {
                imageUri = null
            }
        }
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            imageUri?.let {
                takePictureLauncher.launch(it)
            }
        } else {
            onPermissionDenied?.invoke()
        }
    }

    androidx.compose.material3.ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        val horizontalPaddingModifier = Modifier.padding(horizontal = 24.dp)
        Row(
            modifier = horizontalPaddingModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomTextComponent(
                if (chooseAnAvatarClicked) {
                    LanguageConstants.CHOOSE_AN_AVATAR.localizeString()
                } else {
                    LanguageConstants.SELECT_YOUR_PROFILE_PICTURE.localizeString()
                },
                style = AppFonts.heading7SemiBold,
                lineHeight = 27.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(chooseAnAvatarClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = "Close",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(24.dp)
                        .clickableWithoutRipple {
                            chooseAnAvatarClicked = false
                        },
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        AnimatedContent(
            targetState = chooseAnAvatarClicked,
            label = "choose avatar selection"
        ) { chooseAvatar ->
            Column {
                if (chooseAvatar) {
                    var selectedAvatar = avatarList.firstOrNull { it.id == selectedAvatarId }
                    AvatarComponent(
                        avatarList = avatarList,
                        selectedAvatar = selectedAvatar,
                        maxItemInEachRow = 3,
                        modifier = horizontalPaddingModifier
                    ) { selected ->
                        selectedAvatar = selected
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    ButtonComponent(
                        modifier = horizontalPaddingModifier.fillMaxWidth(),
                        text = LanguageConstants.DONE_BUTTON_TITLE.localizeString(),
                        clickListener = {
                            if (selectedAvatar != null) {
                                onAvatarSelected?.invoke(selectedAvatar!!)
                            }
                            onDismiss.invoke()
                        },
                        buttonType = ButtonType.PRIMARY,
                        buttonSize = ButtonSize.LARGE,
                    )
                } else {
                    Row(
                        modifier = horizontalPaddingModifier.clickableWithoutRipple {
                            chooseAnAvatarClicked = true
                        },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (isAvatar) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_user),
                                "user",
                                modifier = Modifier.size(20.dp),
                                tint = Color.Black,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            CustomTextComponent(
                                LanguageConstants.CHOOSE_AN_AVATAR.localizeString(),
                                style = AppFonts.body2Medium,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = horizontalPaddingModifier.clickableWithoutRipple {
                            galleryLauncher.launch("image/*")
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_image),
                            "image",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        CustomTextComponent(
                            LanguageConstants.CHOOSE_IMAGE_FROM_GALLERY.localizeString(),
                            style = AppFonts.body2Medium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = horizontalPaddingModifier.clickableWithoutRipple {
                            imageUri = context.prepareImageUri()

                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_camera_black),
                            "camera",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        CustomTextComponent(
                            LanguageConstants.TAKE_PHOTO.localizeString(),
                            style = AppFonts.body2Medium,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (!isAvatar) {
                        Spacer(modifier = Modifier.height(14.dp))
                        ButtonComponent(
                            modifier = horizontalPaddingModifier,
                            text = LanguageConstants.REMOVE_CURRENT_PICTURE.localizeString(),
                            clickListener = {
                                onImageDeleted?.invoke()
                                onDismiss.invoke()
                            },
                            buttonType = ButtonType.FAILED_LINK_PRIMARY,
                            buttonSize = ButtonSize.MEDIUM,
                            removePadding = true,
                            startIcon = R.drawable.ic_delete_red
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}