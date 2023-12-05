package com.proysistemas.documentmanager.Administrador

class ModeloCategoria {

    var id : String= ""
    var descripcion :String= ""
    var tiempo : Long= 0
    var uid : String= ""

    constructor()

    constructor(id: String, descripcion: String, tiempo: Long, uid: String) {
        this.id = id
        this.descripcion = descripcion
        this.tiempo = tiempo
        this.uid = uid
    }

}