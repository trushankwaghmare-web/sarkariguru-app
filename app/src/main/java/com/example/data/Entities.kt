package com.example.data

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Singleton profile
    val name: String,
    val phone: String,
    val dob: String,
    val category: String = "General",
    val isRegistered: Boolean = true
)

@Entity(tableName = "user_documents")
data class UserDocument(
    @PrimaryKey val docType: String, // "10TH_MARKSHEET", "12TH_MARKSHEET", "AADHAAR"
    val docName: String, // "10th Marksheet", etc.
    val rollNumber: String,
    val marks: String,
    val year: String,
    val nameOnDoc: String,
    val docNum: String, // Aadhaar number or other document identifier
    val scanTimestamp: Long = System.currentTimeMillis()
)

@Dao
interface SarkariGuruDao {
    // Profile operations
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfile)

    @Query("DELETE FROM user_profile")
    suspend fun clearProfile()

    // Document operations
    @Query("SELECT * FROM user_documents")
    fun getAllDocuments(): Flow<List<UserDocument>>

    @Query("SELECT * FROM user_documents WHERE docType = :docType")
    suspend fun getDocumentByType(docType: String): UserDocument?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDocument(document: UserDocument)

    @Query("DELETE FROM user_documents WHERE docType = :docType")
    suspend fun deleteDocument(docType: String)
}
