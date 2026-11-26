package com.tecsup.murocampus.data

import com.tecsup.murocampus.data.remote.PostApi
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.CancellationException

interface PostRepository {
    suspend fun getPosts(): Result<List<Post>>
    suspend fun getPostById(id: Int): Result<Post>
    suspend fun getCommentsByPostId(postId: Int): Result<List<Comentario>>
    suspend fun getPostsByUser(userId: Int): Result<List<Post>>
    suspend fun createPost(nuevoPost: NuevoPost): Result<Post>
    suspend fun deletePost(id: Int): Result<Unit>
    suspend fun patchPost(id: Int, title: String): Result<Post>
}

class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun getPosts(): Result<List<Post>> = try {
        val response = api.getPosts()
        Result.success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPostById(id: Int): Result<Post> = try {
        val response = api.getPostById(id)
        Result.success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCommentsByPostId(postId: Int): Result<List<Comentario>> = try {
        val response = api.getCommentsByPostId(postId)
        Result.success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getPostsByUser(userId: Int): Result<List<Post>> = try {
        val response = api.getPostsByUser(userId)
        Result.success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun createPost(nuevoPost: NuevoPost): Result<Post> = try {
        val response = api.createPost(nuevoPost)
        Result.success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deletePost(id: Int): Result<Unit> = try {
        val response = api.deletePost(id)
        if (response.isSuccessful) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Error al eliminar: ${response.code()}"))
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun patchPost(id: Int, title: String): Result<Post> = try {
        val response = api.patchPost(id, mapOf("title" to title))
        Result.success(response)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
