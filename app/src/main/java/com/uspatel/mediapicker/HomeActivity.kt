package com.uspatel.mediapicker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val button : Button = findViewById(R.id.button)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            it.data?.let { intent ->
                intent.extras?.let {
                    val images = it.getStringArrayList("image")
                }
            }
        }

        button.setOnClickListener {
            val intent = Intent(this,ImagePickerActivity::class.java).putExtra(ImagePickerActivity.MAX_COUNT,2)
            resultLauncher.launch(intent)
        }
    }
}