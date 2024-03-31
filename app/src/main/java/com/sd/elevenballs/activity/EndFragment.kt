package com.sd.elevenballs.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.FragmentEndBinding
import com.sd.elevenballs.util.StringArg

class EndFragment : Fragment() {

    private val arrayEnd = arrayOf(R.drawable.buket1, R.drawable.sharik1, R.drawable.salut1)

    companion object {
        var Bundle.textArg: String? by StringArg
        var Bundle.winner: String? by StringArg
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEndBinding.inflate(inflater, container, false)

        val message = arguments?.textArg
        val winner = arguments?.winner
        var image = 0
        when(winner) {
            "me" -> {
                image = arrayEnd[arrayEnd.indices.random()]
                binding.buttonShare.visibility = View.VISIBLE
            }
            "comp" -> {
                image = R.drawable.smile
                binding.buttonShare.visibility = View.GONE
            }
        }


        binding.image1.setImageResource(image)
        binding.text1.text = message

        binding.buttonShare.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Можешь меня поздравить!!! Компьютер был обыгран мной в игре ELEVEN BALLS!")
                type = "text/plain"
            }

            val shareIntent =
                Intent.createChooser(intent, getString(R.string.chooser_share_post))

            startActivity(shareIntent)
        }

        binding.buttonNewGame.setOnClickListener {
            findNavController()
                .navigate(R.id.action_endFragment_to_playFragment)
        }

        return binding.root
    }

}