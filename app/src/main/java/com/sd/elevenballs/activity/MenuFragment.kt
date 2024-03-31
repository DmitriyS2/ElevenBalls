package com.sd.elevenballs.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.FragmentMenuBinding

class MenuFragment(private val title:String, private val rules:String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentMenuBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        binding.fragmentRules.text = rules
        builder.setView(binding.root)
        return builder
            .setIcon(R.drawable.info_24)
            .setTitle("Правила игры \n$title")
            .create()
        }
    }



