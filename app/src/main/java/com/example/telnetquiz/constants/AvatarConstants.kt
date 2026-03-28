package com.example.telnetquiz.constants

import androidx.annotation.DrawableRes
import com.example.telnetquiz.R
import kotlin.math.absoluteValue

object AvatarConstants {
    val maleAvatars: List<Int> = listOf(
        R.drawable.avatar_male_1,
        R.drawable.avatar_male_2,
        R.drawable.avatar_male_3,
        R.drawable.avatar_male_4,
        R.drawable.avatar_male_5,
        R.drawable.avatar_male_6,
        R.drawable.avatar_male_7,
    )

    val femaleAvatars: List<Int> = listOf(
        R.drawable.avatar_female_1,
        R.drawable.avatar_female_2,
        R.drawable.avatar_female_3,
        R.drawable.avatar_female_4,
        R.drawable.avatar_female_5,
        R.drawable.avatar_female_6,
        R.drawable.avatar_female_7,
    )

    val avatarList: List<Int> = maleAvatars + femaleAvatars

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
