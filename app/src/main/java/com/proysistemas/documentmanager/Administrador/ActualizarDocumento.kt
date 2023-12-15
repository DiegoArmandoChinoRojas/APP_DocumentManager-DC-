package com.proysistemas.documentmanager.Administrador

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.proysistemas.documentmanager.databinding.ActivityActualizarDocumentoBinding

class ActualizarDocumento : AppCompatActivity() {

    private lateinit var binding: ActivityActualizarDocumentoBinding
    private var idDocumento = ""
    private lateinit var progressDialog: ProgressDialog

    // Título Categoria
    private lateinit var catTituloArrayList: ArrayList<String>
    // Id
    private lateinit var catIdArrayList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idDocumento = intent.getStringExtra("idDocumento")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarCategorias()
        cargarInformacion()

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.TvCategoriaDoc.setOnClickListener{
            dialogCategoria()
        }
        binding.BtnActualizarDoc.setOnClickListener{
            validarInformacion()
        }

    }

    private fun cargarInformacion() {
        val ref= FirebaseDatabase.getInstance().getReference("Documentos")
        ref.child(idDocumento)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Captura de datos para actualizar
                    val titulo = snapshot.child("titulo").value.toString()
                    val descripcion = snapshot.child("descripcion").value.toString()
                    id_seleccionado = snapshot.child("categoria").value.toString()

                    // Seteo en diseño
                    binding.EtTituloDocumento.setText(titulo)
                    binding.EtDescripcionDoc.setText(descripcion)

                    val refCategoria = FirebaseDatabase.getInstance().getReference("Categoria")
                    refCategoria.child(id_seleccionado)
                        .addListenerForSingleValueEvent(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                //Obtener la categoria
                                val categoria = snapshot.child("descripcion").value
                                //Seteo en Tv
                                binding.TvCategoriaDoc.text = categoria.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }

                        })

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private var titulo= ""
    private var descripcion = ""

    private fun validarInformacion() {
        titulo= binding.EtTituloDocumento.text.toString().trim()
        descripcion = binding.EtDescripcionDoc.text.toString().trim()

        if(titulo.isEmpty()){
            Toast.makeText(this, "Ingrese el título", Toast.LENGTH_SHORT).show()
        }
        else if(descripcion.isEmpty()){
            Toast.makeText(this, "Ingrese la descripción", Toast.LENGTH_SHORT).show()
        }
        else if(id_seleccionado.isEmpty()){
            Toast.makeText(this, "Seleccione una categoria", Toast.LENGTH_SHORT).show()
        }
        else{
            actualizarInformacion()
        }

    }

    private fun actualizarInformacion() {
        progressDialog.setMessage("Actualizando la información")
        progressDialog.show()
        val hashMap = HashMap<String, Any>()
        hashMap["titulo"] = "$titulo"
        hashMap["descripcion"] = "$descripcion"
        hashMap["categoria"]= "$id_seleccionado"

        val ref= FirebaseDatabase.getInstance().getReference("Documentos")
        ref.child(idDocumento)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "La actualización fue exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "La actualización fallo debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private var id_seleccionado = ""
    private var titulo_seleccionado = ""
    private fun dialogCategoria() {
        val categoriaArray = arrayOfNulls<String>(catTituloArrayList.size)
        for(i in catTituloArrayList.indices){
            categoriaArray[i] = catTituloArrayList[i]
        }
        val buider = AlertDialog.Builder(this)
        buider.setTitle("Seleccione una categoria")
            .setItems(categoriaArray){dialog, posicion->
                id_seleccionado= catIdArrayList[posicion]
                titulo_seleccionado= catTituloArrayList[posicion]

                binding.TvCategoriaDoc.text = titulo_seleccionado
            }
            .show()


    }

    private fun cargarCategorias() {
        catTituloArrayList = ArrayList()
        catIdArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categoria")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                catTituloArrayList.clear()
                catIdArrayList.clear()
                for(ds in snapshot.children){
                    val id=  ""+ds.child("id").value
                    val categoria = ""+ds.child("descripcion").value

                    catTituloArrayList.add(categoria    )
                    catIdArrayList.add(id)
                }

            }

            override fun onCancelled(error: DatabaseError) {


            }

        })
    }
}