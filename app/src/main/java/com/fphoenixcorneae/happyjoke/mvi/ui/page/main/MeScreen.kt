package com.fphoenixcorneae.happyjoke.mvi.ui.page.main

import android.view.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fphoenixcorneae.happyjoke.R
import com.fphoenixcorneae.happyjoke.ext.noRippleClickable
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.*
import com.fphoenixcorneae.happyjoke.mvi.ui.widget.SystemUiScaffold

/**
 * @desc：我的
 * @date：2023/03/17 10:48
 */
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun MeScreen(
    window: Window? = null
) {
    SystemUiScaffold(window = window) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Grey15)
                .verticalScroll(state = rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    model = R.mipmap.ic_avatar_default,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterStart),
                )
                Column(
                    modifier = Modifier
                        .padding(start = 80.dp)
                        .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "登录/注册",
                        color = Black30,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(text = "快来开始你的创作吧~", color = Grey85, fontSize = 14.sp)
                }
                AsyncImage(
                    model = R.mipmap.ic_arraw_right_grey,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                )
            }
            Row(
                modifier = Modifier.padding(start = 36.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "59", color = Black30, fontSize = 14.sp)
                    Text(text = "关注", color = Grey60, fontSize = 12.sp)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "319", color = Black30, fontSize = 14.sp)
                    Text(text = "粉丝", color = Grey60, fontSize = 12.sp)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "4165", color = Black30, fontSize = 14.sp)
                    Text(text = "乐豆", color = Grey60, fontSize = 12.sp)
                }
            }
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_post),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "帖子", color = Black30, fontSize = 11.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_comment),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "评论", color = Black30, fontSize = 11.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_like),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "赞过", color = Black30, fontSize = 11.sp)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.noRippleClickable { }) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_me_collect),
                            contentDescription = null,
                            modifier = Modifier.size(36.dp),
                        )
                        Text(text = "收藏", color = Black30, fontSize = 11.sp)
                    }
                }
            }
            Image(
                painter = painterResource(id = R.mipmap.ic_me_gongyue_card),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}