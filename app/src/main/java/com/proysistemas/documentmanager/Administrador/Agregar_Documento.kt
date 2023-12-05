package com.proysistemas.documentmanager.Administrador

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proysistemas.documentmanager.R
import com.proysistemas.documentmanager.databinding.ActivityAgregarDocumentoBinding

class Agregar_Documento : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarDocumentoBinding
    private lateinit var categoriaArrayList : ArrayList<ModeloCategoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CargarCategorias()

        // Accion regresar
        binding.IbRegresar.setOnClickListener(){
            onBackPressedDispatcher.onBackPressed()
        }

        // Selecciona en TextView
        binding.TvCategoriaDoc.setOnClickListener{
            SeleccionarCategoria()
        }
    }

    // Buscar y capturar datos, ordenar descripcion
    private fun CargarCategorias() {
        categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categoria").orderByChild("descripcion")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Recorrer BD
                for (ds in snapshot.children){
                    val modelo= ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modelo!!)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private var id_categoria = ""
    private var titulo_categoria=""
    private fun SeleccionarCategoria(){
        val categoriasArray = arrayOfNulls<String>(categoriaArrayList.size)
        for(i in categoriasArray.indices){
            categoriasArray[i]= categoriaArrayList[i].descripcion
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecionar categoria")
            .setItems(categoriasArray){ dialog, which->
                id_categoria=categoriaArrayList[which].id
                titulo_categoria= categoriaArrayList[which].descripcion
                // Agregar en Et - Categoria seleccionada
                binding.TvCategoriaDoc.text= titulo_categoria
            }
            .show()
    }
}