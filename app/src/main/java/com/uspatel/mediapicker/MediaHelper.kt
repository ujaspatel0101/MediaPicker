package com.uspatel.mediapicker

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MediaHelper : ViewModel() {

    private val _images = MutableLiveData<ArrayList<Media>>()
    val images: LiveData<ArrayList<Media>> get() = _images

    private val _albums = MutableLiveData<ArrayList<Album>>()
    val albums: LiveData<ArrayList<Album>> get() = _albums

    fun loadImages(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Media>()

            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val dateAddedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val dateAdded = cursor.getString(dateAddedColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val size = cursor.getInt(sizeColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
//                    val bm = try {
//                        if (Build.VERSION.SDK_INT >= 28){
//                            val source = ImageDecoder.createSource(context.contentResolver,contentUri)
//                            ImageDecoder.decodeBitmap(source)
//                        }else{
//                            MediaStore.Images.Media.getBitmap(context.contentResolver,contentUri)
//                        }
//                    } catch (e: Exception) {
//                        null
//                    }
                    list.add(Media(id, ">" + name, dateAdded, mimeType, size, contentUri, null))
                }
                _images.postValue(list)
            }
        }

    }

    fun loadImages(context: Context, id : Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Media>()

            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

            val selection = MediaStore.Images.ImageColumns.BUCKET_ID + " = ? "

            val selectionArg = arrayOf("$id")

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                selectionArg,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val dateAddedColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val dateAdded = cursor.getString(dateAddedColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val size = cursor.getInt(sizeColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
//                    val bm = try {
//                        if (Build.VERSION.SDK_INT >= 28){
//                            val source = ImageDecoder.createSource(context.contentResolver,contentUri)
//                            ImageDecoder.decodeBitmap(source)
//                        }else{
//                            MediaStore.Images.Media.getBitmap(context.contentResolver,contentUri)
//                        }
//                    } catch (e: Exception) {
//                        null
//                    }
                    list.add(Media(id, ">" + name, dateAdded, mimeType, size, contentUri, null))
                }
                _images.postValue(list)
            }
        }

    }

    fun loadImageAlbums(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val list = ArrayList<Album>()

            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

            val BUCKET_GROUP_BY = "1) GROUP BY 1,(2"
            val BUCKET_ORDER_BY = "MAX(datetaken) DESC"

            context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.DATA
                ),
                BUCKET_GROUP_BY,
                null,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(bucketNameColumn)
                    val data = cursor.getString(dataColumn)

                    list.add(Album(name, id, data))
                }
                _albums.postValue(list)
            }
        }

    }

    suspend fun loadImageForAlbum(context: Context, uri: Uri): String {
        val list = ArrayList<String>()
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        context.contentResolver.query(
            uri,
            null,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val path = cursor.getString(pathColumn)

                list.add(path)
            }
        }
        return if (list.isNotEmpty()) list[0] else ""

    }

}