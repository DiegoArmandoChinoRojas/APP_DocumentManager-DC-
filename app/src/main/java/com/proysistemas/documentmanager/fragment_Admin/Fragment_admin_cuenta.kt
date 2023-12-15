package com.proysistemas.documentmanager.fragment_Admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proysistemas.documentmanager.Administrador.EditarPerfilAdmin
import com.proysistemas.documentmanager.Administrador.Funciones

import com.proysistemas.documentmanager.Elegir_rol
import com.proysistemas.documentmanager.R
import com.proysistemas.documentmanager.databinding.FragmentAdminCuentaBinding

class Fragment_admin_cuenta : Fragment() {

    private lateinit var binding :FragmentAdminCuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mcontext : Context

    override fun onAttach(context: Context) {
        mcontext = context
        super.onAttach(context)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAdminCuentaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        // Obtener informacion de cuenta Administrador
        cargarInformacion()

        //
        binding.EditarPerfilAdmin.setOnClickListener{
            startActivity(Intent(mcontext, EditarPerfilAdmin::class.java))
        }

        binding.CerrarSesionAdmin.setOnClickListener {

            firebaseAuth.signOut()
            startActivity(Intent(context, Elegir_rol::class.java))
            activity?.finishAffinity()
        }
    }

    private fun cargarInformacion() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuario")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    //Obtener la informacion del Administrador
                    val apellidos = "${snapshot.child("apellidos").value}"
                    val nombres = "${snapshot.child("nombres").value}"
                    val correo = "${snapshot.child("correo").value}"
                    val imagen = "${snapshot.child("imagen").value}"
                    var t_registro = "${snapshot.child("tiempo_registro").value}"
                    val rol = "${snapshot.child("rol").value}"

                    if(t_registro == null){
                        t_registro = "0"
                    }

                    // Convertir tiempo
                    val formato_fecha = Funciones.formatotiempo(t_registro.toLong())

                    // Seteo de información
                    binding.TxtApellidoAdmin.text = apellidos
                    binding.TxtNombreAdmin.text = nombres
                    binding.TxtCorreoAdmin.text = correo
                    binding.TxtTRegistroAdmin.text = formato_fecha
                    binding.TxtRolAdmin.text = rol

                    // Seteo de imagen
                    try{
                        Glide.with(mcontext)
                            .load(imagen)
                            .placeholder(R.drawable.ic_perfil_cuenta)
                            .into(binding.imgPerfilAdmin)

                    }catch (e: Exception){
                        Toast.makeText(mcontext, "${e.message}", Toast.LENGTH_SHORT).show()

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}