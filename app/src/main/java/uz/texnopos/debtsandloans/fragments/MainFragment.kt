package uz.texnopos.debtsandloans.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.debtsandloans.FirebaseHelper
import uz.texnopos.debtsandloans.R
import uz.texnopos.debtsandloans.databinding.DialogAddTransactionBinding
import uz.texnopos.debtsandloans.databinding.FragmentMainBinding
import uz.texnopos.debtsandloans.item_space.MarginItemDecoration
import uz.texnopos.debtsandloans.list.Model
import uz.texnopos.debtsandloans.list.MyListAdapter
import java.util.*

class MainFragment : Fragment(R.layout.fragment_main), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val dbHelper = FirebaseHelper()
    private val model = Model()

    private lateinit var viewBinding: FragmentMainBinding
    private lateinit var transactionBinding: DialogAddTransactionBinding

    private val myAdapter: MyListAdapter = MyListAdapter()
    private var navController: NavController? = null

    private var day = 0; private var month = 0; private var year = 0; private var hour = 0; private var minute = 0
    private var savedDay = 0; private var savedMonth = 0; private var savedYear = 0; private var savedHour = 0; private var savedMinute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentMainBinding.bind(view)
        navController = Navigation.findNavController(view)
        viewBinding.recyclerView.adapter = myAdapter
        viewBinding.recyclerView.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_list)))

        // action bar values view clicked event code here ...
        viewBinding.etValue.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_operationsHistory)
        }

        viewBinding.floatingActionButton.setOnClickListener {
            val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
            val dialog = AlertDialog.Builder(requireContext()).setView(addDialogView).show()
            transactionBinding = DialogAddTransactionBinding.bind(addDialogView)

            // ADD button clicked event here ...
            transactionBinding.tvAdd.setOnClickListener {
                dbHelper.eventChangeListener(
                    {
                        myAdapter.add(it)
                    },
                    {
                        //myAdapter.modify(it)
                    },
                    {
                        myAdapter.remove(it)
                    },
                    {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                )

                db.collection("users/${mAuth.currentUser?.uid!!}/contacts").whereEqualTo("contactName", transactionBinding.etContact.text.toString())
                    .get()
                    .addOnSuccessListener {
                        if (it.isEmpty) {
                            val map: MutableMap<String, Any?> = mutableMapOf()
                            map["contactName"] = transactionBinding.etContact.text.toString()
                            db.collection("users").document(mAuth.currentUser?.uid!!)
                                .collection("contacts").document(transactionBinding.etContact.text.toString()).set(map)
                                .addOnSuccessListener {
                                    dbHelper.addTransaction(model,
                                        {
                                            Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show()
                                        },{
                                            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }

                        } else {
                            dbHelper.addTransaction(model,
                                {
                                    Toast.makeText(requireContext(), "updated", Toast.LENGTH_SHORT).show()
                                },{
                                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                dialog.dismiss()
            }

            // SUB button clicked event here ...
            transactionBinding.tvSub.setOnClickListener {
                if (transactionBinding.etContact.text.toString() != "" && transactionBinding.etValue.text.toString() != "") {
                    myAdapter.addUser(0, transactionBinding.etContact.text.toString(), "-${transactionBinding.etValue.text.toString()}".toInt(),
                        transactionBinding.etComment.text.toString(), transactionBinding.tvCalendar.text.toString())
                } else {
                    Toast.makeText(requireContext(), "Title or/and Description Empty !", Toast.LENGTH_LONG).show()
                }
                dialog.dismiss()
            }

            //CANCEL button clicked event here ...
            transactionBinding.tvCancel.setOnClickListener {
                Toast.makeText(requireContext(), "Cancel button clicked !", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            // CALENDAR text view clicked event here ...
            transactionBinding.tvCalendar.setOnClickListener {
                pickDate()
            }
        }

        // action bar options menu view clicked event code here ...
        myAdapter.menuOptionsItemClickListener { view, position ->
            val popupMenu = PopupMenu(requireContext(), view)
            val menuInflater = popupMenu.menuInflater
            menuInflater.inflate(R.menu.item_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.pay_full_amount -> {
                        Toast.makeText(requireContext(), "Play Off Current Sum selected!", Toast.LENGTH_SHORT).show()
                    }
                    R.id.change_balance -> {
                        Toast.makeText(requireContext(), "Change balance selected!", Toast.LENGTH_SHORT).show()
                    }
                    R.id.operation_history -> {
                        Toast.makeText(requireContext(), "Operation History selected", Toast.LENGTH_SHORT).show()
                    }
                    R.id.??lear_history -> {
                        Toast.makeText(requireContext(), "Clear selected!", Toast.LENGTH_SHORT).show()
                    }
                    R.id.rename -> {
                        Toast.makeText(requireContext(), "Rename selected!", Toast.LENGTH_SHORT).show()
                    }
                    R.id.delete_contact -> {
                        Toast.makeText(requireContext(), "Remove Contact selected!", Toast.LENGTH_SHORT).show()
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }

        // action bar options menu item selected event code here ...
        viewBinding.menuIcon.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), viewBinding.menuIcon)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.add_contact -> {
                        val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_transaction, null)
                        val dialog = AlertDialog.Builder(requireContext()).setView(addDialogView).show()
                        Toast.makeText(requireContext(), "Add Contact selected!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.operation_history -> {
                        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_operationsHistory)
                        Toast.makeText(requireContext(), "Operation History selected!", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.backups -> {
                        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_backupsFragment)
                        Toast.makeText(requireContext(), "Backups selected!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.settings -> {
                        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_settingsFragment)
                        Toast.makeText(requireContext(), "Settings selected!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.search -> {
                        Toast.makeText(requireContext(), "Search selected!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.about -> {
                        val addDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.about, null)
                        val dialog = AlertDialog.Builder(requireContext()).setView(addDialogView).show()
                        Toast.makeText(requireContext(), "About selected!", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popupMenu.inflate(R.menu.menu_icon)
            popupMenu.show()
        }

        // action bar sort icon click event here ...
        viewBinding.sort.setOnClickListener {
            val sortDialog = LayoutInflater.from(requireContext()).inflate(R.layout.sort, null)
            AlertDialog.Builder(requireContext()).setView(sortDialog).show()
        }
    }

    // Calendar view created functions here ...
    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    private fun pickDate() {
        getDateTimeCalendar()
        DatePickerDialog(requireContext(), this, year, month, day).show()
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year
        getDateTimeCalendar()
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
        transactionBinding.tvCalendar.text = "$savedDay-$savedMonth-$savedYear"
    }
}