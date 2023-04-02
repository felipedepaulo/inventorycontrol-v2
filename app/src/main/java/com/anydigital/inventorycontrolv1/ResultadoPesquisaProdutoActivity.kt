package com.anydigital.inventorycontrolv1

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anydigital.inventorycontrolv1.AdapterRecyclerViewProduto.UltimoItemExibidoRecyclerView
import com.anydigital.inventorycontrolv1.databinding.ActivityResultadoPesquisaProdutoBinding
import com.anydigital.inventorycontrolv1.model.Produto
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class ResultadoPesquisaProdutoActivity : AppCompatActivity(), SearchView.OnQueryTextListener,
    SearchView.OnCloseListener, AdapterRecyclerViewProduto.ClickProdutos,
    UltimoItemExibidoRecyclerView {

    private val binding: ActivityResultadoPesquisaProdutoBinding by lazy {
        ActivityResultadoPesquisaProdutoBinding.inflate(layoutInflater)
    }

    private var searchView: SearchView? = null
    private var adapterRecyclerViewProduto: AdapterRecyclerViewProduto? = null
    private var IMEI: ArrayList<Produto> = ArrayList()
    private var Fabricante:ArrayList<Produto> = ArrayList()
    private var Modelo:ArrayList<Produto> = ArrayList()
    private var Cor:ArrayList<Produto> = ArrayList()
    private var Ean:ArrayList<Produto> = ArrayList()
    private var Box:ArrayList<Produto> = ArrayList()
    private var Unidade:ArrayList<Produto> = ArrayList()
    private var db: FirebaseFirestore? = null
    private var reference: CollectionReference?= null
    private var proximoDocumento: Query? = null

    private var isFiltrando = false

    val colecao_samsung = "Samsung"
    val colecao_samsung_historico = "SamsungHistorico"

    val colecao_americanas = "Americanas"
    val colecao_americanas_historico = "AmericanasHistorico"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()
        reference = db?.collection(colecao_americanas)
        iniciarRecycleView()

        exibirItens()
    }

    //---------------------------------- RECYCLEVIEW - LISTA-----------------------------
    private fun iniciarRecycleView() {

        adapterRecyclerViewProduto = AdapterRecyclerViewProduto(this,IMEI,Fabricante,Modelo,Cor,Ean,Box,Unidade,this,this)
        binding.rvResultadoPesquisaProduto.layoutManager = LinearLayoutManager(this)
        binding.rvResultadoPesquisaProduto.adapter = adapterRecyclerViewProduto

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search,menu)
        val search = menu!!.findItem(R.id.action_search)

        searchView = search.actionView as SearchView
        searchView?.queryHint = "Digite IMEI do produto"
        searchView?.setOnQueryTextListener(this)
        searchView?.setOnCloseListener(this)
        //searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES // Primeiras palavras com letra maiuscula somente para a primeira palavra
        //searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS // Primeiras palavras com letra maiuscula mesmo seprando por espaco
        return super.onCreateOptionsMenu(menu)
    }

    //---------------------------------- CLICK EM ITEM DA LISTA-----------------------------
    override fun clickProdutos(produto: Produto) {

        var intent = Intent(this,DetalhesDoProdutoActivity::class.java)

        intent.putExtra("fabricante", produto.Fabricante)
        intent.putExtra("modelo", produto.Modelo)
        intent.putExtra("cor", produto.Cor)
        intent.putExtra("imei", produto.IMEI)
        intent.putExtra("ean", produto.Ean)
        intent.putExtra("box", produto.Box)
        intent.putExtra("unidade", produto.Unidade)

        startActivity(intent)
        finish()

    }

    //ultimo item exibido
    override fun ultimoItemExibidoRecyclerView(isExibido: Boolean) {
        if (!isFiltrando){
            exibirMaisItens()
        }

    }

    //---------------------------------- METODOS SEARCH PARA PESQUISA-----------------------------
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("yyyyui-onQueryTextSubmit","onQueryTextSubmit")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("yyyyui-onQueryTextChange",newText.toString())
        isFiltrando = true
        pesquisar(newText.toString())
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClose(): Boolean {
        searchView?.onActionViewCollapsed()
        IMEI.clear()
        adapterRecyclerViewProduto?.notifyDataSetChanged()
        exibirItens()
        return true
    }

    //---------------------------------- PESQUISA POR NOME (FILTRO) -----------------------------
    @SuppressLint("NotifyDataSetChanged")
    fun pesquisar(newText: String){


        var  query =
            db
                ?.collection(colecao_americanas)
                //?.startAt(newText)
                //?.endAt(newText)
                ?.orderBy("fabricante", Query.Direction.DESCENDING)
                ?.whereEqualTo("imei",newText.trim())
                //?.whereEqualTo("imei","1111")
                ?.limit(10)

        query?.get()?.addOnSuccessListener { documentos ->

            IMEI.clear()

            // colocar o if para validar se é null
            for (documento in documentos ){


                val produtos = documento.toObject(Produto::class.java)

                IMEI.add(produtos)
            }

            adapterRecyclerViewProduto?.notifyDataSetChanged()

        }
    }

    //---------------------------------- LER DADOS FIREBASE/PAGINACAO-----------------------------
    fun exibirItens(){

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager,"1")


        var  query =
            db?.collection(colecao_americanas)?.orderBy("imei", Query.Direction.DESCENDING)
                ?.limit(10)


        query?.get()?.addOnSuccessListener { documentos ->

            dialogProgress.dismiss()

            val ultimoDocumento = documentos.documents[documentos.size()-1]
            proximoDocumento =  db?.collection(colecao_americanas)
                ?.orderBy("imei", Query.Direction.DESCENDING)
                ?.startAfter(ultimoDocumento)
                ?.limit(10)

            // colocar o if para validar se é null
            for (documento in documentos ){


                val produtos = documento.toObject(Produto::class.java)

                IMEI.add(produtos)
            }

            adapterRecyclerViewProduto?.notifyDataSetChanged()

        }?.addOnFailureListener { error ->
            alerta("Error: ${error.message}")
            dialogProgress.dismiss()
        }

    }
    fun exibirMaisItens(){

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager,"1")

        proximoDocumento?.get()?.addOnSuccessListener { documentos ->

            dialogProgress.dismiss()

            if(documentos.size()>0){
                val ultimoDocumento = documentos.documents[documentos.size()-1]
                proximoDocumento =  db?.collection(colecao_americanas)
                    ?.orderBy("imei", Query.Direction.DESCENDING)
                    ?.startAfter(ultimoDocumento)
                    ?.limit(10)

                // colocar o if para validar se é null
                for (documento in documentos ){


                    val produtos = documento.toObject(Produto::class.java)

                    IMEI.add(produtos)
                }

                adapterRecyclerViewProduto?.notifyDataSetChanged()

            }else{
                //binding.btExibirMais.visibility = View.GONE
                alerta("Sem mais informações a serem exibidas")
            }

        }?.addOnFailureListener {error ->
            Util.exibirToast(baseContext,"Error: ${error.message}")
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




