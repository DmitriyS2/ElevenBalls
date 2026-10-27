package com.sd.elevenballs.fragments.endScreen

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.FragmentEndBinding
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import com.sd.elevenballs.activity.MainViewModel

class EndFragment : Fragment() {

    private var _binding: FragmentEndBinding? = null
    private val binding: FragmentEndBinding
        get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEndBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val message =
            if (viewModel.state.value.isCompWin) getString(R.string.dont_be_upset) else getString(R.string.congratulations)
        var image: Int
        when (viewModel.state.value.isCompWin) {
            false -> {
                image = R.drawable.victory
                binding.buttonShare.visibility = View.VISIBLE
            }

            true -> {
                image = R.drawable.pity
                binding.buttonShare.visibility = View.GONE
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
            val uri = path.toUri()
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, getString(R.string.victory_message))
                type = "image/*"
            }

            val shareIntent = Intent.createChooser(intent, getString(R.string.chooser_share_post))
            startActivity(shareIntent)
        }

        binding.buttonNewGame.setOnClickListener {
            findNavController().navigate(R.id.action_endFragment_to_startFragment)
        }
    }
}