package com.example.data

import kotlinx.coroutines.flow.Flow

class SarkariGuruRepository(private val dao: SarkariGuruDao) {
    val profile: Flow<UserProfile?> = dao.getProfile()

    suspend fun saveProfile(profile: UserProfile) {
        dao.saveProfile(profile)
    }

    suspend fun clearActiveProfile() {
        dao.clearProfile()
    }

    // Document operations with user phone scoping
    fun getAllDocuments(phone: String): Flow<List<UserDocument>> {
        return dao.getAllDocuments(phone)
    }

    suspend fun getDocumentByType(phone: String, docType: String): UserDocument? {
        return dao.getDocumentByType(phone, docType)
    }

    suspend fun saveDocument(document: UserDocument) {
        dao.saveDocument(document)
    }

    suspend fun deleteDocument(phone: String, docType: String) {
        dao.deleteDocument(phone, docType)
    }

    // User Account operations
    suspend fun getAccountByPhone(phone: String): UserAccount? {
        return dao.getAccountByPhone(phone)
    }

    suspend fun saveAccount(account: UserAccount) {
        dao.saveAccount(account)
    }

    // Saved Jobs operations
    fun getSavedJobs(phone: String): Flow<List<SavedJob>> {
        return dao.getSavedJobs(phone)
    }

    suspend fun saveJob(job: SavedJob) {
        dao.saveJob(job)
    }

    suspend fun deleteSavedJob(phone: String, jobTitle: String) {
        dao.deleteSavedJob(phone, jobTitle)
    }
}
