package com.anydigital.inventorycontrolv1.model

import java.util.Date

class ProdutoHistorico {
    var Box: String? =null
    var Cor: String? = null
    var DataHora: String? = null
    var Ean: String? = null
    var Fabricante: String? = null
    var IMEI: String? = null
    var Modelo: String? = null
    var Operacao: String? = null
    var Unidade: String? = null
    var Usuario: String? = null

    constructor()

    constructor(
        box: String?,
        cor: String?,
        dataHora: String?,
        ean: String?,
        fabricante: String?,
        imei: String?,
        modelo: String?,
        operacao:String?,
        unidade: String?,
        usuario: String?
    )
    {
        this.Box = box
        this.Cor = cor
        this.DataHora = dataHora
        this.Ean = ean
        this.Fabricante = fabricante
        this.IMEI = imei
        this.Modelo = modelo
        this.Operacao = operacao
        this.Unidade = unidade
        this.Usuario = usuario
    }
}