package uz.texnopos.debtsandloans.sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import uz.texnopos.debtsandloans.MainActivity
import uz.texnopos.debtsandloans.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityRegisterBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnRegister.setOnClickListener {
            if(viewBinding.etLoginEmail.text.toString().isNotEmpty() && viewBinding.etLoginPassword.text.toString().isNotEmpty()){
                viewBinding.loading.visibility = View.VISIBLE
                mAuth.createUserWithEmailAndPassword(viewBinding.etLoginEmail.text.toString(), viewBinding.etLoginPassword.text.toString())
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            val user = mAuth.currentUser
                            viewBinding.loading.visibility = View.GONE
                            updateUI(user)
                        } else {
                            Toast.makeText(this, "Authentication is failed !", Toast.LENGTH_LONG).show()
                            viewBinding.loading.visibility = View.GONE
                        }
                    }
            }
        }
    }
    private fun updateUI(user: FirebaseUser?){
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}