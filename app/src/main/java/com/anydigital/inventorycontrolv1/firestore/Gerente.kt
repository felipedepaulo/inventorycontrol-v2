package com.anydigital.inventorycontrolv1.firestore

class Gerente {
    var Nome: String? = null
    var Idade: Int? = null
    var Fumante: Boolean? = null

    constructor()

    constructor(nome: String?, idade: Int?, fumante: Boolean?) {
        this.Nome = nome
        this.Idade = idade
        this.Fumante = fumante
    }
}