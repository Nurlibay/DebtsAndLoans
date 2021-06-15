package uz.texnopos.debtsandloans

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.debtsandloans.list.Model
import java.util.*

class FirebaseHelper {
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    fun eventChangeListener(
        onContactAdded: (model: Model) -> Unit, onContactUpdated: (model: Model) -> Unit,
        onContactDeleted: (model: Model) -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        db.collection("users").document(mAuth.currentUser?.uid!!)
            .collection("contacts")
            // snapshot listener event here ...
            .addSnapshotListener { value, error ->
                if (error != null) {
                    onFailure.invoke(error.localizedMessage)
                    Log.e("Firestore error !", error.localizedMessage!!)
                } else {
                    for (dc in value!!.documentChanges) {
                        val doc = dc.document.toObject(Model::class.java)
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
                                onContactAdded.invoke(doc)
                            }
                            DocumentChange.Type.MODIFIED -> {
                                onContactUpdated.invoke(doc)
                            }
                            DocumentChange.Type.REMOVED -> {
                                onContactDeleted.invoke(doc)
                            }
                        }
                    }
                }
            }
    }

    fun addTransaction(
        model: Model,
        onSuccess: (msg: String?) -> Unit,
        onFailure: (msg: String?) -> Unit
    ){
        val map : MutableMap<String, Any?> = mutableMapOf()
        map["id"] = UUID.randomUUID().toString()
        map["amount"] = model.amount.toString()
        map["comment"] = model.comment
        var contactAmount = 0.0

        db.collection("users").document(mAuth.currentUser?.uid!!)
            .collection("contacts").document(model.contactName)
            .collection("transactions")
            .document(map.getValue("id").toString()).set(map)

            // success event here ...
            .addOnSuccessListener {
                db.collection("users").document(mAuth.currentUser?.uid!!)
                    .collection("contacts").document(model.contactName)
                    .collection("transactions").get()
                    .addOnSuccessListener {
                        onSuccess.invoke("")
                        it.documents.forEach { doc ->
                            contactAmount += doc["amount"].toString().toDouble()
                        }
                        db.collection("users").document(mAuth.currentUser?.uid!!)
                            .collection("contacts").document(model.contactName)
                            .update("amount", contactAmount)
                            .addOnSuccessListener {
                                onSuccess.invoke("updated")
                            }
                    }
            }

            // failure event here ...
            .addOnFailureListener { e ->
                onFailure.invoke(e.localizedMessage)
            }
    }
}