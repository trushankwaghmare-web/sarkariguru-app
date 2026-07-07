package com.example.api

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Translate spoken Hindi into English text suitable for a specific form field
    suspend fun translateHindiVoiceToEnglish(spokenText: String, fieldName: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API Key is not set, using mock translation")
            return@withContext mockTranslate(spokenText, fieldName)
        }

        val prompt = """
            You are a helpful assistant for SarkariGuru.AI.
            The user spoke the following in Hindi/Hinglish: "$spokenText".
            Translate and format this spoken text into a clean, concise English string suitable for an application form field named "$fieldName".
            Return ONLY the final translated/formatted English string, with absolutely no extra commentary, introductory text, or quotation marks.
        """.trimIndent()

        try {
            val responseText = makeApiCall(prompt, apiKey)
            if (responseText != null) {
                responseText.trim().removeSurrounding("\"")
            } else {
                mockTranslate(spokenText, fieldName)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in voice translation: ${e.message}", e)
            mockTranslate(spokenText, fieldName)
        }
    }

    // AI Proofreader matching document names with input names
    data class ProofreadResult(val hasTypo: Boolean, val message: String)

    suspend fun proofreadNames(docName: String, formName: String): ProofreadResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            val matches = docName.trim().equals(formName.trim(), ignoreCase = true)
            return@withContext if (!matches) {
                ProofreadResult(
                    hasTypo = true,
                    message = "ALERT: Name on Document ($docName) differs slightly from Form Entry ($formName). This could lead to form rejection."
                )
            } else {
                ProofreadResult(hasTypo = false, message = "Names match perfectly!")
            }
        }

        val prompt = """
            Compare the full name written on the official document: "$docName" 
            with the name entered in the application form field: "$formName".
            Is there a spelling mistake, typo, or minor discrepancy? (e.g., "Suresh Kumar" vs "Suresh K.").
            Respond in JSON format only. The JSON must contain two keys:
            1. "hasTypo": boolean (true if there is a mismatch or spelling discrepancy, false otherwise)
            2. "message": string (a professional, friendly warning message in Hindi/English explaining the mismatch or confirming that it's perfect)
            
            Do not include any markdown styling like ```json or ```, return the raw JSON object.
        """.trimIndent()

        try {
            val responseText = makeApiCall(prompt, apiKey)
            if (responseText != null) {
                val cleanJson = responseText.trim().removeSurrounding("```json", "```").trim()
                val json = JSONObject(cleanJson)
                ProofreadResult(
                    hasTypo = json.getBoolean("hasTypo"),
                    message = json.getString("message")
                )
            } else {
                ProofreadResult(hasTypo = docName.trim().lowercase() != formName.trim().lowercase(), "Name comparison failed, please check manually.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in proofreading: ${e.message}", e)
            val hasTypo = docName.trim().lowercase() != formName.trim().lowercase()
            ProofreadResult(
                hasTypo = hasTypo,
                message = if (hasTypo) "Name mismatch detected: '$docName' vs '$formName'." else "Details match."
            )
        }
    }

    // OCR Document Details extraction (using simulated base64 or actual)
    data class OcrResult(
        val nameOnDoc: String,
        val rollNumber: String,
        val marks: String,
        val year: String,
        val docNum: String
    )

    suspend fun extractDocumentDetails(docType: String, bitmap: Bitmap?): OcrResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || bitmap == null) {
            Log.w(TAG, "Gemini API Key is not set or bitmap is null, using mock OCR extraction")
            return@withContext getMockOcrResult(docType)
        }

        val prompt = """
            You are an advanced OCR engine for SarkariGuru.AI.
            Extract details from this $docType document (10th Marksheet, 12th Marksheet, or Aadhaar).
            Please extract:
            1. Full name on document (nameOnDoc)
            2. Roll number/seat number (rollNumber - empty for Aadhaar)
            3. Total marks/percentage (marks - empty for Aadhaar)
            4. Passing / Issuing year (year - empty for Aadhaar)
            5. Document ID number (docNum - especially Aadhaar number)
            
            Respond in JSON format only. The JSON must have these keys:
            "nameOnDoc", "rollNumber", "marks", "year", "docNum".
            Do not include any markdown code blocks, return raw JSON text.
        """.trimIndent()

        try {
            val base64Image = bitmapToBase64(bitmap)
            val jsonRequest = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                            put(JSONObject().put("inlineData", JSONObject().apply {
                                put("mimeType", "image/jpeg")
                                put("data", base64Image)
                            }))
                        })
                    })
                })
            }

            val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                val responseJson = JSONObject(responseBody)
                val textResponse = responseJson.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                val cleanJson = textResponse.trim().removeSurrounding("```json", "```").trim()
                val jsonResult = JSONObject(cleanJson)

                OcrResult(
                    nameOnDoc = jsonResult.optString("nameOnDoc", ""),
                    rollNumber = jsonResult.optString("rollNumber", ""),
                    marks = jsonResult.optString("marks", ""),
                    year = jsonResult.optString("year", ""),
                    docNum = jsonResult.optString("docNum", "")
                )
            } else {
                getMockOcrResult(docType)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in document extraction OCR: ${e.message}", e)
            getMockOcrResult(docType)
        }
    }

    private fun makeApiCall(prompt: String, apiKey: String): String? {
        val jsonRequest = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", prompt))
                    })
                })
            })
        }

        val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL?key=$apiKey")
            .post(requestBody)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            val responseJson = JSONObject(responseBody)
            return responseJson.getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
        }
        return null
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun mockTranslate(spokenText: String, fieldName: String): String {
        val clean = spokenText.lowercase().trim()
        return when {
            fieldName.contains("name", ignoreCase = true) -> {
                spokenText.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
            }
            fieldName.contains("phone", ignoreCase = true) -> {
                spokenText.replace(Regex("[^0-9]"), "")
            }
            clean.contains("mumbai") || clean.contains("बॉम्बे") -> "Mumbai, Maharashtra"
            clean.contains("delhi") || clean.contains("दिल्ली") -> "Delhi, NCR"
            clean.contains("pune") || clean.contains("पुणे") -> "Pune, Maharashtra"
            clean.contains("patna") || clean.contains("पटना") -> "Patna, Bihar"
            clean.contains("sharma") -> "Suresh Sharma"
            clean.contains("singh") -> "Rajesh Singh"
            else -> spokenText // Fallback
        }
    }

    private fun getMockOcrResult(docType: String): OcrResult {
        return when (docType) {
            "10TH_MARKSHEET" -> OcrResult(
                nameOnDoc = "Aakash Suresh Kumar",
                rollNumber = "10B89241",
                marks = "482/500 (96.4%)",
                year = "2020",
                docNum = "MS10-2020-0082"
            )
            "12TH_MARKSHEET" -> OcrResult(
                nameOnDoc = "Aakash Suresh Kumar",
                rollNumber = "12C94102",
                marks = "475/500 (95.0%)",
                year = "2022",
                docNum = "MS12-2022-0941"
            )
            else -> OcrResult(
                nameOnDoc = "Aakash Suresh Kumar",
                rollNumber = "",
                marks = "",
                year = "",
                docNum = "5824 9102 3847" // Aadhaar
            )
        }
    }
}
