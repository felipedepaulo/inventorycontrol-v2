package com.anydigital.inventorycontrolv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.anydigital.inventorycontrolv1.databinding.ActivityEstoqueBinding
import com.google.firebase.auth.FirebaseAuth
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EstoqueActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth;
    private var menuItem: MenuItem? = null


    private val binding: ActivityEstoqueBinding by lazy {
        ActivityEstoqueBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = Firebase.auth


        binding.btRealizarAlterarCadastro.setOnClickListener(this)
        binding.btPesquisarEstoque.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {

        var usuario = auth.currentUser?.email

        when(p0?.id){

            binding.btRealizarAlterarCadastro.id ->{
                var intent = Intent(this,CadastroDeProdutoActivity::class.java)
                intent.putExtra("botao_clicado","ButtonEstoque")
                intent.putExtra("usuario",usuario)
                startActivity(intent)

            }
            binding.btPesquisarEstoque.id ->{
                var intent = Intent(this,ResultadoPesquisaProdutoActivity::class.java)
                intent.putExtra("botao_clicado","ButtonPesquisarEstoque")
                intent.putExtra("usuario",usuario)
                startActivity(intent)
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


}