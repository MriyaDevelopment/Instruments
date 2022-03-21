package com.decorator1889.instruments.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.decorator1889.instruments.MainActivity
import com.decorator1889.instruments.R
import com.decorator1889.instruments.databinding.DialogExitBinding
import com.decorator1889.instruments.fragments.ProfileFragmentDirections
import com.decorator1889.instruments.util.Constants

class ExitDialog: DialogFragment() {

    private lateinit var binding: DialogExitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogExitBinding.inflate(inflater, container, false).apply {
        binding = this
        setListeners()
        Log.d(Constants.EXIT_TAG, "ExitDialog created")
    }.root

    private fun setListeners() {
        binding.run {
            no.setOnClickListener {
                dismiss()
            }
            yes.setOnClickListener {
                logOut()
            }
        }
    }

    private fun logOut() {
        (activity as MainActivity).userLogOut()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppDialogTheme)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = ConstraintLayout(requireContext())
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val dialog = Dialog(requireContext())
        dialog.window?.attributes?.windowAnimations = R.style.AnimationDialog
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }
}