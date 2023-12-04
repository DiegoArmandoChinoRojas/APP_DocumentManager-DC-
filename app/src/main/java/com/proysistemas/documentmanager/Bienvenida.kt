package com.proysistemas.documentmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.SupervisorJob

class Bienvenida : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
        firebaseAuth=FirebaseAuth.getInstance()
        verBienvenida()
    }
    fun verBienvenida(){
        object : CountDownTimer(4000,2000){
            override fun onTick(p0: Long) {
            }
            override fun onFinish() {
               VerificarSesion()
            }

        }.start()
    }

    fun VerificarSesion(){
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser==null){
            startActivity(Intent(this,Elegir_rol::class.java))
            finishAffinity()
        }else{
            val reference = FirebaseDatabase.getInstance().getReference("Usuario")
            reference.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val rol = snapshot.child("rol").value
                        if (rol == "Administrador") {
                            startActivity(Intent(this@Bienvenida, MainActivity::class.java))
                            finishAffinity()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}