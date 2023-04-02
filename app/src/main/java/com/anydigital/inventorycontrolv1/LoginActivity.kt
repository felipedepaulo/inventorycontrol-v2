package com.anydigital.inventorycontrolv1

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity(), View.OnClickListener {


    var auth: FirebaseAuth? = null

// ...
// Initialize Firebase Auth
    //auth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.button_login)

        buttonLogin.setOnClickListener(this)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val user = auth?.currentUser

        val email = auth?.currentUser?.email

        if (user != null){
            finish() // Finaliza a tela de login ao retornar a ela
            val intent = Intent(this,MenuPrincipalActivity::class.java)
            intent.putExtra("usuario",email)
            startActivity(intent)

        }

    }

    override fun onClick(p0: View?) {
        val buttonLogin = findViewById<Button>(R.id.button_login)
        when (p0?.id) {
            buttonLogin.id -> {
                buttonLogin()
            }
            else -> false
        }
    }

    fun buttonLogin() {
        val editText_Login_Email = findViewById<EditText>(R.id.editText_Login_Email)
        val email = editText_Login_Email.text.toString()

        val editText_Login_Senha = findViewById<EditText>(R.id.editText_Login_Senha)
        val password = editText_Login_Senha.text.toString()


        if (!email.trim().equals("") && !password.trim().equals("")) {
            if (Util.statusInternet(this)) {
                login(email, password)
            } else {
                alerta("Você está sem conexão com a Internet")
            }
            //Util.exibirToast(this,"Você inseriu todos os dados ")
        } else {
            alerta("Preencha todos os dados")
        }
    }

    fun login(email: String, password: String) {
        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager,"1")

        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                dialogProgress.dismiss()

                if (task.isSuccessful) {

                    //Util.exibirToast(baseContext, "Login realizado com sucesso")
                    finish() // Finaliza a tela de login ao retornar a ela
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                } else {

                    //Util.exibirToast(baseContext, "Falha de Login ${task.exception.toString()}")

                    val erro = task.exception.toString()
                    errosFirebase(erro)
                    Log.i("signInWithEmailAndPassword", task.exception.toString())
                }
            }


        /*
        auth?.signInWithEmailAndPassword(email, password)
            ?.addOnSuccessListener {
                Util.exibirToast(baseContext, "Login realizado com sucesso")
            }?.addOnFailureListener{error ->
                val erro = error.message.toString()
                errosFirebase(erro)
                Log.i("signInWithEmailAndPassword", error.message.toString())
            }
        */

    }

    fun errosFirebase(erro: String) {
        if (erro.contains("The password is invalid")) {
            alerta("A senha é inválida ou não foi informada")
        } else if (erro.contains("There is no user record corresponding to this identifier")) {
            alerta("Esse email não é válido")
        } else {
            alerta("Email ou senha incorreto")
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


