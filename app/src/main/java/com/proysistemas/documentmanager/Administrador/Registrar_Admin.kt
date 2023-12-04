package com.proysistemas.documentmanager.Administrador

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.proysistemas.documentmanager.MainActivity
import com.proysistemas.documentmanager.databinding.ActivityRegistrarAdminBinding


class Registrar_Admin : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarAdminBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Creando la cuenta")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.BtnRegistrarAdmin.setOnClickListener{
            Validar()
        }
        binding.TxtTengoCuenta.setOnClickListener{
            startActivity(Intent(this, Login_Admin::class.java))
        }

    }

    var apellidos = ""
    var nombres = ""
    var correo = ""
    var contraseña = ""
    var r_contraseña = ""
    private fun Validar() {
        apellidos = binding.EtApellidosAdmin.text.toString().trim()
        nombres = binding.EtNombresAdmin.text.toString().trim()
        correo = binding.EtCorreoAdmin.text.toString().trim()
        contraseña = binding.EtContraAdmin.text.toString().trim()
        r_contraseña = binding.EtRContraAdmin.text.toString().trim()

        if(apellidos.isEmpty()){
            binding.EtApellidosAdmin.error = "¿Como te llamas?"
            binding.EtApellidosAdmin.requestFocus()
        }
        else if (nombres.isEmpty()){
            binding.EtNombresAdmin.error = "¿Como te llamas?"
            binding.EtNombresAdmin.requestFocus()
        }
        else if(correo.isEmpty()){
            binding.EtCorreoAdmin.error = "Usaras esta información cuando inicies sesión"
            binding.EtCorreoAdmin.requestFocus()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            binding.EtCorreoAdmin.error = "Este Correo no es valido"
            binding.EtCorreoAdmin.requestFocus()
        }
        else if(contraseña.isEmpty()){
            binding.EtContraAdmin.error = "Ingrese la Contraseña"
            binding.EtContraAdmin.requestFocus()
        }
        else if(contraseña.length<6){
            binding.EtContraAdmin.error = "Ingrese una convinación de almenos seis caracteres"
            binding.EtContraAdmin.requestFocus()
        }
        else if(r_contraseña.isEmpty()){
            binding.EtRContraAdmin.error = "Repita la Contraseña"
            binding.EtRContraAdmin.requestFocus()
        }else if(contraseña != r_contraseña){
            binding.EtRContraAdmin.error = "Las contraseñas no coinciden"
            binding.EtRContraAdmin.requestFocus()
        }
        else{
            CrearCuentaAdmin(correo,contraseña)
        }
        
    }

    private fun CrearCuentaAdmin(correo: String, contraseña: String) {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnSuccessListener {
                AgregarInfoBD()

            }
            .addOnFailureListener {e->
            progressDialog.dismiss()
            Toast.makeText(applicationContext, "Fallo la creación de cuenta por ${e.message}", Toast.LENGTH_SHORT).show()
    }

    }

    private fun AgregarInfoBD() {
        progressDialog.setMessage("Guardando Información")
        val tiempo = System.currentTimeMillis()
        val uid = firebaseAuth.uid

        val datos_admin : HashMap<String, Any?> = HashMap()
        datos_admin["uid"]=uid
        datos_admin["apellidos"]=apellidos
        datos_admin["nombres"]=nombres
        datos_admin["correo"]=correo
        datos_admin["rol"]="Administrador"
        datos_admin["tiempo_registro"]= tiempo
        datos_admin["imagen"]=""

        val reference = FirebaseDatabase.getInstance().getReference("Usuario")
        reference.child(uid!!)
            .setValue(datos_admin)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Cuenta creada", Toast.LENGTH_SHORT)
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    applicationContext,
                    "No se guardo la información debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }













    }
}