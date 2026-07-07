package com.example.data

import kotlinx.coroutines.flow.Flow

class SarkariGuruRepository(private val dao: SarkariGuruDao) {
    val profile: Flow<UserProfile?> = dao.getProfile()
    val allDocuments: Flow<List<UserDocument>> = dao.getAllDocuments()

    suspend fun saveProfile(profile: UserProfile) {
        dao.saveProfile(profile)
    }

    suspend fun clearProfile() {
        dao.clearProfile()
    }

    suspend fun getDocumentByType(docType: String): UserDocument? {
        return dao.getDocumentByType(docType)
    }

    suspend fun saveDocument(document: UserDocument) {
        dao.saveDocument(document)
    }

    suspend fun deleteDocument(docType: String) {
        dao.deleteDocument(docType)
    }
}
