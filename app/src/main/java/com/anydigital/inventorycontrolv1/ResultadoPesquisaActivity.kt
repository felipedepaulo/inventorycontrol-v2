package com.anydigital.inventorycontrolv1

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anydigital.inventorycontrolv1.databinding.ActivityResultadoPesquisaBinding
import com.anydigital.inventorycontrolv1.model.Usuario
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ResultadoPesquisaActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    SearchView.OnCloseListener, AdapterRecyclerViewUsuario.ClickUsuarios,
    AdapterRecyclerViewUsuario.UltimoItemExibidoRecyclerView {

    private lateinit var auth: FirebaseAuth;
    val colecao_samsung = "Samsung"
    val colecao_samsung_historico = "SamsungHistorico"

    val colecao_americanas = "Americanas"
    val colecao_americanas_historico = "AmericanasHistorico"


    private val binding: ActivityResultadoPesquisaBinding by lazy {
        ActivityResultadoPesquisaBinding.inflate(layoutInflater)
    }

    var searchView: SearchView? = null
    var adapterRecyclerViewUsuario: AdapterRecyclerViewUsuario? = null
    var usuario: ArrayList<Usuario> = ArrayList()
    var dataHora: ArrayList<Usuario> = ArrayList()
    var operacao: ArrayList<Usuario> = ArrayList()
    var imei: ArrayList<Usuario> = ArrayList()
    var box: ArrayList<Usuario> = ArrayList()
    var unidade: ArrayList<Usuario> = ArrayList()
    var ean: ArrayList<Usuario> = ArrayList()

    var db: FirebaseFirestore? = null
    var reference: CollectionReference? = null
    var proximoDocumento: Query? = null


    var isFiltrando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

/*
        binding.btExibirMais.setOnClickListener{
            exibirMaisItens()
        }
*/
        var dados = intent.extras
        var codigoUsuario = dados?.getString("usuario").toString()

        //binding.btExibirMais.visibility = View.GONE

        db = FirebaseFirestore.getInstance()
        reference = db?.collection(colecao_americanas_historico)
        iniciarRecycleView()
        exibirItens()

    }

    //---------------------------------- RECYCLEVIEW - LISTA-----------------------------
    private fun iniciarRecycleView() {

        adapterRecyclerViewUsuario =
            AdapterRecyclerViewUsuario(
                this,
                usuario,
                dataHora,
                operacao,
                imei,
                box,
                unidade,
                ean,
                this,
                this
            )
        binding.rvResultadoPesquisa.layoutManager = LinearLayoutManager(this)
        binding.rvResultadoPesquisa.adapter = adapterRecyclerViewUsuario
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search, menu)
        val search = menu!!.findItem(R.id.action_search)

        searchView = search.actionView as SearchView
        searchView?.queryHint = "Digite email do usuário"
        searchView?.setOnQueryTextListener(this)
        searchView?.setOnCloseListener(this)
        //searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES // Primeiras palavras com letra maiuscula somente para a primeira palavra
        //searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS // Primeiras palavras com letra maiuscula mesmo seprando por espaco
        return super.onCreateOptionsMenu(menu)
    }

    //---------------------------------- CLICK EM ITEM DA LISTA-----------------------------
    override fun clickUsuarios(usuario: Usuario) {
        //Util.exibirToast(this,usuario.usuario.toString())

        var intent = Intent(this, DetalhesDaOperacaoActivity::class.java)

        intent.putExtra("codigoUsuario", usuario.usuario)
        intent.putExtra("operacao", usuario.operacao)
        intent.putExtra("modelo", usuario.modelo)
        intent.putExtra("cor", usuario.cor)
        intent.putExtra("imei", usuario.imei)
        intent.putExtra("dataHora", usuario.dataHora)
        intent.putExtra("fabricante", usuario.fabricante)
        intent.putExtra("box", usuario.box)
        intent.putExtra("unidade", usuario.unidade)
        intent.putExtra("ean", usuario.ean)

        startActivity(intent)
        finish()

    }

    //ultimo item exibido
    override fun ultimoItemExibidoRecyclerView(isExibido: Boolean) {
        if (!isFiltrando) {
            exibirMaisItens()
        }

    }

    //---------------------------------- METODOS SEARCH PARA PESQUISA-----------------------------
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("yyyyui-onQueryTextSubmit", "onQueryTextSubmit")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("yyyyui-onQueryTextChange", newText.toString())
        isFiltrando = true
        pesquisar(newText.toString())
        return true
    }

    override fun onClose(): Boolean {
        searchView?.onActionViewCollapsed()
        usuario.clear()
        adapterRecyclerViewUsuario?.notifyDataSetChanged()
        exibirItens()
        return true
    }

    //---------------------------------- PESQUISA POR NOME (FILTRO) -----------------------------
    @SuppressLint("NotifyDataSetChanged")
    fun pesquisar(newText: String) {


        var query =
            db
                ?.collection(colecao_americanas_historico)
                ?.orderBy("dataHora", Query.Direction.DESCENDING)
                ?.whereEqualTo("usuario", newText.trim())
        //?.startAt(newText)
        //?.endAt(newText)

        //?.limit(10)

        query?.get()?.addOnSuccessListener { documentos ->

            usuario.clear()

            // colocar o if para validar se é null
            for (documento in documentos) {


                val usuarios = documento.toObject(Usuario::class.java)

                usuario.add(usuarios)
            }

            adapterRecyclerViewUsuario?.notifyDataSetChanged()

        }
    }

    //---------------------------------- LER DADOS FIREBASE/PAGINACAO-----------------------------
    fun exibirItens() {

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "1")


        var query =
            db?.collection(colecao_americanas_historico)
                ?.orderBy("dataHora", Query.Direction.DESCENDING)
                ?.limit(10)


        query?.get()?.addOnSuccessListener { documentos ->

            dialogProgress.dismiss()

            val ultimoDocumento = documentos.documents[documentos.size() - 1]
            proximoDocumento = db?.collection(colecao_americanas_historico)
                ?.orderBy("dataHora", Query.Direction.DESCENDING)
                ?.startAfter(ultimoDocumento)
                ?.limit(10)

            // colocar o if para validar se é null
            for (documento in documentos) {


                val usuarios = documento.toObject(Usuario::class.java)

                usuario.add(usuarios)
            }

            adapterRecyclerViewUsuario?.notifyDataSetChanged()

        }?.addOnFailureListener { error ->
            alerta("Error:${error.message}")
            dialogProgress.dismiss()
        }

    }

    fun exibirMaisItens() {

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "1")



        proximoDocumento?.get()?.addOnSuccessListener { documentos ->

            dialogProgress.dismiss()

            if (documentos.size() > 0) {
                val ultimoDocumento = documentos.documents[documentos.size() - 1]
                proximoDocumento = db?.collection(colecao_americanas_historico)
                    ?.orderBy("dataHora", Query.Direction.DESCENDING)
                    ?.startAfter(ultimoDocumento)
                    ?.limit(10)

                // colocar o if para validar se é null
                for (documento in documentos) {


                    val usuarios = documento.toObject(Usuario::class.java)

                    usuario.add(usuarios)
                }

                adapterRecyclerViewUsuario?.notifyDataSetChanged()

            } else {
                alerta("Sem mais informações a serem exibidas")
                //binding.btExibirMais.visibility = View.GONE
            }

        }?.addOnFailureListener { error ->
            Util.exibirToast(baseContext, "Error: ${error.message}")
            dialogProgress.dismiss()
        }
    }

    fun alerta(message: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        with(builder) {
            setTitle("Informaçao")
            setMessage(message)
            setPositiveButton("OK", null)
//            setNegativeButton("CANCEL", null)
//            setNeutralButton("NEUTRAL", null)
/*            setPositiveButtonIcon(resources.getDrawable(android.R.drawable.ic_dialog_alert, theme))
            setIcon(resources.getDrawable(android.R.drawable.ic_dialog_alert, theme))*/
        }
        val alertDialog = builder.create()
        alertDialog.show()

        val button = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
        with(button) {
            setBackgroundColor(Color.BLUE)
            setPadding(0, 0, 20, 0)
            setTextColor(Color.WHITE)
        }
    }
}




