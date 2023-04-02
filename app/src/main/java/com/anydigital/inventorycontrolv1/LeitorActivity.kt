package com.anydigital.inventorycontrolv1


import android.Manifest
import android.content.DialogInterface
import android.content.Intent

import android.content.pm.PackageManager
import android.graphics.Color
import android.inputmethodservice.InputMethodService.InputMethodImpl
import android.os.Bundle
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler

class LeitorActivity: AppCompatActivity(),ResultHandler {

    private val REQUEST_CAMERA = 1
    private var scannerView :ZXingScannerView?=null
    private var txtResult: TextView? = null
    private lateinit var auth: FirebaseAuth;
    private var menuItem: MenuItem? = null

    var db: FirebaseFirestore? = null


    val colecao_samsung = "Samsung"
    val colecao_samsung_historico = "SamsungHistorico"

    val colecao_americanas = "Americanas"
    val colecao_americanas_historico = "AmericanasHistorico"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leitor)

        scannerView = findViewById(R.id.scanner)
        txtResult = findViewById<TextView>(R.id.txtResultScanner)

        if (!checkPermission())
            requestPermission()
    }

    private fun checkPermission():Boolean{
        return ContextCompat.checkSelfPermission(this@LeitorActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),REQUEST_CAMERA)
    }

    override fun onResume() {
        super.onResume()
        if (checkPermission()){
            if (scannerView == null){
                scannerView = findViewById(R.id.scanner)
                setContentView(scannerView)
            }
            scannerView?.setResultHandler(this)

            scannerView?.startCamera()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scannerView?.stopCamera()
    }
    override fun handleResult(p0: Result?) {

        var dados = intent.extras
        var botao_clicado = dados?.getString("botao_clicado").toString()
        var usuario = dados?.getString("usuario").toString()

        val result:String? = p0?.text
        val vibrator:Vibrator = applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(1000)
        txtResult?.text = result
        scannerView?.setResultHandler(this)
        scannerView?.startCamera()
        db = FirebaseFirestore.getInstance()


/*        val intent = Intent(this,RegistroDeEntradaActivity::class.java)
        //intent.putExtra("codigoProduto",result.toString())
        startActivity(intent)*/


        if (botao_clicado == "ButtonRegistrarSaida") {
            val intent = Intent(this, RegistroDeSaidaActivity::class.java)
            intent.putExtra("codigoProduto", result.toString())
            intent.putExtra("usuario", usuario)
            finish()
            startActivity(intent)
        }else if(botao_clicado == "ButtonCadastrarProduto"){

            var  query =
                db
                    ?.collection(colecao_americanas)
                    //?.startAt(newText)
                    //?.endAt(newText)
                    ?.whereEqualTo("imei",result.toString().trim())
                    //?.whereEqualTo("imei","1111")
                    //?.limit(10)

            query?.get()?.addOnSuccessListener { documentos ->


                if( documentos.size()==0 ){
                    var intent = Intent(this,CadastroDeProdutoActivity::class.java)
                    intent.putExtra("botao_clicado","ButtonEstoque")
                    intent.putExtra("codigoProduto", result.toString())
                    intent.putExtra("usuario",usuario)
                    startActivity(intent)

                }else{
                    alerta("Produto já cadastrado")
                }
            }?.addOnFailureListener { error ->
                //Util.exibirToast(baseContext, "Erro ao gravar dados: ${error.message.toString()}")
                alerta("Erro ao gravar dados")
            }

        }else{
            val intent = Intent(this,RegistroDeEntradaActivity::class.java)
            intent.putExtra("codigoProduto",result.toString())
            intent.putExtra("usuario",usuario)
            startActivity(intent)
            finish()

        }


/*        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Resultado")
        builder.setPositiveButton("OK") {dialog,which ->
            scannerView?.resumeCameraPreview(this@LeitorActivity)
            startActivity(intent)
        }

        builder.setMessage(result)
        val alert:AlertDialog = builder.create()
        alert.show()*/


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
