package uz.texnopos.debtsandloans

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.debtsandloans.list.Model

class FirebaseHelper {
    private val db = FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    fun eventChangeListener(
        onContactAdded: (model: Model) -> Unit, onContactUpdated: (model: Model) -> Unit,
        onContactDeleted: (model: Model) -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        db.collection("users").document(mAuth.currentUser?.uid!!)
            .collection("contacts")
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
}