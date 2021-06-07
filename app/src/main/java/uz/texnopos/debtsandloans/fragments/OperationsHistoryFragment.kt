package uz.texnopos.debtsandloans.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.debtsandloans.R
import uz.texnopos.debtsandloans.databinding.FragmentOperationsHistoryBinding

class OperationsHistoryFragment : Fragment(R.layout.fragment_operations_history) {

    private lateinit var viewBinding: FragmentOperationsHistoryBinding
    var navController: NavController? = null
    //private val args: OperationsHistoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentOperationsHistoryBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewBinding.historyOptions.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), viewBinding.historyOptions)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.export_excel -> {
                        Toast.makeText(requireContext(), "Export for Excel (.csv)...", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.copy_clipboard -> {
                        Toast.makeText(requireContext(), "Copy to Clipboard", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.search -> {
                        Toast.makeText(requireContext(), "Search", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.history_options)
            popupMenu.show()
        }
    }

}