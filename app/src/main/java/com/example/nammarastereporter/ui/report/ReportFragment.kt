package com.example.nammarastereporter.ui.report

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nammarastereporter.R
import com.example.nammarastereporter.databinding.FragmentReportBinding
import com.example.nammarastereporter.utils.LocationHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import androidx.core.content.ContextCompat

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportViewModel by viewModels()
    private lateinit var locationHelper: LocationHelper
    private var imagePath: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationHelper = LocationHelper(requireContext())


        // Get image path from arguments
        imagePath = arguments?.getString("imagePath") ?: ""

        // Display captured photo
        if (imagePath.isNotEmpty()) {
            val file = File(imagePath)
            if (file.exists()) {
                val bitmap = BitmapFactory.decodeFile(imagePath)
                binding.imgCaptured.setImageBitmap(bitmap)
            }
        }

        // Fetch GPS coordinates
        fetchLocation()

        // Observe location updates
        viewModel.latitude.observe(viewLifecycleOwner) { lat ->
            val lng = viewModel.longitude.value ?: 0.0
            binding.tvLocation.text = String.format("📍 %.6f, %.6f", lat, lng)
        }

        viewModel.longitude.observe(viewLifecycleOwner) { lng ->
            val lat = viewModel.latitude.value ?: 0.0
            binding.tvLocation.text = String.format("📍 %.6f, %.6f", lat, lng)
        }

        // Set current timestamp
        val timestamp = java.text.SimpleDateFormat(
            "dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()
        ).format(java.util.Date())
        binding.tvTimestamp.text = "🕐 $timestamp"

        // Submit button
        binding.btnSubmitReport.setOnClickListener {
            submitReport()
        }

        // Observe report saved event
        viewModel.reportSaved.observe(viewLifecycleOwner) { ticketId ->
            ticketId?.let {
                showSuccessDialog(it)
                viewModel.clearSavedState()
            }
        }

        // Back button
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun fetchLocation() {
        binding.tvLocation.text = "📍 Fetching location..."

        if (!locationHelper.hasLocationPermission()) {
            binding.tvLocation.text = "📍 Location permission not granted"
            Toast.makeText(
                requireContext(),
                "Location permission is needed for accurate reporting.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        locationHelper.getCurrentLocation(
            onSuccess = { location ->
                viewModel.setLocation(location.latitude, location.longitude)
            },
            onFailure = { exception ->
                binding.tvLocation.text = "📍 Unable to get location"
                Toast.makeText(
                    requireContext(),
                    "Could not fetch location: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    private fun submitReport() {
        val name = binding.etReporterName.text.toString().trim()
        val phone = binding.etReporterPhone.text.toString().trim()

        var isValid = true
        if (name.isEmpty()) {
            binding.tilReporterName.error = "Name is required"
            isValid = false
        } else {
            binding.tilReporterName.error = null
        }

        if (phone.isEmpty()) {
            binding.tilReporterPhone.error = "Phone is required"
            isValid = false
        } else {
            binding.tilReporterPhone.error = null
        }

        if (!isValid) return

        val selectedId = binding.rgIssueType.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(requireContext(), "Please select an issue type", Toast.LENGTH_SHORT).show()
            return
        }

        val issueType = when (selectedId) {
            R.id.rbPothole -> "POTHOLE"
            R.id.rbStreetlight -> "STREETLIGHT"
            else -> "UNKNOWN"
        }

        if (imagePath.isEmpty()) {
            Toast.makeText(requireContext(), "No photo captured", Toast.LENGTH_SHORT).show()
            return
        }

        binding.btnSubmitReport.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        viewModel.submitReport(issueType, imagePath, name, phone)
    }

    private fun showSuccessDialog(ticketId: String) {
        binding.btnSubmitReport.isEnabled = true
        binding.progressBar.visibility = View.GONE

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("✅ Report Submitted!")
            .setMessage(
                "Your report has been submitted successfully.\n\n" +
                        "Ticket ID: $ticketId\n\n" +
                        "Please save this ID to track your report."
            )
            .setPositiveButton("Go to Home") { dialog, _ ->
                dialog.dismiss()
                findNavController().navigate(R.id.action_reportFragment_to_homeFragment)
            }
            .setCancelable(false)
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
