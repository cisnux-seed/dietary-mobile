package dev.cisnux.dietarytestxml.presentation.healthprofile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.cisnux.dietarytestxml.databinding.FragmentHealthProfileDialogBinding

class HealthProfileDialog(
    private val onUpdate: () -> Unit,
    private val onDone: () -> Unit,
) : DialogFragment() {
    private var _binding: FragmentHealthProfileDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHealthProfileDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            doneBtn.setOnClickListener {
                onDone()
                dialog?.dismiss()
            }
            updateBtn.setOnClickListener {
                onUpdate()
                dialog?.dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireActivity())
            .show()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
