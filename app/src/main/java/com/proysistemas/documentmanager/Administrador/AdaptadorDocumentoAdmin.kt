package com.proysistemas.documentmanager.Administrador

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.proysistemas.documentmanager.databinding.ItemDocumentoAdminBinding

class AdaptadorDocumentoAdmin : RecyclerView.Adapter<AdaptadorDocumentoAdmin.HolderDocumentoAdmin>, Filterable{

    private lateinit var binding: ItemDocumentoAdminBinding
    private var m_context : Context
    public var docArrayList : ArrayList<ModeloDocumento>
    private var filtroDocumento : ArrayList<ModeloDocumento>
    private var filtro : FiltroDocumentoAdmin?=null

    constructor(m_context: Context, docArrayList: ArrayList<ModeloDocumento>) : super() {
        this.m_context = m_context
        this.docArrayList = docArrayList
        this.filtroDocumento = docArrayList
    }

    inner class HolderDocumentoAdmin (itemView: View) : RecyclerView.ViewHolder(itemView){
        val VisualizarDoc = binding.VisualizarDoc
        val progressBar = binding.progressBar
        val Txt_titulo_documento_admin= binding.TxtTituloDocumentoAdmin
        val Txt_descripcion_documento_admin= binding.TxtDescripcionDocumentoAdmin
        val Txt_categoria_documento_admin = binding.TxtCategoriaDocumentoAdmin
        val Txt_tamaño_documento_admin=binding.TxtTamaODocumentoAdmin
        val Txt_fecha_documento_admin = binding.TxtFechaDocumentoAdmin
        val Ib_mas_opciones = binding.IbMasOpciones
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderDocumentoAdmin {
        binding = ItemDocumentoAdminBinding.inflate(LayoutInflater.from(m_context), parent, false)
        return  HolderDocumentoAdmin(binding.root)
    }

    override fun getItemCount(): Int {
        return docArrayList.size
    }

    override fun onBindViewHolder(holder: HolderDocumentoAdmin, position: Int) {
        val modelo = docArrayList[position]
        val docId = modelo.id
        val categoriaId = modelo.categoria
        val titulo = modelo.titulo
        val descripcion = modelo.descripcion
        val docUrl = modelo.url
        val tiempo= modelo.tiempo

        val formatoTiempo =Funciones.formatotiempo(tiempo)

        holder.Txt_titulo_documento_admin.text= titulo
        holder.Txt_descripcion_documento_admin.text = descripcion
        holder.Txt_fecha_documento_admin.text = formatoTiempo

        Funciones.cargarCategoria(categoriaId , holder.Txt_categoria_documento_admin)
        Funciones.cargarDocumentoUrl(docUrl, titulo, holder.VisualizarDoc, holder.progressBar, null)
        Funciones.cargarTamañoDoc(docUrl,titulo, holder.Txt_tamaño_documento_admin)

        holder.Ib_mas_opciones.setOnClickListener{
            verOpciones(modelo, holder)
        }
    }

    private fun verOpciones(modelo: ModeloDocumento, holder: AdaptadorDocumentoAdmin.HolderDocumentoAdmin) {
        val idDocumento= modelo.id
        val urlDocumento = modelo.url
        var tituloDocumento = modelo.titulo

        val opciones = arrayOf("Actualizar", "Eliminar")

        //Alert Dialog
        val builder = AlertDialog.Builder(m_context)
        builder.setTitle("Seleccione una opción")
            .setItems(opciones){dialog,posicion->
                if(posicion ==0){
                    //Actualizar
                    val intent = Intent(m_context,ActualizarDocumento::class.java)
                    intent.putExtra("idDocumento", idDocumento)
                    m_context.startActivity(intent)

                }else if(posicion == 1){
                    //Eliminar
                    val opcionesEliminacion= arrayOf("Si","Cancelar")
                    val builder =AlertDialog.Builder(m_context)
                        builder.setTitle("¿Confirmas que quieres eliminar el documento ${tituloDocumento}?")
                        .setItems(opcionesEliminacion){dialog,posicion->
                            if(posicion ==0){
                                Funciones.eliminarDocumento(m_context,idDocumento,urlDocumento, tituloDocumento)
                            }else if(posicion ==1){
                                Toast.makeText(m_context, "Se a cancelado", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .show()
                }
            }
            .show()
    }

    override fun getFilter(): Filter {
        if(filtro == null){
            filtro = FiltroDocumentoAdmin(filtroDocumento, this)
        }
        return filtro as FiltroDocumentoAdmin
    }

}