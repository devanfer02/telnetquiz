package com.example.telnetquiz.constants

import androidx.annotation.DrawableRes
import com.example.telnetquiz.R

object AvatarConstants {
    val avatarList: List<Int> = listOf(
        R.drawable.avatar_02,
        R.drawable.avatar_03,
        R.drawable.avatar_04,
        R.drawable.avatar_05,
        R.drawable.avatar_06,
        R.drawable.avatar_07,
        R.drawable.avatar_08,
        R.drawable.avatar_09,
        R.drawable.avatar_10,
        R.drawable.avatar_11,
        R.drawable.avatar_12,
        R.drawable.avatar_13,
        R.drawable.avatar_14,
        R.drawable.avatar_15,
    )

    @DrawableRes
    fun getAvatarResId(index: Int): Int? {
        return avatarList.getOrNull(index)
    }
}
