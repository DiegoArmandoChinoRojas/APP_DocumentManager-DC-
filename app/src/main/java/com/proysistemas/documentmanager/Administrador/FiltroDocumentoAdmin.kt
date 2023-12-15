package com.proysistemas.documentmanager.Administrador

import android.widget.Filter

class FiltroDocumentoAdmin : Filter{

    var filtroList : ArrayList<ModeloDocumento>
    var adaptadorDocumentoAdmin: AdaptadorDocumentoAdmin

    constructor(filtroList: ArrayList<ModeloDocumento>, adaptadorDocumentoAdmin: AdaptadorDocumentoAdmin) {
        this.filtroList = filtroList
        this.adaptadorDocumentoAdmin = adaptadorDocumentoAdmin
    }

    override fun performFiltering(documento: CharSequence?): FilterResults {
        var documento : CharSequence?= documento
        val resultado = FilterResults()
        if(documento!=null && documento.isNotEmpty()){
            documento = documento.toString().lowercase()
            val modeloFilrado: ArrayList<ModeloDocumento> = ArrayList()
            for (i in filtroList.indices){
                if(filtroList[i].titulo.lowercase().contains(documento)){
                    modeloFilrado.add(filtroList[i])
                }
            }
            resultado.count = modeloFilrado.size
            resultado.values = modeloFilrado
        }
        else{
            resultado.count = filtroList.size
            resultado.values = filtroList
        }
        return resultado
    }

    override fun publishResults(constraint: CharSequence?, resultado: FilterResults) {
        adaptadorDocumentoAdmin.docArrayList = resultado.values as ArrayList<ModeloDocumento>
        adaptadorDocumentoAdmin.notifyDataSetChanged()
    }
}