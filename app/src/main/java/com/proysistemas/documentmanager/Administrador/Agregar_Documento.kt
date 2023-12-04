package com.proysistemas.documentmanager.Administrador

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.auth.FirebaseAuth
import com.proysistemas.documentmanager.R
import com.proysistemas.documentmanager.databinding.ActivityAgregarDocumentoBinding

class Agregar_Documento : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarDocumentoBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarDocumentoBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_agregar_documento)

        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Cargando")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}