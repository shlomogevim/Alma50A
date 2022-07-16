package com.sg.alma50a.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sg.alma50a.R
import com.sg.alma50a.databinding.ActivityLoginBinding
import com.sg.alma50a.modeles.User
import com.sg.alma50a.utilities.BaseActivity
import com.sg.alma50a.utilities.Constants
import com.sg.alma50a.utilities.Constants.EXTRA_USER_DETAILS
import com.sg.alma50a.utilities.Constants.SHARPREF_CURRENT_USER_NAME
import com.sg.alma50a.utilities.FirestoreClass

class LoginActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


         pref = getSharedPreferences(Constants.SHARPREF_ALMA, Context.MODE_PRIVATE)
        //   DemiData()

        binding.registerBtn.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)

    }

    private fun DemiData() {
        binding.etEmail.setText("ta@ta.com")
        binding.etPassword.setText("aaaaaa")
    }


    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }



    private fun logInRegisteredUser() {


        if (validateLoginDetails()) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Get the text from editText and trim the space
            val email = binding.etEmail.text.toString().trim { it <= ' ' }
            val password = binding.etPassword.text.toString().trim { it <= ' ' }
          logi("login 64    ======>  email=$email  password=$password")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FirestoreClass().getUserDetails(this)
                        logi("login 71 email=$email  password=$password")

                         pref.edit().putString(SHARPREF_CURRENT_USER_NAME, null).toString()
//                          pref.edit().putInt(Constants.SHARPREF_CURRENT_POST_POSITION, position).apply()

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()
        pref.edit().putString(SHARPREF_CURRENT_USER_NAME, user.userName).apply()
        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        finish()
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_login -> {
                    logInRegisteredUser()
                }
                R.id.registerBtn -> {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
                R.id.tv_forgot_password -> {
                    startActivity(Intent(this, ForgetPasswordActivity::class.java))
                }
            }

        }
    }



}
