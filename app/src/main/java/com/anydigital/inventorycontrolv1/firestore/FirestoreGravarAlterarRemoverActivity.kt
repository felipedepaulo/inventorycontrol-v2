package com.anydigital.inventorycontrolv1.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.anydigital.inventorycontrolv1.databinding.ActivityFirestoreGravarAlterarRemoverBinding
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreGravarAlterarRemoverActivity : AppCompatActivity(), View.OnClickListener {

    private val binding: ActivityFirestoreGravarAlterarRemoverBinding by lazy {
        ActivityFirestoreGravarAlterarRemoverBinding.inflate(layoutInflater)
    }

    var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_firestore_gravar_alterar_remover)
        setContentView(binding.root)

        binding.buttonFirestoreGravarAlterarRemoverSalvar.setOnClickListener(this)
        binding.buttonFirestoreGravarAlterarRemoverAlterar.setOnClickListener(this)
        binding.buttonFirestoreGravarAlterarRemoverRemover.setOnClickListener(this)

        db = FirebaseFirestore.getInstance()


    }

    //----CLICKS
    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.buttonFirestoreGravarAlterarRemoverSalvar.id -> {
                buttonSalvar()
            }
            binding.buttonFirestoreGravarAlterarRemoverAlterar.id -> {
                buttonAlterar()
            }
            binding.buttonFirestoreGravarAlterarRemoverRemover.id -> {
                buttonRemover()
            }
            else -> return

        }

    }

    //----Ação de Clicks para salvar dados
    private fun buttonSalvar() {
        val nome = binding.editTextFirestoreGravarAlterarRemoverGerenteNome.text.toString()
        val idadeString = binding.editTextFirestoreGravarAlterarRemoverGerenteIdade.text.toString()
        val idNomePasta = binding.editTextFirestoreGravarAlterarRemoverNomePasta.text.toString()

        if (!nome.trim().isEmpty() && !idadeString.trim().isEmpty() && !idNomePasta.trim()
                .isEmpty()
        ) {

            if (Util.statusInternet(baseContext)) {
                val idade: Int = idadeString.toInt()

                val gerente = Gerente(nome, idade, true)

                salvarDados(gerente, idNomePasta)
            } else {
                Util.exibirToast(baseContext, "Sem conexão com Internet")
            }
        } else {
            Util.exibirToast(baseContext, "Insira todos os campos de forma correta")
        }
    }

    //----Ação de Clicks para alterar dados
    private fun buttonAlterar() {
        val nome = binding.editTextFirestoreGravarAlterarRemoverGerenteNome.text.toString()
        val idadeString = binding.editTextFirestoreGravarAlterarRemoverGerenteIdade.text.toString()
        val idNomePasta = binding.editTextFirestoreGravarAlterarRemoverNomePasta.text.toString()

        if (!nome.trim().isEmpty() && !idadeString.trim().isEmpty() && !idNomePasta.trim()
                .isEmpty()
        ) {

            if (Util.statusInternet(baseContext)) {
                val idade: Int = idadeString.toInt()

                val gerente1 = Gerente(nome, idade, true)

                alterarDados(gerente1, idNomePasta)
            } else {
                Util.exibirToast(baseContext, "Sem conexão com Internet")
            }
        } else {
            Util.exibirToast(baseContext, "Insira todos os campos de forma correta")
        }
    }

    //----Ação de Clicks para remover dados
    private fun buttonRemover() {
        val idNomePasta = binding.editTextFirestoreGravarAlterarRemoverNomePasta.text.toString()

        if (!idNomePasta.trim()
                .isEmpty()
        ) {

            if (Util.statusInternet(baseContext)) {
                removerDados(idNomePasta)
            } else {
                Util.exibirToast(baseContext, "Sem conexão com Internet")
            }
        } else {
            Util.exibirToast(baseContext, "Insira todos os campos de forma correta")
        }
    }

    //----Salvar dados Firebase
    private fun salvarDados(gerente: Gerente, idNomePasta: String) {
        val reference: CollectionReference = db!!.collection("Gerentes")

        val dialogProgess = DialogProgress()
        dialogProgess.show(supportFragmentManager, "0")

/*        var data = hashMapOf<String, Any>(
            "Nome" to nome,
            "Idade" to idade,
            "Fumante" to false
        )*/


        // Grava no banco enviado o nome do documento (idNomePasta)
        reference.document(idNomePasta).set(gerente).addOnSuccessListener {
            dialogProgess.dismiss()
            Util.exibirToast(baseContext, "Sucesso ao gravar dados")
        }.addOnFailureListener { error ->
            dialogProgess.dismiss()
            Util.exibirToast(baseContext, "Erro ao gravar dados: ${error.message.toString()}")
        }

        // Grava no banco sem enviar o nome do documento (idNomePasta), sendo criado automaticamente no firestore
        /*reference.add(gerente).addOnSuccessListener {
            Util.exibirToast(baseContext,"Sucesso ao gravar dados")
        }.addOnFailureListener {error ->
            Util.exibirToast(baseContext,"Erro ao gravar dados: ${error.message.toString()}")
        }*/

        // Grava no banco sem enviar o nome do documento (idNomePasta) de outra forma, sendo criado automaticamente no firestore
        /*
        reference.document().set(gerente).addOnSuccessListener {
            Util.exibirToast(baseContext,"Sucesso ao gravar dados")
        }.addOnFailureListener {error ->
            Util.exibirToast(baseContext,"Erro ao gravar dados: ${error.message.toString()}")
        }
        */

    }

    //----Alterar dados Firebase
    private fun alterarDados(gerente1: Gerente, idNomePasta: String) {
        val reference: CollectionReference = db!!.collection("Gerentes")

        val dialogProgess = DialogProgress()
        dialogProgess.show(supportFragmentManager, "0")

        val gerente1 = hashMapOf(
            "nome" to gerente1.Nome,
            "idade" to gerente1.Idade,
            "fumante" to gerente1.Fumante
        )

        // Grava no banco enviado o nome do documento (idNomePasta)
        reference.document(idNomePasta).update(gerente1 as Map<String, Any>).addOnSuccessListener {
            dialogProgess.dismiss()
            Util.exibirToast(baseContext, "Sucesso ao gravar dados")
        }.addOnFailureListener { error ->
            dialogProgess.dismiss()
            Util.exibirToast(baseContext, "Erro ao gravar dados: ${error.message.toString()}")
        }
    }

    //----Remover dados Firebase
    private fun removerDados(idNomePasta: String) {
        val reference: CollectionReference = db!!.collection("Gerentes")

        val dialogProgess = DialogProgress()
        dialogProgess.show(supportFragmentManager, "0")

        // Grava no banco enviado o nome do documento (idNomePasta)
        reference.document(idNomePasta).delete().addOnSuccessListener {
            dialogProgess.dismiss()
            Util.exibirToast(baseContext, "Sucesso ao remover dados")
        }.addOnFailureListener { error ->
            dialogProgess.dismiss()
            Util.exibirToast(baseContext, "Erro ao remover dados: ${error.message.toString()}")
        }
    }

}


