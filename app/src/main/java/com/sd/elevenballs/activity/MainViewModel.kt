package com.sd.elevenballs.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sd.elevenballs.R
import com.sd.elevenballs.activity.contract.PlayEffect
import com.sd.elevenballs.activity.contract.PlayEvent
import com.sd.elevenballs.activity.contract.PlayState
import com.sd.elevenballs.activity.contract.START_COUNT_BALLS
import com.sd.elevenballs.enums.Difficulty
import com.sd.elevenballs.model.Ball
import com.sd.elevenballs.util.UiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(PlayState())
    val state: StateFlow<PlayState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PlayEffect>()
    val effect: SharedFlow<PlayEffect> = _effect.asSharedFlow()

    private val colors = listOf(
        R.drawable.ball_blue_24,
        R.drawable.ball_green_24,
        R.drawable.ball_orange_24,
        R.drawable.ball_yellow_24
    )

    init {
        startGame()
    }

    fun onEvent(event: PlayEvent) {
        when (event) {
            is PlayEvent.OnClickButtonBall -> clickButtonBall(event.count)
            PlayEvent.OnClickNewGame -> startGame()
            PlayEvent.OnClickShowRules -> showRules()
            PlayEvent.OnClickChangeDifficulty -> showDifficultyDialog()
            is PlayEvent.ChangeDifficulty -> setDifficulty(event.index)
        }
    }

    private fun startGame() {
        val balls = List(START_COUNT_BALLS) {
            Ball(colorBall = colors.random())
        }
        _state.update {
            it.copy(
                balls = balls,
                textAnswer = UiText.Res(R.string.your_move),
                countBalls = START_COUNT_BALLS,
                isCompWin = false,
                textCount = UiText.Res(R.string.stock, listOf(START_COUNT_BALLS)),
                isButtonsCountBallsEnabled = true,
            )
        }
        viewModelScope.launch {
            _effect.emit(PlayEffect.ShowToast(UiText.Res(R.string.new_game)))
        }
    }

    private fun clickButtonBall(count: Int) {
        viewModelScope.launch {
            delay(500)
            when {
                (_state.value.countBalls - count) == 0 -> {
                    _state.update { it.copy(isCompWin = true) }
                    _effect.emit(PlayEffect.NavigateToEnd)
                    Log.d("MyLog", "VM isCompWin: ${_state.value.isCompWin}")
                }

                (_state.value.countBalls - count) < 0 -> {
                    _state.update { it.copy(textAnswer = UiText.Res(R.string.number_bigger_stock)) }
                }

                else -> {
                    _state.update { it.copy(isButtonsCountBallsEnabled = false) }

                    val shar = when (count) {
                        1 -> R.string.ball_1
                        2 -> R.string.balls_2
                        3 -> R.string.balls_3
                        else -> 0
                    }
                    repeat(count) {
                        delay(1000)
                        _state.update {
                            it.copy(
                                balls = it.balls.dropLast(1),
                                countBalls = it.countBalls - 1,
                                textCount = UiText.Res(R.string.stock, listOf(it.countBalls - 1)),
                                textAnswer = UiText.Res(R.string.you_removed, listOf(UiText.Res(shar)))
                            )
                        }
                    }
                    turnComp()
                }
            }
        }
    }

    private suspend fun turnComp() {
        var numberBall = 0
        val randomDifficulty = (0..100).random()
        val isRandomTurn: Boolean = randomDifficulty in (0.._state.value.difficulty.percent)
        if (isRandomTurn) {
            when (_state.value.countBalls) {
                1 -> {
                    _state.update { it.copy(isCompWin = false) }
                    _effect.emit(PlayEffect.NavigateToEnd)
                }

                2 -> numberBall = 1
                3 -> numberBall = (1..2).random()
                else -> numberBall = (1..3).random()
            }
        } else {
            when (_state.value.countBalls) {
                1 -> {
                    _state.update { it.copy(isCompWin = false) }
                    _effect.emit(PlayEffect.NavigateToEnd)
                }

                2, 6 -> numberBall = 1

                3, 7 -> numberBall = 2

                4, 8 -> numberBall = 3

                5, 9, 10, 11 -> numberBall = (1..3).random()
            }
        }

        val shar = when (numberBall) {
            1 -> R.string.ball_1
            2 -> R.string.balls_2
            3 -> R.string.balls_3
            else -> 0
        }
        repeat(numberBall) {
            delay(1000)
            _state.update {
                it.copy(
                    balls = it.balls.dropLast(1),
                    countBalls = it.countBalls - 1,
                    textCount = UiText.Res(R.string.stock, listOf(it.countBalls - 1)),
                    textAnswer = UiText.Res(R.string.i_removed, listOf(UiText.Res(shar)))
                )
            }
        }
        delay(500)
        _state.update { it.copy(isButtonsCountBallsEnabled = true) }
    }

    private fun showRules() {
        viewModelScope.launch {
            _effect.emit(PlayEffect.ShowRules)
        }
    }

    private fun showDifficultyDialog() {
        viewModelScope.launch {
            _effect.emit(PlayEffect.ShowDifficultyDialog)
        }
    }

    fun setDifficulty(index: Int) {
        val difficulty: Difficulty = when (index) {
            0 -> Difficulty.EASY

            1 -> Difficulty.MEDIUM

            2 -> Difficulty.HARD

            else -> Difficulty.EASY
        }
        _state.update { it.copy(difficulty = difficulty) }
    }
}