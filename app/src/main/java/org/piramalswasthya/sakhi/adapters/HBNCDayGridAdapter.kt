package org.piramalswasthya.sakhi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.piramalswasthya.sakhi.databinding.RvItemIconHbncBinding
import org.piramalswasthya.sakhi.model.HbncIcon
import org.piramalswasthya.sakhi.model.ImmunizationIcon

class HBNCDayGridAdapter (private val clickListener: HbncIconClickListener) :
    ListAdapter<HbncIcon, HBNCDayGridAdapter.IconViewHolder>(ImmunizationIconDiffCallback) {
    object ImmunizationIconDiffCallback : DiffUtil.ItemCallback<HbncIcon>() {
        override fun areItemsTheSame(oldItem: HbncIcon, newItem: HbncIcon) =
            oldItem.count == newItem.count

        override fun areContentsTheSame(oldItem: HbncIcon, newItem: HbncIcon) =
            (oldItem == newItem)

    }


    class IconViewHolder private constructor(private val binding: RvItemIconHbncBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) : IconViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RvItemIconHbncBinding.inflate(layoutInflater,parent,false)
                return IconViewHolder(binding)
            }
        }

        fun bind(item: HbncIcon, clickListener: HbncIconClickListener){
            binding.homeIcon = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IconViewHolder.from(parent)

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class HbncIconClickListener(val selectedListener: (hhId: Long,benId: Long, count : Int ) -> Unit) {
        fun onClicked(icon : HbncIcon) = selectedListener(icon.hhId, icon.benId, icon.count)

    }
}