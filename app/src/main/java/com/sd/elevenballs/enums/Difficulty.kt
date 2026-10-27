package com.sd.elevenballs.enums

import androidx.annotation.StringRes
import com.sd.elevenballs.R

enum class Difficulty(val percent: Int, val index: Int, @field:StringRes val displayNameRes: Int) {
    EASY(90, 0, R.string.easy_difficulty),
    MEDIUM(50, 1, R.string.medium_difficulty),
    HARD(10, 2, R.string.hard_difficulty)
}