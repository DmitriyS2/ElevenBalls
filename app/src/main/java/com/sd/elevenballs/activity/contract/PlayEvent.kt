package com.sd.elevenballs.activity.contract

sealed interface PlayEvent {

    data class OnClickButtonBall(val count: Int) : PlayEvent
    data object OnClickNewGame: PlayEvent
    data object OnClickShowRules: PlayEvent
    data object OnClickChangeDifficulty: PlayEvent
    data class ChangeDifficulty(val index: Int): PlayEvent
}