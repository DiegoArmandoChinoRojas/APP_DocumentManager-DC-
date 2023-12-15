package com.proysistemas.documentmanager.Administrador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proysistemas.documentmanager.databinding.ItemDocumentoAdminBinding

class AdaptadorDocumentoAdmin : RecyclerView.Adapter<AdaptadorDocumentoAdmin.HolderDocumentoAdmin>{

    private lateinit var binding: ItemDocumentoAdminBinding
    private lateinit var m_context : Context
    private lateinit var docArrayList : ArrayList<ModeloDocumento>

    constructor(m_context: Context, docArrayList: ArrayList<ModeloDocumento>) : super() {
        this.m_context = m_context
        this.docArrayList = docArrayList
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

    }

}