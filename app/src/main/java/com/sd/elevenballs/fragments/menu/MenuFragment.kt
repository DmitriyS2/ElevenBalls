package com.sd.elevenballs.fragments.menu

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sd.elevenballs.R
import com.sd.elevenballs.databinding.FragmentMenuBinding

class MenuFragment() : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentMenuBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        val title = arguments?.getString(ARG_TITLE).orEmpty()
        val rules = arguments?.getString(ARG_RULES).orEmpty()

        binding.fragmentRules.text = rules
        builder.setView(binding.root)
        return builder
            .setIcon(R.drawable.info_24)
            .setTitle(getString(R.string.rules_title, title))
            .create()
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_RULES = "arg_rules"

        fun newInstance(title: String, rules: String): MenuFragment {
            return MenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_RULES, rules)
                }
            }
        }
    }
}