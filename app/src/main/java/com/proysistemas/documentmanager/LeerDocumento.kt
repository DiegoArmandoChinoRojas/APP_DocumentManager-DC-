package com.proysistemas.documentmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.proysistemas.documentmanager.Administrador.Constantes
import com.proysistemas.documentmanager.databinding.ActivityLeerDocumentoBinding

class LeerDocumento : AppCompatActivity() {

    private lateinit var binding : ActivityLeerDocumentoBinding
    var idDocumento = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeerDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idDocumento= intent.getStringExtra("idDocumento")!!

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        cargarInformacionDoc()
    }

    private fun cargarInformacionDoc() {
        val ref = FirebaseDatabase.getInstance().getReference("Documentos")
        ref.child(idDocumento)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val docUrl = snapshot.child("url").value
                    cargarDocumentosStorage("$docUrl")
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    // FirebaseStorage
    private fun cargarDocumentosStorage(docUrl: String) {
        val reference = FirebaseStorage.getInstance().getReferenceFromUrl(docUrl)
        reference.getBytes(Constantes.Maximo_bytes)
            .addOnSuccessListener {bytes ->
                // Cargar Documento
                binding.VisualizadorDoc.fromBytes(bytes)
                    .swipeHorizontal(false)
                    .onPageChange { pagina, contadorPaginas ->
                        val paginaActual = pagina+1
                        binding.TxtLeerDoc.text = "$paginaActual/$contadorPaginas"
                    }
                    .load()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener{e->
                binding.progressBar.visibility = View.GONE
            }

    }
}