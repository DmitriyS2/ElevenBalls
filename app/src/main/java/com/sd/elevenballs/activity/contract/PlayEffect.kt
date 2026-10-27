package com.sd.elevenballs.activity.contract

import com.sd.elevenballs.util.UiText

sealed interface PlayEffect {

    data object ShowRules : PlayEffect
    data object ShowDifficultyDialog : PlayEffect
    data object NavigateToEnd : PlayEffect
    data class ShowToast(val text: UiText) : PlayEffect
}