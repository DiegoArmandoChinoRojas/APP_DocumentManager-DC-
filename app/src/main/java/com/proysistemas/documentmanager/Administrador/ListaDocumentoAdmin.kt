package com.proysistemas.documentmanager.Administrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proysistemas.documentmanager.R
import com.proysistemas.documentmanager.databinding.ActivityListaDocumentoAdminBinding

class ListaDocumentoAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityListaDocumentoAdminBinding
    private var idCategoria= ""
    private var descripcionCate= ""

    private lateinit var docArrayList: ArrayList<ModeloDocumento>
    private lateinit var adaptadorDocumentoAdmin: AdaptadorDocumentoAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaDocumentoAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        idCategoria = intent.getStringExtra("idCategoria")!!
        descripcionCate = intent.getStringExtra("descripcionCategoria")!!

        binding.TxtTituloCategoria.text = descripcionCate

        ListarDocumentos()
    }

    private fun ListarDocumentos() {
        docArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Documentos")
        ref.orderByChild("categoria").equalTo(idCategoria)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    docArrayList.clear()
                    for(ds in snapshot.children){
                        val modelo = ds.getValue(ModeloDocumento::class.java)
                        if(modelo !=null){
                            docArrayList.add(modelo)
                        }
                    }
                    adaptadorDocumentoAdmin = AdaptadorDocumentoAdmin(this@ListaDocumentoAdmin, docArrayList)
                    binding.RVDocumentosAdmin.adapter = adaptadorDocumentoAdmin
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }
}