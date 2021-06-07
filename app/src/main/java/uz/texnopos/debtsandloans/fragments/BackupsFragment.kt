package uz.texnopos.debtsandloans.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import uz.texnopos.debtsandloans.R
import uz.texnopos.debtsandloans.databinding.FragmentBackupsBinding
import uz.texnopos.debtsandloans.databinding.FragmentOperationsHistoryBinding

class BackupsFragment : Fragment() {

    private lateinit var viewBinding: FragmentBackupsBinding
    var navController: NavController? = null
    //private val args: OperationsHistoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentBackupsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewBinding.btnBack.setOnClickListener {

        }
    }

}