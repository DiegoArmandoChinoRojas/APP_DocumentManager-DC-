package com.proysistemas.documentmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.proysistemas.documentmanager.Administrador.Registrar_Admin
import com.proysistemas.documentmanager.databinding.ActivityElegirRolBinding

class Elegir_rol : AppCompatActivity() {

    private lateinit var binding: ActivityElegirRolBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElegirRolBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnRolAdministrador.setOnClickListener {
            //Toast.makeText(applicationContext, "Rol Administrador", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@Elegir_rol, Registrar_Admin::class.java))
        }

        binding.BtnRolEmpleado.setOnClickListener {
            //Toast.makeText(applicationContext, "Rol Empleado", Toast.LENGTH_SHORT).show()
        }
    }
}