package sa.sauditourism.employee.extensions

import android.os.SystemClock
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.max

fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    interactionSource: MutableInteractionSource = MutableInteractionSource(),
    onClick: () -> Unit,
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    },
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        role = role,
        indication = null,
        interactionSource = remember { interactionSource },
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.slidingLineTransition(pagerState: PagerState, distance: Float) =
    composed {
        val direction = LocalLayoutDirection.current
        graphicsLayer {
            val scrollPosition = pagerState.currentPage + pagerState.currentPageOffsetFraction
            if (direction == LayoutDirection.Rtl) {
                translationX = (0 - scrollPosition) * distance
            } else {
                translationX = scrollPosition * distance
            }
        }
    }

fun Modifier.clickableOnce(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "clickable"
        properties["enabled"] = enabled
        properties["onClickLabel"] = onClickLabel
        properties["role"] = role
        properties["onClick"] = onClick
    },
) {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() },
    )
}

internal interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
    MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
    private var lastEventTime: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (SystemClock.elapsedRealtime() - lastEventTime < 1000) {
            return
        }
        lastEventTime = SystemClock.elapsedRealtime()
        event.invoke()
    }
}

fun Modifier.mirror(): Modifier = composed {
    if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
        this.scale(scaleX = -1f, scaleY = 1f)
    } else {
        this
    }
}

fun Modifier.dashedBorder(width: Dp, radius: Dp, color: Color) =
    drawBehind {
        drawIntoCanvas {
            val paint = Paint()
                .apply {
                    strokeWidth = width.toPx()
                    this.color = color
                    style = PaintingStyle.Stroke
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                }
            it.drawRoundRect(
                width.toPx(),
                width.toPx(),
                size.width - width.toPx(),
                size.height - width.toPx(),
                radius.toPx(),
                radius.toPx(),
                paint
            )
        }
    }

fun Modifier.scrollbar(
    scrollState: ScrollState,
    direction: Orientation,
    defaultPlanning: Boolean,
    config: ScrollbarConfig = ScrollbarConfig(),
): Modifier = composed {
    var (
        indicatorThickness, indicatorColor, indicatorCornerRadius,
        alpha, alphaAnimationSpec, padding
    ) = config

    val isScrollingOrPanning = scrollState.isScrollInProgress || defaultPlanning
    val isVertical = direction == Orientation.Vertical

    alpha = alpha ?: if (isScrollingOrPanning) 0.8f else 0f
    alphaAnimationSpec = alphaAnimationSpec ?: tween(
        delayMillis = if (isScrollingOrPanning) 0 else 1500,
        durationMillis = if (isScrollingOrPanning) 150 else 500
    )

    val scrollbarAlpha by animateFloatAsState(alpha, alphaAnimationSpec, label = "")

    drawWithContent {
        drawContent()

        val showScrollbar = true

        // Draw scrollbar only if currently scrolling or if scroll animation is ongoing.
        if (showScrollbar) {
            val (topPadding, bottomPadding, startPadding, endPadding) = arrayOf(
                padding.calculateTopPadding().toPx(), padding.calculateBottomPadding().toPx(),
                padding.calculateStartPadding(layoutDirection).toPx(),
                padding.calculateEndPadding(layoutDirection).toPx()
            )

            val isLtr = layoutDirection == LayoutDirection.Ltr
            val contentOffset = scrollState.value
            val viewPortLength = if (isVertical) size.height else size.width
            val viewPortCrossAxisLength = if (isVertical) size.width else size.height
            val contentLength = max(viewPortLength + scrollState.maxValue, 0.001f /* To prevent divide by zero error */)
            val scrollbarLength = viewPortLength -
                    (if (isVertical) topPadding + bottomPadding else startPadding + endPadding)
            val indicatorThicknessPx = indicatorThickness.toPx()
            val indicatorLength = max((scrollbarLength / contentLength) * viewPortLength, 20f.dp.toPx())
            val indicatorOffset = (scrollbarLength / contentLength) * contentOffset
            val scrollIndicatorSize = if (isVertical) Size(indicatorThicknessPx, indicatorLength)
            else Size(indicatorLength, indicatorThicknessPx)

            val scrollIndicatorPosition = if (isVertical)
                Offset(
                    x = if (isLtr) viewPortCrossAxisLength - indicatorThicknessPx - endPadding
                    else startPadding,
                    y = indicatorOffset + topPadding
                )
            else
                Offset(
                    x = if (isLtr) indicatorOffset + startPadding
                    else viewPortLength - indicatorOffset - indicatorLength - endPadding,
                    y = viewPortCrossAxisLength - indicatorThicknessPx - bottomPadding
                )

            drawRoundRect(
                color = indicatorColor,
                cornerRadius = indicatorCornerRadius.let { CornerRadius(it.toPx(), it.toPx()) },
                topLeft = scrollIndicatorPosition,
                size = scrollIndicatorSize,
                alpha = scrollbarAlpha
            )
        }
    }
}

data class ScrollbarConfig(
    val indicatorThickness: Dp = 8.dp,
    val indicatorColor: Color = Color.Gray.copy(alpha = 0.7f),
    val indicatorCornerRadius: Dp = indicatorThickness / 2,
    val alpha: Float? = null,
    val alphaAnimationSpec: AnimationSpec<Float>? = null,
    val padding: PaddingValues = PaddingValues(all = 0.dp),
)

fun Modifier.verticalScrollWithScrollbar(
    scrollState: ScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
    defaultPlanning: Boolean = false,
    scrollbarConfig: ScrollbarConfig = ScrollbarConfig()
): Modifier = this
    .scrollbar(scrollState, direction = Orientation.Vertical, config = scrollbarConfig, defaultPlanning = defaultPlanning)
    .verticalScroll(scrollState, enabled, flingBehavior, reverseScrolling)


fun Modifier.horizontalScrollWithScrollbar(
    scrollState: ScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
    defaultPlanning: Boolean = false,
    scrollbarConfig: ScrollbarConfig = ScrollbarConfig()
): Modifier = this
    .scrollbar(scrollState, direction = Orientation.Horizontal, config = scrollbarConfig, defaultPlanning = defaultPlanning)
    .horizontalScroll(scrollState, enabled, flingBehavior, reverseScrolling)

fun Modifier.conditional(condition : Boolean, modifier : Modifier.() -> Modifier) : Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}