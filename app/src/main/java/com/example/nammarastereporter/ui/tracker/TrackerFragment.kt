package com.example.nammarastereporter.ui.tracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nammarastereporter.R
import com.example.nammarastereporter.databinding.FragmentTrackerBinding

class TrackerFragment : Fragment() {

    private var _binding: FragmentTrackerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TrackerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            val ticketId = binding.etTicketId.text.toString().trim()
            if (ticketId.isEmpty()) {
                binding.tilTicketId.error = "Please enter a Ticket ID"
                return@setOnClickListener
            }
            binding.tilTicketId.error = null
            viewModel.searchByTicketId(ticketId)
        }

        binding.btnViewHistory.setOnClickListener {
            findNavController().navigate(R.id.action_trackerFragment_to_historyFragment)
        }

        // Observe search results
        viewModel.searchResult.observe(viewLifecycleOwner) { report ->
            if (report != null) {
                binding.cardResult.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.GONE

                binding.tvResultTicketId.text = report.ticketId
                binding.tvResultIssueType.text = when (report.issueType) {
                    "POTHOLE" -> "🕳️ Pothole"
                    "STREETLIGHT" -> "💡 Broken Streetlight"
                    else -> report.issueType
                }
                binding.tvResultLocation.text = String.format(
                    "📍 %.6f, %.6f", report.latitude, report.longitude
                )
                binding.tvResultTimestamp.text = "🕐 ${report.timestamp}"
                binding.tvResultReporterName.text = report.userName
                binding.tvResultReporterPhone.text = report.userPhone
                binding.tvResultStatus.text = "📋 ${report.status}"

                // Style status chip
                val statusColor = when (report.status) {
                    "Pending" -> com.google.android.material.R.color.design_default_color_secondary
                    "Resolved" -> android.R.color.holo_green_dark
                    else -> com.google.android.material.R.color.design_default_color_primary
                }
            }
        }

        // Observe not found state
        viewModel.notFound.observe(viewLifecycleOwner) { notFound ->
            if (notFound) {
                binding.cardResult.visibility = View.GONE
                binding.tvNotFound.visibility = View.VISIBLE
            }
        }

        // Observe loading state
        viewModel.isSearching.observe(viewLifecycleOwner) { isSearching ->
            binding.progressBar.visibility = if (isSearching) View.VISIBLE else View.GONE
            binding.btnSearch.isEnabled = !isSearching
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
