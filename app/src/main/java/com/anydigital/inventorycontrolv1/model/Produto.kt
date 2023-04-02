package com.anydigital.inventorycontrolv1.model

class Produto {
    var Cor: String? = null
    var Fabricante: String? = null
    var IMEI: String? = null
    var Modelo: String? = null
    var Box: String? = null
    var Unidade: String? = null
    var Ean:String? = null

    constructor()

    constructor(cor: String?, fabricante: String?, imei: String?, modelo: String?, box: String?,unidade:String?,ean: String?) {
        this.Cor = cor
        this.Fabricante = fabricante
        this.IMEI = imei
        this.Modelo = modelo
        this.Box = box
        this.Unidade = unidade
        this.Ean = ean
    }
}