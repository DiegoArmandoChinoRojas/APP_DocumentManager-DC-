package com.proysistemas.documentmanager.Administrador

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.proysistemas.documentmanager.databinding.ActivityAgregarDocumentoBinding

class Agregar_Documento : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarDocumentoBinding
    private lateinit var categoriaArrayList : ArrayList<ModeloCategoria>

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var docUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarDocumentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()

        CargarCategorias()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere porfavor")
        progressDialog.setCanceledOnTouchOutside(false)


        // regresar
        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        // Adjuntar documento
        binding.IbAdjuntarDoc.setOnClickListener{
            ElegirDocumento()
        }

        // Selecciona categoria
        binding.TvCategoriaDoc.setOnClickListener{
            SeleccionarCategoria()
        }

        // Subir documento
        binding.BtnSubirDoc.setOnClickListener{
            ValidarInformacion()
        }

    }

    private var titulo= ""
    private var descripcion = ""
    private var categoria = ""

    // Capturar los datos del documento
    private fun ValidarInformacion() {
        titulo= binding.EtTituloDocumento.text.toString().trim()
        descripcion = binding.EtDescripcionDoc.text.toString().trim()
        categoria = binding.TvCategoriaDoc.text.toString().trim()

        if(titulo.isEmpty()){
            Toast.makeText(this, "Ingrese el título", Toast.LENGTH_SHORT).show()
        }else if(descripcion.isEmpty()){
            Toast.makeText(this, "Ingrese la descripción", Toast.LENGTH_SHORT).show()
        }else if (categoria.isEmpty()){
            Toast.makeText(this, "Seleccione la categoria", Toast.LENGTH_SHORT).show()
        }else if (docUri == null){
            Toast.makeText(this, "Adjunte un documento", Toast.LENGTH_SHORT).show()
        }
        else{
            SubirDocumentoStore()
        }
    }

    // Implementación FireBaseStorage -- Archivos
    private fun SubirDocumentoStore() {
        progressDialog.setMessage("Subiendo documento")
        progressDialog.show()

        val tiempo = System.currentTimeMillis()
        val ruta_doc = "Documentos/$tiempo"
        val storageReference = FirebaseStorage.getInstance().getReference(ruta_doc)
        storageReference.putFile(docUri!!)
            .addOnSuccessListener {tarea->
                // Obtener la Uri del archivo subido al Storage
                val uriTask : Task<Uri> = tarea.storage.downloadUrl
                while(!uriTask.isSuccessful);
                val UrlDocSubido= "${uriTask.result}"
                SubirDocumentoBD(UrlDocSubido, tiempo)

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo la subida del documento debido a ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun SubirDocumentoBD(urlDocSubido: String, tiempo: Long) {
        progressDialog.setMessage("Subiendo documento a la BD")
        val uid= firebaseAuth.uid

        val hashMap : HashMap<String, Any> = HashMap()
        hashMap["uid"]= "$uid"
        hashMap["id"]= "$tiempo"
        hashMap["titulo"]= titulo
        hashMap["descripcion"]= descripcion
        hashMap["categoria"]= categoria
        hashMap["url"]= urlDocSubido
        hashMap["tiempo"]= tiempo
        hashMap["contadorDescargas"]= 0
        hashMap["contadorVistas"]= 0

        val ref = FirebaseDatabase.getInstance().getReference("Documentos")
        ref.child("$tiempo")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Documento subido con éxito!!", Toast.LENGTH_SHORT).show()
                binding.EtTituloDocumento.setText("")
                binding.EtDescripcionDoc.setText("")
                binding.TvCategoriaDoc.setText("")
                docUri = null

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this, "Fallo la subida del documento debido a ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    // Buscar y Capturar datos
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
    //Acceder memoria interna
    private fun ElegirDocumento(){
        val intent= Intent()
        intent.type = "application/documento"
        intent.action = Intent.ACTION_GET_CONTENT
        documentoActivityRl.launch(intent)
    }

    // Obtener Uri del documento Gestor Archivos
    val documentoActivityRl = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult>{resultado ->
            if(resultado.resultCode == RESULT_OK){
                docUri = resultado.data!!   .data
            }else{
                Toast.makeText(this,"Cancelado", Toast.LENGTH_SHORT).show()
            }
        }
    )





}