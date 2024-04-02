package com.sd.elevenballs.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sd.elevenballs.R
import com.sd.elevenballs.activity.EndFragment.Companion.textArg
import com.sd.elevenballs.activity.EndFragment.Companion.winner
import com.sd.elevenballs.adapter.BallAdapter
import com.sd.elevenballs.databinding.FragmentPlayBinding
import com.sd.elevenballs.dto.Ball

class PlayFragment : Fragment() {

    var fragmentBinding: FragmentPlayBinding? = null
    private val arrayColor = arrayOf(
        R.drawable.ball_blue_24,
        R.drawable.ball_green_24,
        R.drawable.ball_orange_24,
        R.drawable.ball_yellow_24
    )

    private var countBall = 11
    private var isWinComp = false
    private val adapter = BallAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlayBinding.inflate(inflater, container, false)
        fragmentBinding = binding

        startGame()

           binding.rcBalls.layoutManager = GridLayoutManager(activity, 11)
        binding.rcBalls.adapter = adapter

        binding.buttonForNumber.setOnClickListener {
            val text = binding.editNumber.text.toString()
            if (!binding.editNumber.text.isNullOrEmpty() && text.length == 1) {
                val numberInput = binding.editNumber.text.toString().toInt()
                if (numberInput in 1..3) {
                    if ((countBall - numberInput) >= 0) {
                        binding.editNumber.setText("")
                        Thread.sleep(500)
                        elevenBalls(numberInput)
                    } else {
                        binding.editNumber.setText("")
                        binding.textAnswer.text = getString(R.string.number_bigger_stock)
                    }

                } else {
                    binding.editNumber.setText("")
                    binding.textAnswer.text = getString(R.string.wrong_number)
                }
            }
        }

        binding.apply {
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.menu_balls_options)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.newGameBalls -> {
                                startGame()
                                true
                            }

                            R.id.rulesBalls -> {
                                val menuDialog = MenuFragment(getString(R.string.name_game), getString(R.string.rules_balls))
                                val manager = childFragmentManager
                                menuDialog.show(manager, "MENU_BALLS")
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    private fun startGame() {
        adapter.ballList.clear()
        fragmentBinding?.apply {
            textAnswer.text = getString(R.string.your_move)
            fillField()
            countBall = 11
            isWinComp = false
            textCount.text = getString(R.string.stock, countBall)
        }
    }

    private fun fillField() {
        for (index1 in 0..10) {
            val ball = Ball(arrayColor[(arrayColor.indices).random()])
            adapter.addBall(ball)
        }
    }

    private fun elevenBalls(number:Int) {
        deleteBall(number)
        var numberBall = 0
        var shar = "шар"
        when(countBall){
            1 -> {
                Thread.sleep(1000)
                findNavController()
                    .navigate(R.id.action_playFragment_to_endFragment,
                        Bundle().apply {
                            textArg = ""
                            winner = "me"
                        })
            }
            2, 6 -> {
                numberBall = 1
                deleteBall(numberBall)
            }
            3, 7 -> {
                numberBall = 2
                deleteBall(numberBall)
                 shar = "шара"
            }
            4, 8 -> {
                numberBall = 3
                deleteBall(numberBall)
            }
            5, 9, 10, 11 -> {
                numberBall = (1..3).random()
                deleteBall(numberBall)
            }
        }
            fragmentBinding?.textAnswer?.text = getString(R.string.i_removed, numberBall, shar)
    }

    private fun deleteBall(count:Int) {
        for(i in 1..count){
            adapter.removeBall()
            countBall--
            if(countBall==0) {
                Thread.sleep(2000)
                findNavController()
                    .navigate(R.id.action_playFragment_to_endFragment,
                        Bundle().apply {
                            textArg = getString(R.string.dont_be_upset)
                            winner = "comp"
                        })
            }
        }
        fragmentBinding?.textCount?.text = getString(R.string.stock, countBall)
    }

}