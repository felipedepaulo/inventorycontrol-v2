package com.anydigital.inventorycontrolv1

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.anydigital.inventorycontrolv1.model.Usuario

class AdapterRecyclerViewUsuario(
    val context: Context,
    var nome: ArrayList<Usuario>,
    var dataHora:ArrayList<Usuario>,
    var operacao:ArrayList<Usuario>,
    var imei:ArrayList<Usuario>,
    var box: ArrayList<Usuario>,
    var unidade: ArrayList<Usuario>,
    var ean: ArrayList<Usuario>,
    var clickUsuarios: ClickUsuarios,
    var ultimoItemExibidoRecyclerView: UltimoItemExibidoRecyclerView
    ):RecyclerView.Adapter<AdapterRecyclerViewUsuario.ViewHolder>() {


    //Diz qual layout é dos itens
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_lista_usuarios_recyclerview, parent, false)


        return ViewHolder(view)

    }

    //inserir informacoes no layout
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterRecyclerViewUsuario.ViewHolder, position: Int) {
        val usuarios: Usuario = nome[position]

        holder.nome.text = "eMail: ${usuarios.usuario}"
        holder.dataHora.text = "Data e Hora Operação: ${usuarios.dataHora}"
        holder.operacao.text = usuarios.operacao
        holder.imei.text = "IMEI: ${usuarios.imei}"

        holder.cardView.setOnClickListener {
            clickUsuarios.clickUsuarios(usuarios)
            //clickUsuarios.clickUsuarios(dataHora)
        }

        if (position == getItemCount()-1){
            ultimoItemExibidoRecyclerView.ultimoItemExibidoRecyclerView(true)
        }
    }

    //Qual o tamanho da lista
    override fun getItemCount(): Int {
        return nome.size
    }

    interface ClickUsuarios{
        fun clickUsuarios(usuario: Usuario)

    }

    interface UltimoItemExibidoRecyclerView{
        fun ultimoItemExibidoRecyclerView(isExibido: Boolean){

        }

    }
    //ligar variaveis com itens(elementos) do layout
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val nome = itemView.findViewById<TextView>(R.id.tv_ListaItem_Item_Usuario)
        val dataHora = itemView.findViewById<TextView>(R.id.tv_ListaItem_Item_dataHora)
        val operacao = itemView.findViewById<TextView>(R.id.tv_ListaItem_Item_operacao)
        val imei = itemView.findViewById<TextView>(R.id.tv_ListaItem_Item_imei)
        val cardView = itemView.findViewById<CardView>(R.id.cv_ListaitemUsuarios)

    }
}