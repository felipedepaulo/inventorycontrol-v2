package com.anydigital.inventorycontrolv1

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.anydigital.inventorycontrolv1.databinding.ActivityRegistroDeEntradaBinding
import com.anydigital.inventorycontrolv1.model.Produto
import com.anydigital.inventorycontrolv1.model.ProdutoHistorico
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RegistroDeEntradaActivity : AppCompatActivity(), View.OnClickListener {

    var db: FirebaseFirestore? = null
    var reference: CollectionReference? = null
    var referenceDocumento: DocumentReference? = null
    private lateinit var auth: FirebaseAuth;
    private var menuItem: MenuItem? = null

    val colecao_samsung = "Samsung"
    val colecao_samsung_historico = "SamsungHistorico"

    val colecao_americanas = "Americanas"
    val colecao_americanas_historico = "AmericanasHistorico"

    private val binding: ActivityRegistroDeEntradaBinding by lazy {
        ActivityRegistroDeEntradaBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_de_entrada)
        setContentView(binding.root)

        var dados = intent.extras
        var codigoProduto = dados?.getString("codigoProduto").toString()

        binding.buttonRegistrarEntrada.setOnClickListener(this)
        binding.buttonCancelaEntrada.setOnClickListener(this)

        db = FirebaseFirestore.getInstance()

        var produto = Produto()



        reference = db?.collection(colecao_americanas)
        referenceDocumento = db!!.collection(colecao_americanas).document(colecao_americanas_historico)

        ouvinte(codigoProduto)

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.buttonRegistrarEntrada.id -> {
                buttonSalvarDados()
                Util.exibirToast(baseContext,"Registro de entrada realizado com sucesso")
                startActivity(Intent(this, MenuPrincipalActivity::class.java))
                finish()

            }
            binding.buttonCancelaEntrada.id -> {
                startActivity(Intent(this, MenuPrincipalActivity::class.java))
                finish()

            }
        }

    }

    private fun buttonSalvarDados() {

        var dados = intent.extras
        val data = Calendar.getInstance().time
        var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        val codigoBarras = binding.edCodigoBarrasEntrada.text.toString()
        val cor = binding.edCorEntrada.text.toString()
        val fabricante = binding.edFabricanteEntrada.text.toString()
        val modelo = binding.edModeloEntrada.text.toString()
        val operacao = getString(R.string.operacao_de_entrada)
        val box = binding.edBoxEntrada.text.toString()
        val unidade = binding.edUnidadeEntrada.text.toString()
        val ean = binding.edEanEntrada.text.toString()
        //val usuario = "felipedepaulo@gmail.com.br"
        var usuario = dados?.getString("usuario").toString()
        val dataHora = dateTimeFormat.format(data)


        if (!codigoBarras.trim().isEmpty() && !cor.trim().isEmpty() && !fabricante.trim()
                .isEmpty() && !modelo.trim()
                .isEmpty()
        ) {

            if (Util.statusInternet(baseContext)) {
                //val idade: Int = idadeString.toInt()

                val produtoHistorico = ProdutoHistorico(
                    box,
                    cor,
                    dataHora,
                    ean,
                    fabricante,
                    codigoBarras,
                    modelo,
                    operacao,
                    unidade,
                    usuario
                )


                registrar(produtoHistorico)

            } else {
                alerta("Sem conexão com a Internet")
            }
        } else {
            alerta("Insira todos os campos de forma correta")
        }
    }

    private fun registrar(produtoHistorico: ProdutoHistorico) {


        val reference: CollectionReference = db!!.collection(colecao_americanas_historico)

        val dialogProgess = DialogProgress()
        dialogProgess.show(supportFragmentManager, "0")

/*        var data = hashMapOf<String, Any>(
            "Nome" to nome,
            "Idade" to idade,
            "Fumante" to false
        )*/


        // Grava no banco enviado o nome do documento (idNomePasta)
        //reference.document("AmericanasHistorico").set(produtoHistorico).addOnSuccessListener {

        reference.document().set(produtoHistorico).addOnSuccessListener {
            dialogProgess.dismiss()
        }.addOnFailureListener { error ->
            dialogProgess.dismiss()
            alerta("Erro ao gravar dados: ${error.message.toString()}")
        }
    }

    private fun ouvinte(imei: String) {

        var edCodigoBarras = binding.edCodigoBarrasEntrada
        val edFabricante = binding.edFabricanteEntrada
        val edModelo = binding.edModeloEntrada
        val edCor = binding.edCorEntrada
        val edBox = binding.edBoxEntrada
        val edUnidade = binding.edUnidadeEntrada
        val edEan = binding.edEanEntrada

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "0")

        var listaProdutos: MutableList<Produto> = ArrayList<Produto>()

        val query = reference!!.whereEqualTo("imei", imei.trim())


        query!!.addSnapshotListener { documentos, error ->

            dialogProgress.dismiss()

            if (error != null) {
                alerta("Erro na comunicação com o servidor: ${error.message.toString()}")
            } else if (documentos != null && !documentos.isEmpty) {

                listaProdutos.clear()

                for (documento in documentos) {
                    val produto = documento.toObject(Produto::class.java)
                    listaProdutos.add(produto)

                    listaProdutos[0].IMEI.let { edCodigoBarras.setText(it) }
                    listaProdutos[0].Cor.let { edCor.setText(it) }
                    listaProdutos[0].Fabricante.let { edFabricante.setText(it) }
                    listaProdutos[0].Modelo.let { edModelo.setText(it) }
                    listaProdutos[0].Box.let { edBox.setText(it) }
                    listaProdutos[0].Unidade.let { edUnidade.setText(it) }
                    listaProdutos[0].Ean.let { edEan.setText(it) }

                }
            } else {
                alerta("Produto não cadastrado")
                binding.buttonRegistrarEntrada.isEnabled = false
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var dados = intent.extras
        var usuario = dados?.getString("usuario").toString()

        return when(item.itemId){
            R.id.menu_principal ->{
                var intent = Intent(this, MenuPrincipalActivity::class.java)

                if (usuario == null) {
                    val usuario = auth?.currentUser
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)
                return true
            }
            R.id.historico_menu ->{
                var intent = Intent(this, ResultadoPesquisaActivity::class.java)

                if (usuario == null) {
                    val usuario = auth?.currentUser
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)
                //item.isChecked = !item.isChecked
                return true
            }
            R.id.alterar_cadastro_submenu ->{
                var intent = Intent(this, CadastroDeProdutoActivity::class.java)

                if (usuario == null) {
                    val usuario = auth?.currentUser
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)
                return true
            }
            R.id.pesquisar_produto_submenu ->{
                var intent = Intent(this, ResultadoPesquisaProdutoActivity::class.java)

                if (usuario == null) {
                    val usuario = auth?.currentUser
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)

                return true
            }
            /*menuItem?.itemId ->{
                println("Item dinamico clicado!!")
                true
            }*/
            else->{
                return super.onOptionsItemSelected(item)
            }
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


