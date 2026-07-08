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

@Entity(tableName = "user_documents", primaryKeys = ["userPhone", "docType"])
data class UserDocument(
    val userPhone: String,
    val docType: String, // "10TH_MARKSHEET", "12TH_MARKSHEET", "AADHAAR"
    val docName: String, // "10th Marksheet", etc.
    val rollNumber: String,
    val marks: String,
    val year: String,
    val nameOnDoc: String,
    val docNum: String, // Aadhaar number or other document identifier
    val scanTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey val phone: String,
    val email: String,
    val passwordHash: String,
    val name: String,
    val dob: String = "15/07/2002",
    val category: String = "General"
)

@Entity(tableName = "saved_jobs")
data class SavedJob(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phone: String, // associated user phone
    val jobTitle: String,
    val jobSector: String,
    val lastDate: String,
    val salary: String,
    val eligibility: String,
    val officialLink: String,
    val isApplied: Boolean = false,
    val applyDate: String = ""
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
    @Query("SELECT * FROM user_documents WHERE userPhone = :phone")
    fun getAllDocuments(phone: String): Flow<List<UserDocument>>

    @Query("SELECT * FROM user_documents WHERE userPhone = :phone AND docType = :docType")
    suspend fun getDocumentByType(phone: String, docType: String): UserDocument?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDocument(document: UserDocument)

    @Query("DELETE FROM user_documents WHERE userPhone = :phone AND docType = :docType")
    suspend fun deleteDocument(phone: String, docType: String)

    // User Accounts
    @Query("SELECT * FROM user_accounts WHERE phone = :phone")
    suspend fun getAccountByPhone(phone: String): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAccount(account: UserAccount)

    // Saved Jobs
    @Query("SELECT * FROM saved_jobs WHERE phone = :phone")
    fun getSavedJobs(phone: String): Flow<List<SavedJob>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(job: SavedJob)

    @Query("DELETE FROM saved_jobs WHERE phone = :phone AND jobTitle = :jobTitle")
    suspend fun deleteSavedJob(phone: String, jobTitle: String)
}
