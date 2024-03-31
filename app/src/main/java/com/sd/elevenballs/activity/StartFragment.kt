package com.sd.elevenballs.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.apply {
            ObjectAnimator.ofPropertyValuesHolder(
                textElevenBalls,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0F, 1.2F, 1F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0F, 1.2F, 1F)
            ).setDuration(1000L)
                .start()

            buttonStartGame.alpha = 0f
            buttonStartGame.animate().alpha(1f).translationYBy(-50f).setStartDelay(1000).duration =
                1000

            buttonStartGame.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_playFragment)
            }

        }

        return binding.root
    }

}