package com.proysistemas.documentmanager.Administrador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.proysistemas.documentmanager.R

class ListaDocumentoAdmin : AppCompatActivity() {

    private var idCategoria= ""
    private var descripcionCate= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_documento_admin)

        val intent = intent
        idCategoria = intent.getStringExtra("idCategoria")!!
        descripcionCate = intent.getStringExtra("descripcionCategoria")!!

    }
}