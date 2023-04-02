package com.anydigital.inventorycontrolv1.model

class Usuario(
    var cor: String?,
    var dataHora: String?,
    var fabricante: String?,
    var imei: String?,
    var modelo: String?,
    var operacao: String?,
    var usuario: String?,
    var box: String?,
    var unidade: String?,
    var ean: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

}
