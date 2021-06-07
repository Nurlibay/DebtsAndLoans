package uz.texnopos.debtsandloans.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import uz.texnopos.debtsandloans.R
import uz.texnopos.debtsandloans.databinding.FragmentBackupsBinding
import uz.texnopos.debtsandloans.databinding.FragmentOperationsHistoryBinding
import uz.texnopos.debtsandloans.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var viewBinding: FragmentSettingsBinding
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentSettingsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}