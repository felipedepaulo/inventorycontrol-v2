package com.anydigital.inventorycontrolv1

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnCreateContextMenuListener
import com.anydigital.inventorycontrolv1.databinding.ActivityMenuPrincipalBinding
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MenuPrincipalActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth;
    private var menuItem: MenuItem? = null



    private val binding: ActivityMenuPrincipalBinding by lazy {
        ActivityMenuPrincipalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        auth = Firebase.auth


        binding.buttonRegistrarEntrada.setOnClickListener(this)
        binding.buttonRegistrarSaida.setOnClickListener(this)
        binding.buttonHistoricoEntradasSaidas.setOnClickListener(this)
        binding.buttonEstoque.setOnClickListener(this)
        binding.buttonDeslogar.setOnClickListener(this)

        permissao()
        ouvinteAutenticacao()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

/*
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        println("onPrepareOptionsMenu")

        menuItem?.let {
            menu?.removeItem(it.itemId)
        }

        var menuChecked = menu?.findItem(R.id.menu2)

        if (menuChecked!!.isChecked){
            menuItem = menu?.add("Menu criado dinamicamente")
        }
        return super.onPrepareOptionsMenu(menu)
    }
*/

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

    // Permisão
    private fun permissao() {
        val permissoes = arrayOf<String>(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
        )

        Util.permissao(this, 100, permissoes)
    }

    // Ouvinte autenticação
    private fun ouvinteAutenticacao() {
        val auth = Firebase.auth
        auth.addAuthStateListener { authAtual ->
/*            if (authAtual.currentUser != null) {
                Util.exibirToast(baseContext, "Usuário Logado")
            } else {
                Util.exibirToast(baseContext, "Deslogado")
            }*/
        }
    }

    // Ações de clique
    override fun onClick(p0: View?) {

        val idButtonEstoque = binding.buttonEstoque.id
        val idButtonRegistrarEntrada = binding.buttonRegistrarEntrada.id
        val idButtonRegistrarSaida = binding.buttonRegistrarSaida.id
        val idButtonDeslogar = binding.buttonDeslogar.id
        val idbuttonHistoricoEntradasSaidas = binding.buttonHistoricoEntradasSaidas.id
        var dados = intent.extras
        var usuario = dados?.getString("usuario").toString()


        when (p0?.id) {

            idButtonEstoque -> {
                val intent = Intent(this, EstoqueActivity::class.java)

                if (usuario == null) {
                    val usuario = auth?.currentUser
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)
                //finish()

            }


            idButtonRegistrarSaida -> {

                val intent = Intent(this, LeitorActivity::class.java)

                intent.putExtra("botao_clicado", "ButtonRegistrarSaida")

                if (usuario == "null") {
                    val usuario = auth?.currentUser?.email
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                //finish()
                startActivity(intent)
            }

            idButtonRegistrarEntrada -> {

                val intent = Intent(this, LeitorActivity::class.java)

                if (usuario == "null") {
                    val usuario = auth?.currentUser?.email
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)
                //finish()

            }

            idbuttonHistoricoEntradasSaidas -> {
                var intent = Intent(this, ResultadoPesquisaActivity::class.java)

                if (usuario == null) {
                    val usuario = auth?.currentUser
                    intent.putExtra("usuario", usuario)
                } else {
                    intent.putExtra("usuario", usuario)
                }
                startActivity(intent)
                //finish()

            }

            idButtonDeslogar -> {
                finish()
                val auth = Firebase.auth
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            else -> false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {

                alerta("Aceite as permissões para funcionamento do aplicativo")
                finish()
                break
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