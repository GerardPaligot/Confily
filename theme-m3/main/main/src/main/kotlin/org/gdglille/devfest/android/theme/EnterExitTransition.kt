package org.gdglille.devfest.android.theme

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset

fun enterSlideInHorizontal(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 200, easing = EaseIn)
) = slideInHorizontally(animationSpec = animationSpec, initialOffsetX = { it })

fun popEnterSlideInHorizontal(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 200, easing = EaseIn)
) = slideInHorizontally(animationSpec = animationSpec, initialOffsetX = { -it })

fun exitSlideInHorizontal(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 200, easing = EaseOut)
) = slideOutHorizontally(animationSpec = animationSpec, targetOffsetX = { -it })

fun popExistSlideInHorizontal(
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(durationMillis = 200, easing = EaseOut)
) = slideOutHorizontally(animationSpec = animationSpec, targetOffsetX = { it })
