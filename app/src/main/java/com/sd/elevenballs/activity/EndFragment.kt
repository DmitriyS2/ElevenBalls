package com.sd.elevenballs.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.FragmentEndBinding
import com.sd.elevenballs.util.StringArg

class EndFragment : Fragment() {

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
        when (winner) {
            "me" -> {
                image = R.drawable.victory
                binding.buttonShare.visibility = View.VISIBLE
                binding.text1.visibility = View.GONE
            }

            "comp" -> {
                image = R.drawable.smile
                binding.buttonShare.visibility = View.GONE
                binding.text1.visibility = View.VISIBLE
            }
        }

        binding.image1.setImageResource(image)
        binding.text1.text = message

        binding.buttonShare.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, image)
            val path = MediaStore.Images.Media.insertImage(
                activity?.contentResolver,
                bitmap,
                "Title",
                null
            )
            val uri = Uri.parse(path)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, getString(R.string.victory_message))
                type = "image/*"
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