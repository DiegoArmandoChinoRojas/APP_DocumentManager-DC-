package com.proysistemas.documentmanager.fragment_Admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proysistemas.documentmanager.Administrador.AdaptadorCategoria
import com.proysistemas.documentmanager.Administrador.Agregar_Categoria
import com.proysistemas.documentmanager.Administrador.Agregar_Documento
import com.proysistemas.documentmanager.Administrador.ModeloCategoria
import com.proysistemas.documentmanager.databinding.FragmentAdminDashboardBinding

class Fragment_admin_dashboard : Fragment() {

    private lateinit var binding: FragmentAdminDashboardBinding
    private lateinit var mcontext : Context
    private lateinit var categoriaArrayList : ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoria: AdaptadorCategoria

    override fun onAttach(context: Context) {
        mcontext= context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= FragmentAdminDashboardBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ListarCategorias()

        binding.BuscarCategoria.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int ,p2:Int,p3: Int) {

            }

            override fun onTextChanged(categoria: CharSequence?, p1: Int ,p2:Int,p3: Int) {
                try{
                    adaptadorCategoria.filter.filter(categoria)
                }catch (e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.BtnAgregarCategoria.setOnClickListener{
            startActivity(Intent(mcontext,Agregar_Categoria::class.java))
        }
        binding.AgregarPdf.setOnClickListener(){
            startActivity(Intent(mcontext,Agregar_Documento::class.java))

        }
    }
    private fun ListarCategorias(){
        categoriaArrayList = ArrayList()
        val ref = FirebaseDatabase.getInstance().getReference("Categoria").orderByChild("descripcion")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for(ds in snapshot.children){
                    val modelo = ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modelo!!)
                }
                adaptadorCategoria = AdaptadorCategoria(mcontext, categoriaArrayList)
                binding.RvCategorias.adapter= adaptadorCategoria
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

}