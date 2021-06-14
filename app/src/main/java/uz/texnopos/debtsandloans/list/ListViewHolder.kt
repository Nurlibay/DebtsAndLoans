package uz.texnopos.debtsandloans.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.debtsandloans.databinding.ListItemBinding

class ListViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun populateModel(model: Model, menuOptionsItemClick: (view: View, position: Int) -> Unit, position: Int){
        binding.contactName.text = model.contactName
        binding.etValue.text = model.amount.toString()
        binding.moreIcon.setOnClickListener {
            menuOptionsItemClick.invoke(binding.moreIcon, position)
        }
    }
}