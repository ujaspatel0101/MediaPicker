package com.uspatel.mediapicker

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class MainActivity : AppCompatActivity() {

    private  var selectedImages: ArrayList<String> = ArrayList()
    private lateinit var rvMedia: RecyclerView
    private lateinit var tvCount: TextView
    private lateinit var fabDone: ExtendedFloatingActionButton
    private var count: Int = 0
    private var currentFragment = "album"
    private var resultIntent : Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        init()
        loadAlbumFragment()
    }

    private fun loadAlbumFragment() {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frmLayout, AlbumListFragment(this))
        transaction.commit()
    }

    private fun init() {
        rvMedia = findViewById(R.id.rvMedia)
        tvCount = findViewById(R.id.tvCount)
        fabDone = findViewById(R.id.fabDone)

        fabDone.setOnClickListener {
            resultIntent = Intent("com.uspatel.imagepicker.ACTION_IMAGE_RESULT")
            resultIntent!!.putExtra("image",selectedImages)
            setResult(RESULT_OK,resultIntent)
            finish()
        }
    }

    fun currentFragment(name: String) {
        currentFragment = name
    }

    override fun onBackPressed() {
        if (currentFragment == "images") {
            loadAlbumFragment()
            return
        }
        super.onBackPressed()
    }

    fun updateCount(it: Int, images : List<String>) {
        count = it
        selectedImages.apply {
            clear()
            addAll(images)
        }
        tvCount.text = it.toString()
        fabDone.visibility = if (count > 0) VISIBLE else GONE
    }
}