package com.sd.elevenballs.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

sealed interface UiText {
    data class Res(@StringRes val id: Int, val args: List<Any> = emptyList()) : UiText
    data class Raw(val value: String) : UiText
}

fun Fragment.resolve(text: UiText?): String = when (text) {
    is UiText.Res -> getString(text.id, *text.args.toTypedArray())
    is UiText.Raw -> text.value
    null -> ""
}

fun UiText.asString(context: Context): String = when (this) {
    is UiText.Raw -> value
    is UiText.Res -> {
        val resolvedArgs = args.map { arg ->
            when (arg) {
                is UiText -> arg.asString(context)
                else -> arg
            }
        }.toTypedArray()
        context.getString(id, *resolvedArgs)
    }
}