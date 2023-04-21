package com.fphoenixcorneae.happyjoke.mvi.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.Grey20
import com.fphoenixcorneae.happyjoke.mvi.ui.theme.GreyLine

/**
 * @param hint: 空字符时的提示
 * @param startIcon: 左侧图标;  -1 则不显示
 * @param iconSpacing: 左侧图标与文字的距离; 相当于: drawablePadding
 */
@Composable
fun AuthCodeEditText(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    hint: String = "",
    hintColor: Color = Color.LightGray,
    @DrawableRes startIcon: Int = -1,
    @DrawableRes startCheckedIcon: Int = -1,
    iconSpacing: Dp = 4.dp,
    onRightTextClick: () -> Unit = {},
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    cursorBrush: Brush = SolidColor(MaterialTheme.colorScheme.primary),
) {
    // 焦点, 用于控制左侧Icon、底部分割线状态以及右侧叉号显示
    var hasFocus by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { hasFocus = it.isFocused },
            singleLine = true,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Grey20, RoundedCornerShape(30.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // -1 不显示 左侧Icon
                    if (!hasFocus && startIcon != -1) {
                        Image(
                            painter = painterResource(id = startIcon),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(iconSpacing))
                    } else if (hasFocus && startCheckedIcon != -1) {
                        Image(
                            painter = painterResource(id = startCheckedIcon),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(iconSpacing))
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 60.dp)
                            .weight(1f)
                    ) {
                        // 当空字符时, 显示hint
                        if (text.isEmpty()) {
                            Text(text = hint, color = hintColor, style = textStyle)
                        }
                        // 原本输入框的内容
                        innerTextField()
                    }

                    VerticalDivider(color = GreyLine, thickness = 5.dp)

                    Text(text = "获取验证码", color = hintColor, style = textStyle, modifier = Modifier.width(60.dp))
                }
            }
        )
    }
}
