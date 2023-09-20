package br.com.sommelier.base.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import br.com.sommelier.util.Constants.IMAGE_URL
import br.com.sommelier.util.Constants.KEY_IMAGE_URI
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

const val TAG = "ImageWorker"

class ImageUploaderWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val coroutineDispatcher: CoroutineDispatcher,
    private val storageReference: StorageReference
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = withContext(coroutineDispatcher) {
        val inputFileUri = inputData.getString(KEY_IMAGE_URI)
        return@withContext try {
            uploadImageFromUri(Uri.parse(inputFileUri))
        } catch (genericException: Exception) {
            genericException.printStackTrace()
            Result.failure()
        }
    }

    private suspend fun uploadImageFromUri(imageUri: Uri): Result =
        suspendCoroutine { continuation ->
            imageUri.lastPathSegment?.let {
                val imageReference = storageReference.child(it)
                Log.d(TAG, it)
                val uploadTask = imageReference.putFile(imageUri)
                uploadTask.addOnSuccessListener { uri ->
                    Log.d(TAG, uri.toString())
                    val outputData = Data.Builder()
                        .putString(IMAGE_URL, uri.toString())
                        .build()
                    continuation.resume(Result.success(outputData))
                }.addOnFailureListener {
                    continuation.resume(Result.failure())
                }
            } ?: continuation.resume(Result.failure())
        }
}
