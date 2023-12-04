package com.proysistemas.documentmanager.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.proysistemas.documentmanager.MainActivity

import com.proysistemas.documentmanager.databinding.ActivityAgregarCategoriaBinding

class Agregar_Categoria : AppCompatActivity() {

    private lateinit var  binding: ActivityAgregarCategoriaBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var  progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAgregarCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.AgregarCateooriaBD.setOnClickListener{
            validarDatos()
        }

    }
    private var categoria = ""
    private fun validarDatos() {
        categoria = binding.EtCategoria.text.toString().trim()
        if(categoria.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese una categoria", Toast.LENGTH_SHORT).show()
        }else{
            AgregarCategoriaBD()
        }
    }

    private fun AgregarCategoriaBD() {
        progressDialog.setMessage("Agregando Categoria")
        progressDialog.show()

        val tiempo = System.currentTimeMillis()

        val hashMap = HashMap<String,Any>()
        hashMap["id"]= "$tiempo"
        hashMap["categoria"]=categoria
        hashMap["tiempo"]= tiempo
        hashMap["uid"] = "${firebaseAuth.uid}"

        // Referencia BD, crear categoria BD
        val ref = FirebaseDatabase.getInstance().getReference("Categoria")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Se Agrego categoria", Toast.LENGTH_SHORT).show()
                binding.EtCategoria.setText("")
                startActivity(Intent(this@Agregar_Categoria, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener{e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "No se agregó la categoria debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}