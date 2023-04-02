package com.anydigital.inventorycontrolv1.firestore

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.anydigital.inventorycontrolv1.databinding.ActivityFirestoreLerDadosBinding
import com.anydigital.inventorycontrolv1.util.DialogProgress
import com.anydigital.inventorycontrolv1.util.Util
import com.google.firebase.firestore.*

class FirestoreLerDadosActivity : AppCompatActivity() {

    var db: FirebaseFirestore? = null
    var reference: CollectionReference? = null
    var referenceDocumento: DocumentReference? = null

    private val binding: ActivityFirestoreLerDadosBinding by lazy {
        ActivityFirestoreLerDadosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_firestore_ler_dados)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        var gerente = Gerente()

        reference = db?.collection("Gerentes")
        referenceDocumento = db!!.collection("Gerentes").document("gerente0")

        //ouvinte_1()
        //ouvinte_2()
        //ouvinte_3()
        //ouvinte_4()
        //ouvinte_5()
        //ouvinte_6()
        ouvinte_7()

    }

    @SuppressLint("SetTextI18n")
    private fun ouvinte_1() {
        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante


        referenceDocumento!!.get().addOnSuccessListener { documento ->

            if (documento != null && documento.exists()) {
                val dados = documento.data

                val key = documento.id //Gerente0

                val nome = dados?.get("Nome").toString()
                val idade = dados?.get("Idade").toString()
                val fumante = dados?.get("Fumante").toString()

                textViewFirestoreLerDadosNome.setText(key + "\n" + nome)
                textViewFirestoreLerDadosIdade.setText(key + "\n" + idade)
                textViewFirestoreLerDadosFumante.setText(key + "\n" + fumante)
            } else {
                Util.exibirToast(
                    baseContext,
                    "Erro ao ler o documento, ele não existe ou estã vazio"
                )
            }

        }.addOnFailureListener { error ->
            Util.exibirToast(
                baseContext,
                "Erro ao ler dados do servidor ${error.message.toString()}"
            )
        }
/*        db!!.collection("Gerentes").document("gerente0").get().addOnCompleteListener {task ->
            if (task.isSuccessful){
                val documento = task.result
                if (documento!=null && documento.exists()){
                    val dados = documento.data
                    val nome = dados?.get("Nome").toString()
                    val idade = dados?.get("Idade").toString()
                    val fumante = dados?.get("Fumante").toString()

                    textViewFirestoreLerDadosNome.setText(nome)
                    textViewFirestoreLerDadosIdade.setText(idade)
                    textViewFirestoreLerDadosFumante.setText(fumante)

                }else{
                    Util.exibirToast(baseContext,"Erro ao ler o documento, ele não existe ou estã vazio")
                }
            }else{
                Util.exibirToast(baseContext,"Erro ao ler dados do servidor ${task.exception.toString()}")
            }
        }*/
    }

    @SuppressLint("SetTextI18n")
    private fun ouvinte_2() {
        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "0")

        referenceDocumento!!.get().addOnSuccessListener { documento ->

            dialogProgress.dismiss()

            if (documento != null && documento.exists()) {
                val dados = documento.data

                val key = documento.id //Gerente0

                val gerente = documento.toObject(Gerente::class.java)

                textViewFirestoreLerDadosNome.setText("${gerente?.Nome}")
                textViewFirestoreLerDadosIdade.setText("${gerente?.Idade}")
                textViewFirestoreLerDadosFumante.setText("${gerente?.Fumante}")
            } else {
                Util.exibirToast(
                    baseContext,
                    "Erro ao ler o documento, ele não existe ou estão vazio"
                )
            }

        }.addOnFailureListener { error ->

            dialogProgress.dismiss()
            Util.exibirToast(
                baseContext,
                "Erro ao ler dados do servidor ${error.message.toString()}"
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun ouvinte_3() {
        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante

        var textViewFirestoreLerDadosNome2 = binding.textViewFirestoreLerDadosNome2
        val textViewFirestoreLerDadosIdade2 = binding.textViewFirestoreLerDadosIdade2
        val textViewFirestoreLerDadosFumante2 = binding.textViewFirestoreLerDadosFumante2

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "0")

        var listaGerentes: MutableList<Gerente> = ArrayList<Gerente>()

        reference!!.get().addOnSuccessListener { documentos ->

            if (documentos != null) {
                for (documento in documentos) {
                    val key = documento.id

                    val nome = documento.get("Nome").toString()
                    val idade = documento.get("Idade").toString()
                    val fumante = documento.get("Fumante").toString()

                    val gerente = documento.toObject(Gerente::class.java)

                    listaGerentes.add(gerente)

                    //Log.d("felipeteste","Nome pasta: ${key} -- Gerente Nome: ${gerente.Nome}")
                    //Log.d("felipeteste","Nome pasta: ${key} -- Gerente Nome: $nome -- Idade: $idade")
                    dialogProgress.dismiss()
                }


                textViewFirestoreLerDadosNome.text = "${listaGerentes.get(0).Nome}"
                textViewFirestoreLerDadosIdade.text = "${listaGerentes.get(0).Idade}"
                textViewFirestoreLerDadosFumante.text = "${listaGerentes.get(0).Fumante}"

                textViewFirestoreLerDadosNome2.text = "${listaGerentes.get(1).Nome}"
                textViewFirestoreLerDadosIdade2.text = "${listaGerentes.get(1).Idade}"
                textViewFirestoreLerDadosFumante2.text = "${listaGerentes.get(1).Fumante}"

            } else {
                dialogProgress.dismiss()
                Util.exibirToast(
                    baseContext,
                    "Erro ao ler o documento, ele não existe ou estão vazio"
                )
            }

        }.addOnFailureListener { error ->
            dialogProgress.dismiss()
            Util.exibirToast(
                baseContext,
                "Erro ao ler dados do servidor ${error.message.toString()}"
            )

        }

    }

    private fun ouvinte_4() {

        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante

        var textViewFirestoreLerDadosNome2 = binding.textViewFirestoreLerDadosNome2
        val textViewFirestoreLerDadosIdade2 = binding.textViewFirestoreLerDadosIdade2
        val textViewFirestoreLerDadosFumante2 = binding.textViewFirestoreLerDadosFumante2

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "0")

        //var listaGerentes: MutableList<Gerente> = ArrayList<Gerente>()

        reference!!.document("gerente0").addSnapshotListener { documento, error ->


            if (error != null) {
                Util.exibirToast(
                    baseContext,
                    "Erro de comunicação com o servidor ${error.message.toString()}"
                )
            } else if (documento != null && documento.exists()) {
                val key = documento.id //Gerente0

                val gerente = documento.toObject(Gerente::class.java)

                textViewFirestoreLerDadosNome.setText("${gerente?.Nome}")
                textViewFirestoreLerDadosIdade.setText("${gerente?.Idade}")
                textViewFirestoreLerDadosFumante.setText("${gerente?.Fumante}")

            } else {
                Util.exibirToast(
                    baseContext,
                    "Essa pasta não existe ou está vazia ${error?.message.toString()}"
                )
            }
        }
        dialogProgress.dismiss()


    }

    private fun ouvinte_5() {
        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante

        var textViewFirestoreLerDadosNome2 = binding.textViewFirestoreLerDadosNome2
        val textViewFirestoreLerDadosIdade2 = binding.textViewFirestoreLerDadosIdade2
        val textViewFirestoreLerDadosFumante2 = binding.textViewFirestoreLerDadosFumante2

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager, "0")

        var listaGerentes: MutableList<Gerente> = ArrayList<Gerente>()

        reference!!.addSnapshotListener { documentos, error ->

            dialogProgress.dismiss()

            if (error != null) {
                Util.exibirToast(
                    baseContext,
                    "Erro na comunicação com o servidor: ${error.message.toString()}"
                )
            } else if (documentos != null) {

                listaGerentes.clear()

                if (documentos != null) {
                    for (documento in documentos) {
                        val key = documento.id

                        //val nome = documento.get("Nome").toString()
                        //val idade = documento.get("Idade").toString()
                        //val fumante = documento.get("Fumante").toString()

                        val gerente = documento.toObject(Gerente::class.java)

                        listaGerentes.add(gerente)

                        //Log.d("felipeteste","Nome pasta: ${key} -- Gerente Nome: ${gerente.Nome}")
                        //Log.d("felipeteste","Nome pasta: ${key} -- Gerente Nome: $nome -- Idade: $idade")

                    }
                    textViewFirestoreLerDadosNome.text = "${listaGerentes.get(0).Nome}"
                    textViewFirestoreLerDadosIdade.text = "${listaGerentes.get(0).Idade}"
                    textViewFirestoreLerDadosFumante.text = "${listaGerentes.get(0).Fumante}"

                    textViewFirestoreLerDadosNome2.text = "${listaGerentes.get(1).Nome}"
                    textViewFirestoreLerDadosIdade2.text = "${listaGerentes.get(1).Idade}"
                    textViewFirestoreLerDadosFumante2.text = "${listaGerentes.get(1).Fumante}"

                } else {
                    Util.exibirToast(baseContext, "Está pasta não existe ou está vazia")
                }
            }
        }

    }

    // Usando consulta de multiplos dados recomendado pelo Google
    private fun ouvinte_6() {
        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante

        var textViewFirestoreLerDadosNome2 = binding.textViewFirestoreLerDadosNome2
        val textViewFirestoreLerDadosIdade2 = binding.textViewFirestoreLerDadosIdade2
        val textViewFirestoreLerDadosFumante2 = binding.textViewFirestoreLerDadosFumante2

        //val query = reference!!.whereEqualTo("Fumante",false)

        reference!!.addSnapshotListener { documentos, error ->


            if (error != null) {
                Util.exibirToast(
                    baseContext,
                    "Erro na comunicação com o servidor: ${error.message.toString()}"
                )
            } else if (documentos != null) {
                for (documento in documentos.documentChanges){

                    when(documento.type){
                        DocumentChange.Type.ADDED-> {
                            val key = documento.document.id
                            val gerente = documento.document.toObject(Gerente::class.java)
                            Log.d("Type.ADDED","ADDED - Key: ${key} Nome: ${gerente.Nome}")
                        }
                        DocumentChange.Type.MODIFIED->{
                            val key = documento.document.id
                            val gerente = documento.document.toObject(Gerente::class.java)
                            Log.d("Type.MODIFIED","MODIFIED - Key: ${key} Nome: ${gerente.Nome}")

                        }
                        DocumentChange.Type.REMOVED->{
                            val key = documento.document.id
                            val gerente = documento.document.toObject(Gerente::class.java)
                            Log.d("Type.REMOVED","REMOVED - Key: ${key} Nome: ${gerente.Nome}")
                        }

                    }//fim do when

                }// fim do for

            } else {
                Util.exibirToast(baseContext, "Está pasta não existe ou está vazia")
            }
        }
    }

    // Usando Query
    private fun ouvinte_7() {
        var textViewFirestoreLerDadosNome = binding.textViewFirestoreLerDadosNome
        val textViewFirestoreLerDadosIdade = binding.textViewFirestoreLerDadosIdade
        val textViewFirestoreLerDadosFumante = binding.textViewFirestoreLerDadosFumante

        var textViewFirestoreLerDadosNome2 = binding.textViewFirestoreLerDadosNome2
        val textViewFirestoreLerDadosIdade2 = binding.textViewFirestoreLerDadosIdade2
        val textViewFirestoreLerDadosFumante2 = binding.textViewFirestoreLerDadosFumante2

        //val query = reference!!.whereEqualTo("Fumante",false)
        //val query = reference!!.whereLessThan("Idade",40)
        //val query = reference!!.whereGreaterThan("Idade",39)
        //val query = reference!!.whereEqualTo("Fumante",true).whereGreaterThan("Idade",39)
        //val query = reference!!.orderBy("Nome", Query.Direction.ASCENDING).limit(3)


        //val query = reference!!.orderBy("Nome").whereEqualTo("Nome","Leonardo").limit(3)
        val query = reference!!.orderBy("Nome").startAt("L").endAt("L"+"\uf8ff").limit(3)

        query.addSnapshotListener { documentos, error ->


            if (error != null) {
                Util.exibirToast(
                    baseContext,
                    "Erro na comunicação com o servidor: ${error.message.toString()}"
                )
            } else if (documentos != null) {
                for (documento in documentos.documentChanges){

                    when(documento.type){
                        DocumentChange.Type.ADDED-> {
                            val key = documento.document.id
                            val gerente = documento.document.toObject(Gerente::class.java)
                            Log.d("Type.ADDED","ADDED - Key: ${key} Nome: ${gerente.Nome}")
                        }
                        DocumentChange.Type.MODIFIED->{
                            val key = documento.document.id
                            val gerente = documento.document.toObject(Gerente::class.java)
                            Log.d("Type.MODIFIED","MODIFIED - Key: ${key} Nome: ${gerente.Nome}")

                        }
                        DocumentChange.Type.REMOVED->{
                            val key = documento.document.id
                            val gerente = documento.document.toObject(Gerente::class.java)
                            Log.d("Type.REMOVED","REMOVED - Key: ${key} Nome: ${gerente.Nome}")
                        }

                    }//fim do when

                }// fim do for

            } else {
                Util.exibirToast(baseContext, "Está pasta não existe ou está vazia")
            }
        }
    }

}






