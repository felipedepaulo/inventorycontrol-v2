package com.anydigital.inventorycontrolv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.anydigital.inventorycontrolv1.databinding.ActivityDetalhesDoProdutoBinding
import com.google.firebase.auth.FirebaseAuth

class DetalhesDoProdutoActivity : AppCompatActivity() {

    private val binding: ActivityDetalhesDoProdutoBinding by lazy {
        ActivityDetalhesDoProdutoBinding.inflate(layoutInflater)
    }

    private var menuItem: MenuItem? = null
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var dados = intent.extras
        var modelo = dados?.getString("modelo").toString()
        var cor = dados?.getString("cor").toString()
        var imei = dados?.getString("imei").toString()
        var fabricante = dados?.getString("fabricante").toString()
        var ean = dados?.getString("ean").toString()
        var box = dados?.getString("box").toString()
        var unidade = dados?.getString("unidade").toString()

        this.binding.edFabricante.setText(fabricante)
        this.binding.edModelo.setText(modelo)
        this.binding.edCor.setText(cor)
        this.binding.edCodigoBarrasCadastro.setText(imei)
        this.binding.edEan.setText(ean)
        this.binding.edBox.setText(box)
        this.binding.edUnidade.setText(unidade)

        var buttonOk = binding.buttonOk.setOnClickListener {
            startActivity(Intent(this, ResultadoPesquisaProdutoActivity::class.java))
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

