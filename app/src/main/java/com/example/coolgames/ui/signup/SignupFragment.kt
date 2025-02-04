package com.example.coolgames.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.coolgames.R
import com.example.coolgames.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this).get(SignupViewModel::class.java)
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textDashboard
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        val navController = findNavController()
        binding.btnSignup.setOnClickListener(View.OnClickListener {
            var email = binding.etEmail.text.toString()
            var password = binding.etPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty())
                signupUser(email = email, password = password)
            else {
                Toast.makeText(
                    context,
                    "Enter credentials ",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        binding.btnLogin.setOnClickListener(View.OnClickListener {
            navController.navigate(R.id.loginFragment)
        })
        return root
    }

    private fun signupUser(email: String, password: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val userEmail = user?.email
                    Toast.makeText(context, "Signup successful $userEmail", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.action_signupFragment_to_welcomeFragment)
                } else {
                    Toast.makeText(
                        context, "Signup failed: ${task.exception?.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Signup"
    }
}