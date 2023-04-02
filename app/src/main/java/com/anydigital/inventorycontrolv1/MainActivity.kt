package com.anydigital.inventorycontrolv1

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.anydigital.inventorycontrolv1.databinding.ActivityMainBinding
import com.anydigital.inventorycontrolv1.firestore.FirestoreGravarAlterarRemoverActivity
import com.anydigital.inventorycontrolv1.firestore.FirestoreLerDadosActivity
import com.anydigital.inventorycontrolv1.storage.StorageDownloadActivity
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var auth: FirebaseAuth;

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.cardViewMainDownloadImagem.setOnClickListener(this)
        binding.cardViewMainUploadImagem.setOnClickListener(this)
        binding.cardViewMainLerDados.setOnClickListener(this)
        binding.cardViewMainGravarAlterarLerDados.setOnClickListener(this)
        binding.cardViewMainCategorias.setOnClickListener(this)
        binding.cardViewMainRegistrarEntrada.setOnClickListener(this)
        binding.cardViewMainDeslogar.setOnClickListener(this)



        permissao()
        ouvinteAutenticacao()

    }


    // Permisão
    private fun permissao(){
        val permissoes = arrayOf<String>(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.CAMERA
        )

        Util.permissao(this,100, permissoes )
    }

    // Ouvinte autenticação
    private fun ouvinteAutenticacao(){
        val auth = Firebase.auth
        auth.addAuthStateListener { authAtual ->
            if (authAtual.currentUser != null){
                Util.exibirToast(baseContext,"Usuário Logado")
            }else{
                Util.exibirToast(baseContext,"Deslogado")
            }
        }
    }

    // Ações de clique
    override fun onClick(p0: View?) {
        val cardviewMainDeslogar = findViewById<CardView>(R.id.cardView_Main_Deslogar)
        val cardviewMainDownloadImagem = findViewById<CardView>(R.id.cardView_Main_DownloadImagem)
        val cardviewMainUploadImagem = findViewById<CardView>(R.id.cardView_Main_UploadImagem)
        val cardviewMainLerDados = findViewById<CardView>(R.id.cardView_Main_LerDados)
        val cardviewMainGravarAlterarLerDados = findViewById<CardView>(R.id.cardView_Main_GravarAlterarLerDados)
        val cardviewMainCategorias = findViewById<CardView>(R.id.cardView_Main_Categorias)
        val cardviewMainRegistrarEntrada = findViewById<CardView>(R.id.cardView_Main_RegistrarEntrada)


        when(p0?.id){


            cardviewMainDownloadImagem.id ->{
                startActivity(Intent(this,StorageDownloadActivity::class.java))
            }

            cardviewMainUploadImagem.id ->{
                Util.exibirToast(baseContext,"cardView_Main_UploadImagem")
            }

            cardviewMainLerDados.id ->{
                startActivity(Intent(this,FirestoreLerDadosActivity::class.java))
            }

            cardviewMainGravarAlterarLerDados.id ->{
                startActivity(Intent(this,FirestoreGravarAlterarRemoverActivity::class.java))
            }

            cardviewMainCategorias.id ->{
                Util.exibirToast(baseContext,"cardView_Main_Categorias")
            }

            cardviewMainRegistrarEntrada.id ->{
                Util.exibirToast(baseContext,"cardView_Main_RegistrarEntrada")
                startActivity(Intent(this,LeitorActivity::class.java))
            }

            cardviewMainDeslogar.id ->{
                finish()
                val auth = Firebase.auth
                auth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
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

        for(result in grantResults){
            if (result == PackageManager.PERMISSION_DENIED){

                Util.exibirToast(baseContext,"Aceite as permisões para funcionar o aplicativo")
                finish()
                break
            }

        }

    }



}