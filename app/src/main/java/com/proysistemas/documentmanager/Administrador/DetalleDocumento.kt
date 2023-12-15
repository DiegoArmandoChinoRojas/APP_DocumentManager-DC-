package com.proysistemas.documentmanager.Administrador

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proysistemas.documentmanager.LeerDocumento
import com.proysistemas.documentmanager.R
import com.proysistemas.documentmanager.databinding.ActivityDetalleDocumentoBinding

class DetalleDocumento : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleDocumentoBinding
    private var idDocumento = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idDocumento = intent.getStringExtra("idDocumento")!!

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.BtnLeerDoc.setOnClickListener{
            val intent = Intent(this@DetalleDocumento, LeerDocumento::class.java)
            intent.putExtra("idDocumento", idDocumento)
            startActivity(intent)
        }

        cargarDetalleDoc()

    }

    private fun cargarDetalleDoc() {
        val ref = FirebaseDatabase.getInstance().getReference("Documentos")
        ref.child(idDocumento)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    // Obtener la informacion del libro a traves de id

                    val categoria = "${snapshot.child("categoria").value}"
                    val contadorDescargas = "${snapshot.child("contadorDescargas").value}"
                    val contadorVistas = "${snapshot.child("contadorVistas").value}"
                    val descripcion = "${snapshot.child("descripcion").value}"
                    val tiempo = "${snapshot.child("tiempo").value}"
                    val titulo = "${snapshot.child("titulo").value}"
                    val url = "${snapshot.child("url").value}"

                    // Formato tiempo
                    val fecha= Funciones.formatotiempo(tiempo.toLong())

                    // Cargar categoria
                    Funciones.cargarCategoria(categoria,binding.categoriaD)

                    // Cargar Imagen
                    Funciones.cargarDocumentoUrl("$url", "$titulo", binding.VisualizadorDoc, binding.progressBar,binding.paginasD)

                    // Cargar tamaño
                    Funciones.cargarTamañoDoc("$url", "$titulo", binding.tamaOD)

                    // Seteo con la Información
                    binding.tituloDocD.text = titulo
                    binding.descripcionD.text= descripcion
                    binding.descargasD.text= contadorDescargas
                    binding.vistasD.text=contadorVistas
                    binding.fechaD.text = fecha

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}