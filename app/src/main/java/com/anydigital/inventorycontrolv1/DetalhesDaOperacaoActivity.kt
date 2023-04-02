package com.anydigital.inventorycontrolv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.anydigital.inventorycontrolv1.databinding.ActivityDetalhesDaOperacaoBinding
import com.google.firebase.auth.FirebaseAuth


class DetalhesDaOperacaoActivity : AppCompatActivity() {

    private var menuItem: MenuItem? = null
    private lateinit var auth: FirebaseAuth;

    private val binding: ActivityDetalhesDaOperacaoBinding by lazy {
        ActivityDetalhesDaOperacaoBinding.inflate(this.layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(this.binding.root)

        var dados = intent.extras
        var codigoUsuario = dados?.getString("codigoUsuario").toString()
        var operacao = dados?.getString("operacao").toString()
        var modelo = dados?.getString("modelo").toString()
        var imei = dados?.getString("imei").toString()
        var dataHora = dados?.getString("dataHora").toString()
        var box = dados?.getString("box").toString()
        var ean = dados?.getString("ean").toString()
        var unidade = dados?.getString("unidade").toString()

        this.binding.edOperacao.setText(operacao)
        this.binding.edUsuario.setText(codigoUsuario)
        this.binding.edModelo.setText(modelo)
        this.binding.edCodigoBarrasCadastro.setText(imei)
        this.binding.edDataHora.setText(dataHora)
        this.binding.edBoxCadastro.setText(box)
        this.binding.edEanCadastro.setText(ean)
        this.binding.edUnidadeCadastro.setText(unidade)

        var buttonOk = binding.buttonOk.setOnClickListener {
            startActivity(Intent(this,ResultadoPesquisaActivity::class.java))
            finish()

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


}