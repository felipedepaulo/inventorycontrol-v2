package com.anydigital.inventorycontrolv1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.anydigital.inventorycontrolv1.model.Produto
import com.anydigital.inventorycontrolv1.model.Usuario


class AdapterRecyclerViewProduto(
    val context: Context,
    var IMEI: ArrayList<Produto>,
    var fabricante: ArrayList<Produto>,
    var modelo: ArrayList<Produto>,
    var cor: ArrayList<Produto>,
    var ean: ArrayList<Produto>,
    var box: ArrayList<Produto>,
    var unidade: ArrayList<Produto>,

    var clickProdutos: ClickProdutos,
    var ultimoItemExibidoRecyclerView: UltimoItemExibidoRecyclerView
) : RecyclerView.Adapter<AdapterRecyclerViewProduto.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_lista_produtos_recyclerview, parent, false)

        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterRecyclerViewProduto.ViewHolder, position: Int) {
        val produtos: Produto = IMEI[position]

        holder.IMEI.text = "IMEI: ${produtos.IMEI}"
        holder.Fabricante.text = "Fabricante: ${produtos.Fabricante}"
        holder.Modelo.text = "Modelo: ${produtos.Modelo}"
        holder.Ean.text = "Ean: ${produtos.Ean}"

        holder.cardView.setOnClickListener {
            clickProdutos.clickProdutos(produtos)
        }

        if (position == getItemCount() - 1) {
            ultimoItemExibidoRecyclerView.ultimoItemExibidoRecyclerView(true)
        }
    }

    override fun getItemCount(): Int {
        return IMEI.size
    }

    interface ClickProdutos {
        fun clickProdutos(produto: Produto)

    }

    interface UltimoItemExibidoRecyclerView {
        fun ultimoItemExibidoRecyclerView(isExibido: Boolean) {

        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val IMEI = itemView.findViewById<TextView>(R.id.tv_listaitem_imei_produto)
        val Fabricante = itemView.findViewById<TextView>(R.id.tv_listaitem_fabricante_produto)
        val Modelo = itemView.findViewById<TextView>(R.id.tv_listaitem_modelo_produto)
        val Ean = itemView.findViewById<TextView>(R.id.tv_listaitem_ean_produto)
        val cardView = itemView.findViewById<CardView>(R.id.cv_ListaitemProdutos)

    }
}