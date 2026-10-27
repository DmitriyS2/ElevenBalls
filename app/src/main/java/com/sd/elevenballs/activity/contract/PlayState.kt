package com.sd.elevenballs.activity.contract

import com.sd.elevenballs.enums.Difficulty
import com.sd.elevenballs.model.Ball
import com.sd.elevenballs.util.UiText

data class PlayState (
    val balls: List<Ball> = emptyList(),
    val countBalls: Int = START_COUNT_BALLS,
    val difficulty: Difficulty = Difficulty.EASY,
    val isCompWin: Boolean = false,
    val textAnswer: UiText? = null,
    val textCount: UiText? = null,
    val isButtonsCountBallsEnabled: Boolean = true,
)

const val START_COUNT_BALLS = 11