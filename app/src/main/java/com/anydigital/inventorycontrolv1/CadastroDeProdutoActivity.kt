package com.anydigital.inventorycontrolv1

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.anydigital.inventorycontrolv1.databinding.ActivityCadastroDeProdutoBinding
import com.anydigital.inventorycontrolv1.model.Produto
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class CadastroDeProdutoActivity : AppCompatActivity(), View.OnClickListener {

    var db: FirebaseFirestore? = null
    private lateinit var auth: FirebaseAuth;
    var reference: CollectionReference? = null
    var referenceDocumento: DocumentReference? = null
    private var menuItem: MenuItem? = null

    val colecao_samsung = "Samsung"
    val colecao_samsung_historico = "SamsungHistorico"

    val colecao_americanas = "Americanas"
    val colecao_americanas_historico = "AmericanasHistorico"



    private val binding: ActivityCadastroDeProdutoBinding by lazy {
        ActivityCadastroDeProdutoBinding.inflate(layoutInflater)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        reference = db?.collection(colecao_americanas)
        //reference = db?.collection(colecao_samsung)

        binding.buttonCadastrarProduto.setOnClickListener(this)
        binding.buttonCancelaCadastro.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when (p0?.id) {
            binding.buttonCadastrarProduto.id -> {
                buttonSalvarDados()
            }
            binding.buttonCancelaCadastro.id -> {
                finish()
            }
        }
    }

    private fun buttonSalvarDados() {

        var dados = intent.extras
        val data = Calendar.getInstance().time
        var dateTimeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

        val imei = binding.edCodigoBarrasCadastro.text.toString()
        val cor = binding.edCorCadastro.text.toString()
        val fabricante = binding.edFabricanteCadastro.text.toString()
        val modelo = binding.edModeloCadastro.text.toString()
        var usuario = dados?.getString("usuario").toString()
        val dataHora = dateTimeFormat.format(data)
        val box = binding.edBox.text.toString()
        val unidade =  binding.edUnidade.text.toString()
        val ean = binding.edEan.text.toString()


        if (!imei.trim().isEmpty() && !cor.trim().isEmpty() && !fabricante.trim()
                .isEmpty() && !modelo.trim()
                .isEmpty()
        ) {

            if (Util.statusInternet(baseContext)) {
                //val idade: Int = idadeString.toInt()

                val produto = Produto(
                    cor,
                    fabricante,
                    imei,
                    modelo,
                    box,
                    unidade,
                    ean
                )

                registrar(produto)

            } else {
                Util.exibirToast(baseContext, "Sem conexão com Internet")
            }
        } else {
            Util.exibirToast(baseContext, "Insira todos os campos de forma correta")
        }
    }

    private fun registrar(produto: Produto) {


        val reference: CollectionReference = db!!.collection(colecao_americanas)

        val dialogProgess = DialogProgress()
        dialogProgess.show(supportFragmentManager, "0")

/*        var data = hashMapOf<String, Any>(
            "Nome" to nome,
            "Idade" to idade,
            "Fumante" to false
        )*/


        // Grava no banco enviado o nome do documento (idNomePasta)
        //reference.document("AmericanasHistorico").set(produtoHistorico).addOnSuccessListener {

        reference.document().set(produto).addOnSuccessListener {
            dialogProgess.dismiss()
//            Util.exibirToast(baseContext, "Registro de Entrada Realizado com Sucesso")
            alerta("Produto Cadastrado/Atualizado com Sucesso")
            binding.edFabricanteCadastro.text = null
            binding.edModeloCadastro.text = null
            binding.edCorCadastro.text = null
            binding.edCodigoBarrasCadastro.text = null
            binding.edBox.text = null
            binding.edEan.text = null
            binding.edUnidade.text = null
            binding.edFabricanteCadastro.isFocusable = true

        }.addOnFailureListener { error ->
            dialogProgess.dismiss()
            //Util.exibirToast(baseContext, "Erro ao gravar dados: ${error.message.toString()}")
            alerta("Erro ao gravar dados")
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