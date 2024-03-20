package com.example.challenge

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.challenge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        try{
            sharedPreferences = getSharedPreferences("challenge", MODE_PRIVATE)
            val savedUsername = sharedPreferences.getString("username", "")
            // val savedPassword = sharedPreferences.getString("password", "") // No es seguro guardar la contraseña
            val remember = sharedPreferences.getBoolean("remember", false)
            _binding.editUsername.setText(savedUsername)
            // _binding.editPassword.setText(savedPassword) // No es seguro guardar la contraseña
            _binding.checkBox.isChecked = remember
        } catch (e: Exception){
            Toast.makeText(this, "Error al cargar las preferencias", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        setupObservers()
        _binding.btnLogin.setOnClickListener {
            _binding.btnLogin.isEnabled = false
            viewModel.login(
                _binding.editUsername.text.toString(),
                _binding.editPassword.text.toString()
            )
        }
    }

    private fun setupObservers() {
        viewModel.activityState.observe(this) {
            when (it) {
                ActivityStates.Idle -> {
                    _binding.progressLoader.visibility = View.GONE
                    _binding.btnLogin.isEnabled = true
                }
                ActivityStates.Loading -> {
                    _binding.progressLoader.visibility = View.VISIBLE
                }
                ActivityStates.Success -> {
                    val editor = sharedPreferences.edit()
                    if (_binding.checkBox.isChecked) {
                        editor.putBoolean("remember", true)
                        editor.putString("username", _binding.editUsername.text.toString())
                        // editor.putString("password", _binding.editPassword.text.toString()) // No es seguro guardar la contraseña
                        editor.apply()
                    } else {
                        editor.putString("username", "")
                        // editor.putString("password", "") // No es seguro guardar la contraseña
                        editor.putBoolean("remember", false)
                        editor.apply()
                    }
                    _binding.progressLoader.visibility = View.GONE
                    Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()
                    _binding.btnLogin.isEnabled = true
                }
                is ActivityStates.Error -> {
                    _binding.progressLoader.visibility = View.GONE
                    Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()
                    _binding.btnLogin.isEnabled = true
                }
            }
        }
    }
}
