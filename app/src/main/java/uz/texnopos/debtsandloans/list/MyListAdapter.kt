package uz.texnopos.debtsandloans.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.debtsandloans.databinding.ListItemBinding

class MyListAdapter: RecyclerView.Adapter<ListViewHolder>() {

    private var models: MutableList<Model> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addUser(position: Int, personName: String, summa: Int){
        models.add(position, Model(personName, summa))
        notifyItemInserted(position)
        notifyItemRangeChanged(position, models.size)
    }

    var menuOptionsItemClick: (view: View, position: Int) -> Unit = { _, _ ->  }
    fun menuOptionsItemClickListener(menuItemClick: (view: View, position: Int) -> Unit){
        this.menuOptionsItemClick = menuItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.populateModel(models[position], menuOptionsItemClick, position)
    }

    override fun getItemCount(): Int = models.size
}