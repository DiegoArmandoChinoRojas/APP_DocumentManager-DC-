package com.proysistemas.documentmanager.Administrador

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.proysistemas.documentmanager.databinding.ItemCategoriaAdminBinding

class AdaptadorCategoria : RecyclerView.Adapter<AdaptadorCategoria.HolderCategoria>, Filterable {

    private lateinit var binding: ItemCategoriaAdminBinding

    private val mcontext : Context
    public var categoriaArrayList : ArrayList<ModeloCategoria>

    private var filtroList : ArrayList<ModeloCategoria>
    private var filtro : FiltroCategoria? = null

    constructor(mcontext: Context, categoriaArrayList: ArrayList<ModeloCategoria>) {
        this.mcontext = mcontext
        this.categoriaArrayList = categoriaArrayList
        this.filtroList = categoriaArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategoria {
        binding = ItemCategoriaAdminBinding.inflate(LayoutInflater.from(mcontext),parent, false)
        return HolderCategoria(binding.root)
    }

    // Obtener los item de lista
    override fun getItemCount(): Int {
        return categoriaArrayList.size
    }

    // Obtener datos
    override fun onBindViewHolder(holder: HolderCategoria, position: Int) {
        val modelo = categoriaArrayList[position]
        val id= modelo.id
        val descripcion = modelo.descripcion
        val tiempo = modelo.tiempo
        val uid = modelo. uid

        holder.categoriaTv.text = descripcion

        holder.eliminarCategoria.setOnClickListener{
            val builder = AlertDialog.Builder(mcontext)
            builder.setTitle("Eliminar categoria")
                .setMessage("¿Estas seguro de eliminar la categoria?")
                .setPositiveButton("Confirmar"){a, d->
                    Toast.makeText(mcontext, "Eliminando categoria", Toast.LENGTH_SHORT).show()
                    EliminarCategoria(modelo, holder)
                }
                .setNegativeButton("Cancelar"){a, d->
                    a.dismiss()
                }
            builder.show()
        }
    }

    // Obtener y buscar por ID-BD
    private fun EliminarCategoria(modelo: ModeloCategoria, holder: AdaptadorCategoria.HolderCategoria) {
        val id=modelo.id
        val ref = FirebaseDatabase.getInstance().getReference("Categoria")
        ref.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(mcontext, "Categoria eliminada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(mcontext, "No se pudo eliminar la categoria debido a${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    inner class HolderCategoria(itemView : View): RecyclerView.ViewHolder(itemView){
        var categoriaTv : TextView = binding.ItemNombreCategoriaA
        var eliminarCategoria : ImageButton = binding.EliminarCategoria
    }

    override fun getFilter(): Filter {
        if (filtro == null){
            filtro = FiltroCategoria(filtroList,this)
        }
        return filtro as FiltroCategoria
    }

}