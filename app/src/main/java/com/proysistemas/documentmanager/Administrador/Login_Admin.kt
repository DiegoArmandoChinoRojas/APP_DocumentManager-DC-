package com.proysistemas.documentmanager.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.proysistemas.documentmanager.MainActivity
import com.proysistemas.documentmanager.databinding.ActivityLoginAdminBinding


class Login_Admin : AppCompatActivity() {

    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Cargando")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnLoginAdmin.setOnClickListener{
            ValidarDatosAdmin()
        }
    }
    private var correo = ""
    private var contraseña = ""

    private fun ValidarDatosAdmin() {
        correo = binding.EtCorreoAdmin.text.toString().trim()
        contraseña = binding.EtContraAdmin.text.toString().trim()

        if (correo.isEmpty()) {
            binding.EtCorreoAdmin.error = "Ingrese el Correo"
            binding.EtCorreoAdmin.requestFocus()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.EtCorreoAdmin.error = "Correo invalido"
            binding.EtCorreoAdmin.requestFocus()
        }
        else if (contraseña.isEmpty()) {
            binding.EtContraAdmin.error = "Ingrese la contraseña"
            binding.EtContraAdmin.requestFocus()
        }
        else{
            LoginAdmin()
            }
        }
        private fun LoginAdmin() {
            progressDialog.setMessage("Iniciando Sesión")
            progressDialog.show()

            firebaseAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    startActivity(Intent(this,MainActivity::class.java))
                    finishAffinity()
                }
                .addOnFailureListener{ e ->
                    Toast.makeText(applicationContext, "No se pudo Iniciar Sesión debido a ${e.message}", Toast.LENGTH_SHORT).show()

                }
        }
    }
