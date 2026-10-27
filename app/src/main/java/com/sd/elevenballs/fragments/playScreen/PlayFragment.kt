package com.sd.elevenballs.fragments.playScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sd.elevenballs.R
import com.sd.elevenballs.activity.MainViewModel
import com.sd.elevenballs.adapter.BallAdapter
import com.sd.elevenballs.databinding.FragmentPlayBinding
import com.sd.elevenballs.enums.Difficulty
import com.sd.elevenballs.activity.contract.PlayEffect
import com.sd.elevenballs.activity.contract.PlayEvent
import com.sd.elevenballs.activity.contract.START_COUNT_BALLS
import com.sd.elevenballs.util.asString
import com.sd.elevenballs.util.resolve
import kotlinx.coroutines.launch

class PlayFragment : Fragment() {

    private var _binding: FragmentPlayBinding? = null
    private val binding: FragmentPlayBinding
        get() = _binding!!

    private val adapter = BallAdapter()

    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectEvent()
        collectEffect()

        binding.apply {
            rcBalls.layoutManager = GridLayoutManager(activity, START_COUNT_BALLS)
            rcBalls.adapter = adapter

            button1.setOnClickListener {
                viewModel.onEvent(PlayEvent.OnClickButtonBall(1))
            }
            button2.setOnClickListener {
                viewModel.onEvent(PlayEvent.OnClickButtonBall(2))
            }
            button3.setOnClickListener {
                viewModel.onEvent(PlayEvent.OnClickButtonBall(3))
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_balls_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.difficulty -> {
                                viewModel.onEvent(PlayEvent.OnClickChangeDifficulty)
                                true
                            }

                            R.id.newGameBalls -> {
                                viewModel.onEvent(PlayEvent.OnClickNewGame)
                                true
                            }

                            R.id.rulesBalls -> {
                                viewModel.onEvent(PlayEvent.OnClickShowRules)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun collectEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.setBalls(state.balls)
                    binding.apply {
                        button1.isEnabled = state.isButtonsCountBallsEnabled
                        button2.isEnabled = state.isButtonsCountBallsEnabled
                        button3.isEnabled = state.isButtonsCountBallsEnabled
                        textAnswer.text = state.textAnswer?.asString(requireContext())
                        textCount.text = resolve(state.textCount)
                    }
                }
            }
        }
    }

    private fun collectEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        PlayEffect.ShowRules -> showRulesDialog()
                        PlayEffect.ShowDifficultyDialog -> showDifficultyDialog()
                        PlayEffect.NavigateToEnd -> navigateToEndScreen()
                        is PlayEffect.ShowToast -> showToast(resolve(effect.text))
                    }
                }
            }
        }
    }

    private fun navigateToEndScreen() {
        findNavController()
            .navigate(R.id.action_playFragment_to_endFragment)
    }

    fun showRulesDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setIcon(R.drawable.info_24)
            .setTitle(getString(R.string.rules_title, getString(R.string.name_game)))
            .setMessage(R.string.rules_balls)
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    fun showDifficultyDialog() {
        val itemsDifficulty: Array<String> =
            Difficulty.entries.map { getString(it.displayNameRes) }.toTypedArray()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.difficult_title)
            .setSingleChoiceItems(itemsDifficulty, viewModel.state.value.difficulty.index) { dialog, which ->
                viewModel.onEvent(PlayEvent.ChangeDifficulty(which))
                dialog.dismiss()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}