package com.uspatel.mediapicker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AlbumListFragment(private val mainActivity: MainActivity) : Fragment(R.layout.fragment_album_list) {

    private lateinit var rvAlbums :RecyclerView
    private var adapter: AlbumAdapter? = null

    private val viewModel : MediaHelper by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.currentFragment("album")

        rvAlbums = view.findViewById(R.id.rvAlbums)

        val onItemClickListener : (Long) -> Unit = {
            loadImageListFragment(it)
        }

        adapter = AlbumAdapter(requireContext(), onItemClickListener)
        rvAlbums.adapter = adapter
        rvAlbums.layoutManager = GridLayoutManager(requireContext(),2)

    }

    private fun loadImageListFragment(id: Long) {
       val manager = requireActivity().supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frmLayout, ImageListFragment(id, mainActivity))
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()


        viewModel.albums.observe(this){
            adapter?.setImages(it)
            Log.d("TAG", "init: ${it.size}")
//            val file = File(it[0].imgPath)
        }

        viewModel.loadImageAlbums(requireContext())
    }

    override fun onStop() {
        viewModel.albums.removeObservers(requireActivity())
        super.onStop()
    }

}