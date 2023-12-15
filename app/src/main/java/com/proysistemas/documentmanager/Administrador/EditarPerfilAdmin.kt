package com.proysistemas.documentmanager.Administrador

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.proysistemas.documentmanager.R
import com.proysistemas.documentmanager.databinding.ActivityEditarPerfilAdminBinding

class EditarPerfilAdmin : AppCompatActivity() {

    private lateinit var binding : ActivityEditarPerfilAdminBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    private var imagenUri : Uri?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        cargarInformacion()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.FbCambiarImg.setOnClickListener{
            mostrarOpciones()
        }

        // Actualizar
        binding.BtnActualizarInfo.setOnClickListener{
            validarInformacion()
        }


    }

    private fun mostrarOpciones() {
        val popupMenu = PopupMenu(this,binding.imgPerfilAdmin)

        popupMenu.menu.add(Menu.NONE,0,0, "Galería")
        popupMenu.menu.add(Menu.NONE,0,0, "Cámara")
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener { item->
            val id = item.itemId
            // Elegir una imagen de galeria
            if(id ==0){
                seleccionarImgGaleria()

            // Tomar fotografia
            }else if(id ==1){

            }
            true
        }
    }

    private fun seleccionarImgGaleria() {
        // Visualizar por tipo de archivo G
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "imagen/*"
        ARLGaleria.launch(intent)

    }
    // Obtener la img de galería
    private val ARLGaleria = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback <ActivityResult>{resultado->
            if(resultado.resultCode == Activity.RESULT_OK){
                val data = resultado.data
                imagenUri = data!!.data

                binding.imgPerfilAdmin.setImageURI(imagenUri)
            }else{
                Toast.makeText(applicationContext, "Cancelado", Toast.LENGTH_SHORT).show()

            }
        }
    )

    private var apellidos = ""
    private fun validarInformacion() {
        apellidos = binding.EtAApellidos.text.toString().trim()
        if(apellidos.isEmpty()){
            Toast.makeText(applicationContext, "Ingrese un nuevo apellido", Toast.LENGTH_SHORT).show()
        }else{
            if(imagenUri ==null){
                ActualizarInfo()
            }else{
                subirImagenStorage()
            }

        }

    }

    private fun subirImagenStorage() {
        progressDialog.setMessage("Subiendo imagen a Storage")
        progressDialog.show()

        val rutaImagen = "ImagenesPerfilAdministrador/"+firebaseAuth.uid

        val ref = FirebaseStorage.getInstance().getReference(rutaImagen)
        ref.putFile(imagenUri!!)
            .addOnSuccessListener {taskSnapshot->
                val uriTask : Task<Uri> = taskSnapshot.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val urlImagen = "${uriTask.result}"
                subirImagenDataBase(urlImagen)

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subirImagenDataBase(urlImagen :String) {
        progressDialog.setMessage("Actualizando imagen")

        val hashMap: HashMap<String, Any> = HashMap()
        if(imagenUri != null){
            hashMap["imagen"]= urlImagen
        }

        val ref = FirebaseDatabase.getInstance().getReference("Usuario")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Su imagen se a actualizado", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun ActualizarInfo() {
        progressDialog.setMessage("Actualizando Información")
        val hashMap : HashMap<String,Any> = HashMap()
        hashMap["apellidos"]= "${apellidos}"
        val ref = FirebaseDatabase.getInstance().getReference("Usuario")
        ref.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Se actualizo su información", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "No se realizo la actualización debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuario")
        ref.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Obtener la información
                    val apellidos = "${snapshot.child("apellidos").value}"
                    val imagen ="${snapshot.child("imagen").value}"

                    // Seteo
                    binding.EtAApellidos.setText(apellidos)

                    try{
                        // Libreria GLIDE
                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder(R.drawable.ic_perfil_cuenta)
                            .into(binding.imgPerfilAdmin)

                    }catch (e: Exception){
                        Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }
}