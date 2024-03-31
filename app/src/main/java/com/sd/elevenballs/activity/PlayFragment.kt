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
    private val rulesBalls =
        "    Перед Вами 11 шаров. Нужно убирать по 1-3 шара за раз.\n\n    Проигрывает тот, кто убирает последний шар"
    private val titleBalls = "Eleven Balls"

    private var countBall = 11
    private var inputBall = 0
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
                        Thread.sleep(1000)
                        elevenBalls(numberInput)
                    } else {
                        binding.editNumber.setText("")
                        binding.textAnswer.text = "Введенное число больше остатка"
                    }

                } else {
                    binding.editNumber.setText("")
                    binding.textAnswer.text = "Неправильно набрано число!\nПопробуй заново"
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
                                val menuDialog = MenuFragment(titleBalls, rulesBalls)
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
           // groupGame.visibility = View.VISIBLE
        //    textAnswer.gravity = 0
            textAnswer.text = "Твой ход"
            fillField()
            countBall = 11
            inputBall = 0
            isWinComp = false
            textCount.text = "Осталось $countBall"
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

        var textAnswer = ""
        when(countBall){
            1 -> {
                Thread.sleep(1500)
                findNavController()
                    .navigate(R.id.action_playFragment_to_endFragment,
                        Bundle().apply {
                            textArg = "Поздравляю!!!\nС победой!"
                            winner = "me"
                        })
//                textAnswer = "Поздравляю!!!\nС победой!"
//                countBall=0
//                endGame(textAnswer)
            }
            2, 6 -> {
                //countBall --
                deleteBall(1)
                textAnswer = "Я убрал 1 шар"
      //          Thread.sleep(2000)
            }
            3, 7 -> {
                //countBall -= 2
                deleteBall(2)
                textAnswer = "Я убрал 2 шарa"
       //         Thread.sleep(2000)
            }
            4, 8 -> {
                //countBall -= 3
                deleteBall(3)
                textAnswer = "Я убрал 3 шарa"
        //        Thread.sleep(2000)
            }
            5, 9, 10, 11 -> {
                val randomNumber = (1..3).random()
                val shar = if(randomNumber==1) "шар" else "шара"
                //countBall -= randomNumber
                deleteBall(randomNumber)
                textAnswer = "Я убрал $randomNumber $shar"
             //   Thread.sleep(2000)
            }
        }

//        if (countBall==1) {
//            winComp()
//        } else if(countBall!=0) {
            fragmentBinding?.textAnswer?.text = textAnswer
    //    Thread.sleep(2000)
  //      }
    }

    private fun deleteBall(count:Int) {
        for(i in 1..count){
            adapter.removeBall()
            countBall--
            if(countBall==0) {
          //      winComp()
                Thread.sleep(2000)
                findNavController()
                    .navigate(R.id.action_playFragment_to_endFragment,
                        Bundle().apply {
                            textArg = "Не расстраивайся!\nПовезет в другой раз"
                            winner = "comp"
                        })
            }
        }
        fragmentBinding?.textCount?.text = "Осталось $countBall"
    }

//    private fun winComp() {
//        isWinComp = true
//        endGame("Не расстраивайся!\nПовезет в другой раз")
//    }

//    private fun endGame(text:String) {
//        binding.groupGame.visibility = View.GONE
//        fragmentBinding?.textAnswer?.gravity = 1
//        binding.textAnswer.text = text
//        if(!isWinComp) {
//            binding.pictureEnd.visibility = View.VISIBLE
//            //     binding.pictureEnd.setImageResource(arrayEnd[arrayEnd.indices.random()])
//        }
//    }

//    private fun exitGame() {
//        val i = Intent()
//        i.putExtra("result", "В какую игру будем играть?")
//        setResult(AppCompatActivity.RESULT_OK, i)
//        finish()
//    }

}