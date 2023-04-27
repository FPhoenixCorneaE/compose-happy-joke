package com.fphoenixcorneae.happyjoke.mvi.ui.page.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Black30
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold

/**
 * @desc：消息
 * @date：2023/03/17 10:48
 */
@Preview
@Composable
fun MessageScreen() {
    SystemUiScaffold(isFitsSystemWindows = false) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(bottom = 60.dp)
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
            ) {
                Text(
                    text = "消息",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .align(Alignment.CenterStart),
                )
                Image(
                    painter = painterResource(id = R.mipmap.ic_search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconCountTextItem(iconRes = R.mipmap.ic_message_like, msgCount = 15875, content = "赞·踩")
                IconCountTextItem(iconRes = R.mipmap.ic_message_comment, msgCount = 1933, content = "评论")
                IconCountTextItem(iconRes = R.mipmap.ic_message_attention, msgCount = 319, content = "关注")
            }
        }
    }
}

@Preview
@Composable
private fun IconCountTextItem(
    iconRes: Int = R.mipmap.ic_message_like,
    msgCount: Int = 15875,
    content: String = "赞·踩",
) {
    ConstraintLayout {
        val (icon, count, text) = createRefs()
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .size(48.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )
        Text(
            text = "$msgCount",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier
                .constrainAs(count) {
                    top.linkTo(icon.top)
                    bottom.linkTo(icon.top)
                    start.linkTo(icon.end)
                    end.linkTo(icon.end)
                }
                .border(width = 2.dp, color = Color.White, RoundedCornerShape(16.dp))
                .background(color = Color.Red, RoundedCornerShape(16.dp))
                .padding(horizontal = 4.dp, vertical = 2.dp),
        )
        Text(
            text = content,
            color = Black30,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(icon.bottom, margin = 4.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
        )
    }
}