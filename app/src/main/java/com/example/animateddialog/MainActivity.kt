package com.example.animateddialog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.animateddialog.ui.theme.AnimatedDialogTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimatedDialogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var dialogOpen by remember {
                        mutableStateOf(false)
                    }
                    Dialog(
                        visible = dialogOpen,
                        duration = 3000,
                        onTap = { dialogOpen = true },
                        onDismiss = { dialogOpen = false },
                        dialogEnterAnim = slideInVertically(tween(500)) + fadeIn(),
                        dialogExitAnim = slideOutVertically(tween(500)) + fadeOut()
                    ) {
                        Text(
                            text = "Nulla quis lorem ut libero malesuada feugiat. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                            color = Color.White
                        )
                    }
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Tap", style = TextStyle(color = Color.Cyan, fontSize = 46.sp, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    duration: Int,
    position: Alignment = Alignment.TopCenter,
    dialogEnterAnim: EnterTransition = scaleIn(tween(500)),
    dialogExitAnim: ExitTransition = scaleOut(tween(500)),
    progressColor: Color = Color(0xff96ffe7),
    progressBackgroundColor: Color = Color(0xff27c5a1),
    showProgress: Boolean = true,
    onTap: () -> Unit,
    onDismiss: () -> Unit,
    disableSplash: Boolean = false,
    content: @Composable () -> Unit
) {
    var _duration by remember { mutableIntStateOf(duration) }
    var progress by remember { mutableFloatStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress, animationSpec = tween(
            durationMillis = _duration,
            easing = LinearEasing
        ), finishedListener = {
            _duration = duration
        }, label = ""
    )
    LaunchedEffect(visible) {
        if (visible) {
            progress = 1f
            delay(duration.toLong())
            onDismiss()
            _duration = 1
            progress = 0f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(2f),
        contentAlignment = position
    ) {
        var contentModifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
        contentModifier = if (disableSplash) {
            contentModifier.clickable(
                onClick = onTap,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            )
        } else {
            contentModifier.clickable {
                onTap()
            }
        }
        Box(
            modifier = contentModifier,
        ) {
            AnimatedVisibility(visible = visible, enter = dialogEnterAnim, exit = dialogExitAnim) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(Color(0xff14aa88))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    content()
                    if (showProgress) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape),
                            progress = progressAnimation,
                            color = progressColor,
                            trackColor = progressBackgroundColor
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AnimatedDialogTheme {
    }
}