package com.anydigital.inventorycontrolv1.storage

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import com.anydigital.inventorycontrolv1.R
import com.anydigital.inventorycontrolv1.util.Util
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class StorageDownloadActivity : AppCompatActivity(), View.OnClickListener {

    var storage: FirebaseStorage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_download)

        val buttonStorageDownloadDownload =
            findViewById<Button>(R.id.button_StorageDownload_Download)
        val buttonStorageDownloadRemover = findViewById<Button>(R.id.button_StorageDownload_Remover)

        buttonStorageDownloadDownload.setOnClickListener(this)
        buttonStorageDownloadRemover.setOnClickListener(this)

        val progressBarStorageDownload = findViewById<ProgressBar>(R.id.progressBar_StorageDownload)
        progressBarStorageDownload.visibility = View.GONE

        storage = Firebase.storage


    }

    override fun onClick(p0: View?) {
        val buttonStorageDownloadDownload =
            findViewById<Button>(R.id.button_StorageDownload_Download)
        val buttonStorageDownloadRemover = findViewById<Button>(R.id.button_StorageDownload_Remover)

        buttonStorageDownloadDownload.setOnClickListener(this)
        buttonStorageDownloadRemover.setOnClickListener(this)

        when (p0?.id) {
            buttonStorageDownloadDownload.id -> {
                button_Download()
            }
            buttonStorageDownloadRemover.id -> {
                button_Remover()

            }
        }
    }

    private fun button_Remover() {
        if (Util.statusInternet(baseContext)) {
            remover_Imagem_1_Url()
        } else {
            Util.exibirToast(baseContext, "Sem conexão com a internet")
        }
    }

    private fun remover_Imagem_1_Url() {
        val progressBarStorageDownload = findViewById<ProgressBar>(R.id.progressBar_StorageDownload)
        progressBarStorageDownload.visibility = View.VISIBLE

        var url = "https://firebasestorage.googleapis.com/v0/b/inventorycontrolv1.appspot.com/o/imagem1%2F1649887677502.jpg?alt=media&token=28eea378-6d5f-4706-a3a8-99461f3be3bb"
        val reference = storage!!.getReferenceFromUrl(url)
        reference.delete().addOnSuccessListener {
            Util.exibirToast(baseContext,"Sucesso ao remover imagem")
        }.addOnFailureListener {error ->
            Util.exibirToast(baseContext,"Erro ao remover imagem: ${error.message.toString()}")
        }
        finish()
        startActivity(Intent(this, StorageDownloadActivity::class.java))
    }

    private fun button_Download() {
        if (Util.statusInternet(baseContext)) {
            //download_Imagem_1()
            //download_Imagem_2_Url()
            download_Imagem_3_Nome()
        } else {
            Util.exibirToast(baseContext, "Sem conexão com a internet")
        }
    }

    private fun download_Imagem_1_Url() {
        val urlImagem =
            "https://firebasestorage.googleapis.com/v0/b/inventorycontrolv1.appspot.com/o/imagem1%2F1649887677502.jpg?alt=media&token=28eea378-6d5f-4706-a3a8-99461f3be3bb"
        val imageViewStorageDownload = findViewById<ImageView>(R.id.imageView_StorageDownload)
        Glide.with(baseContext).asBitmap().load(urlImagem)
            .placeholder(R.drawable.baseline_autorenew_24).into(imageViewStorageDownload)
    }

    private fun download_Imagem_2_Url() {
        val progressBarStorageDownload = findViewById<ProgressBar>(R.id.progressBar_StorageDownload)
        progressBarStorageDownload.visibility = View.VISIBLE

        val urlImagem =
            "https://firebasestorage.googleapis.com/v0/b/inventorycontrolv1.appspot.com/o/imagem1%2F1649887677502.jpg?alt=media&token=28eea378-6d5f-4706-a3a8-99461f3be3bb"
        val imageViewStorageDownload = findViewById<ImageView>(R.id.imageView_StorageDownload)

        Glide.with(baseContext).asBitmap().load(urlImagem)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Util.exibirToast(
                        baseContext,
                        "Erro ao realizar o download da imagem: ${e.toString()}"
                    )
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Util.exibirToast(baseContext, "Sucesso ao realizar o download da imagem")
                    progressBarStorageDownload.visibility = View.GONE
                    return false
                }

            }).into(imageViewStorageDownload)

    }

    private fun download_Imagem_3_Nome() {
        val progressBarStorageDownload = findViewById<ProgressBar>(R.id.progressBar_StorageDownload)
        progressBarStorageDownload.visibility = View.VISIBLE

        val reference = storage!!.reference.child("imagem1").child("1649887677502.jpg")
        reference.downloadUrl.addOnSuccessListener { task ->

            val urlImagem = task.toString()
            val imageViewStorageDownload = findViewById<ImageView>(R.id.imageView_StorageDownload)

            Glide.with(baseContext).asBitmap().load(urlImagem)
                .listener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Util.exibirToast(
                            baseContext,
                            "Erro ao realizar o download da imagem: ${e.toString()}"
                        )
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Util.exibirToast(baseContext, "Sucesso ao realizar o download da imagem")
                        progressBarStorageDownload.visibility = View.GONE
                        return false
                    }

                }).into(imageViewStorageDownload)

        }.addOnFailureListener { error ->
            val url = error
            Util.exibirToast(baseContext, "Erro ao carregar a imagem: ${url.message.toString()}")
        }
    }

}

