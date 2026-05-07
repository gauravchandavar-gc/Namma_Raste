package com.example.nammarastereporter.ui.history

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.nammarastereporter.R
import com.example.nammarastereporter.data.db.ReportEntity
import com.example.nammarastereporter.databinding.ItemReportHistoryBinding
import java.io.File

class HistoryAdapter : ListAdapter<ReportEntity, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    class HistoryViewHolder(private val binding: ItemReportHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(report: ReportEntity) {
            binding.tvHistoryTicketId.text = report.ticketId
            binding.tvHistoryType.text = when (report.issueType) {
                "POTHOLE" -> "🕳️ Pothole"
                "STREETLIGHT" -> "💡 Streetlight"
                else -> report.issueType
            }
            binding.tvHistoryDate.text = report.timestamp
            binding.tvHistoryStatus.text = report.status

            // Load thumbnail if exists
            if (report.imagePath.isNotEmpty()) {
                val file = File(report.imagePath)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(report.imagePath)
                    binding.imgHistoryThumb.setImageBitmap(bitmap)
                }
            }
            
            // Status styling
            when (report.status) {
                "Pending" -> {
                    binding.tvHistoryStatus.setTextColor(binding.root.context.getColor(R.color.status_pending))
                    binding.tvHistoryStatus.setBackgroundResource(R.drawable.bg_status_pending)
                }
                "Resolved" -> {
                    binding.tvHistoryStatus.setTextColor(binding.root.context.getColor(android.R.color.holo_green_dark))
                    binding.tvHistoryStatus.setBackgroundResource(R.drawable.bg_status_resolved)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemReportHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ReportEntity>() {
        override fun areItemsTheSame(oldItem: ReportEntity, newItem: ReportEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReportEntity, newItem: ReportEntity): Boolean {
            return oldItem == newItem
        }
    }
}
