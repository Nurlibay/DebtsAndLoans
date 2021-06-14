package uz.texnopos.debtsandloans.sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import uz.texnopos.debtsandloans.MainActivity
import uz.texnopos.debtsandloans.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginBinding
    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.btnLogin.setOnClickListener {
            if (viewBinding.etLoginEmail.text!!.isNotEmpty() && viewBinding.etLoginPassword.text!!.isNotEmpty()) {
                viewBinding.loading.visibility = View.VISIBLE
                mAuth.signInWithEmailAndPassword(viewBinding.etLoginEmail.text.toString(), viewBinding.etLoginPassword.text.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            val currentUser = mAuth.currentUser
                            viewBinding.loading.visibility = View.GONE
                            updateUI(currentUser)
                        } else {
                            Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                            viewBinding.loading.visibility = View.GONE
                            //updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(this, "Enter your Email and Password !", Toast.LENGTH_SHORT).show()
            }
        }
        viewBinding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}