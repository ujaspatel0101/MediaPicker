package com.uspatel.mediapicker

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ImageListFragment(
    private val id: Long,
   private val maxCount: Int,
    private val imagePickerActivity: ImagePickerActivity
) : Fragment(R.layout.fragment_image_list) {
    private lateinit var rvImages : RecyclerView
    private var adapter: ImageAdapter? = null

    private val viewModel : MediaHelper by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePickerActivity.currentFragment("images")
        rvImages = view.findViewById(R.id.rvAlbums)

        val onItemClickListener : (Int, List<String>) -> Unit = { it, list ->
            imagePickerActivity.updateCount(it, list)
        }

        adapter = ImageAdapter(requireContext(), onItemClickListener,maxCount)
        rvImages.adapter = adapter
        rvImages.layoutManager = GridLayoutManager(requireContext(),3)

    }



    override fun onStart() {
        super.onStart()


        viewModel.images.observe(this){
            adapter?.setImages(it)
            Log.d("TAG", "init: ${it.size}")
//            val file = File(it[0].imgPath)
        }

        viewModel.loadImages(requireContext(), id)
    }

    override fun onStop() {
        viewModel.images.removeObservers(requireActivity())
        super.onStop()
    }
}
