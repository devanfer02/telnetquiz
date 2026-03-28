package com.example.telnetquiz.constants

import androidx.annotation.DrawableRes
import com.example.telnetquiz.R
import kotlin.math.absoluteValue

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

    val maleAvatars: List<Int> = listOf(
        R.drawable.avatar_04,
        R.drawable.avatar_06,
        R.drawable.avatar_08,
        R.drawable.avatar_09,
        R.drawable.avatar_12,
        R.drawable.avatar_14,
        R.drawable.avatar_15,
    )

    val femaleAvatars: List<Int> = listOf(
        R.drawable.avatar_02,
        R.drawable.avatar_03,
        R.drawable.avatar_05,
        R.drawable.avatar_07,
        R.drawable.avatar_10,
        R.drawable.avatar_11,
        R.drawable.avatar_13,
    )

    @DrawableRes
    fun getAvatarResId(index: Int): Int? {
        return avatarList.getOrNull(index)
    }

    @DrawableRes
    fun getRandomAvatarIndex(gender: Boolean?): Int {
        val list = if (gender == true) maleAvatars else femaleAvatars
        val resId = list.random()
        return avatarList.indexOf(resId)
    }

    @DrawableRes
    fun getDefaultAvatarResId(gender: Boolean?, seed: String): Int {
        val list = if (gender == true) maleAvatars else femaleAvatars
        val index = seed.hashCode().absoluteValue % list.size
        return list[index]
    }
}
