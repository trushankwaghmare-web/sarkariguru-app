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
    private val MODELS = listOf("gemini-2.5-flash", "gemini-1.5-flash", "gemini-2.0-flash")

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
            
            var textResponse: String? = null
            for (model in MODELS) {
                try {
                    val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
                    val request = Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build()

                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string() ?: ""
                        val responseJson = JSONObject(responseBody)
                        textResponse = responseJson.getJSONArray("candidates")
                            .getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")
                        break
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "OCR model fallback for $model: ${e.message}")
                }
            }

            if (textResponse != null) {
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

        for (model in MODELS) {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
                val request = Request.Builder()
                    .url(url)
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
            } catch (e: Exception) {
                Log.w(TAG, "API call fallback for $model: ${e.message}")
            }
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

    suspend fun generateHindiApplyGuide(
        jobTitle: String,
        jobOfficialLink: String,
        eligibility: String,
        fees: String,
        userName: String,
        userDob: String,
        userQualification: String,
        userCategory: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API Key is not set, using mock Hindi apply guide")
            return@withContext getMockHindiApplyGuide(jobTitle, jobOfficialLink, eligibility, fees, userName, userDob, userQualification, userCategory)
        }

        val prompt = """
            You are an expert AI application assistant for SarkariGuru.AI.
            Generate a highly detailed, step-by-step application guide in simple Hindi language for applying to the following government job:
            Job Title: "$jobTitle"
            Official Application Link: "$jobOfficialLink"
            Job Eligibility: "$eligibility"
            Job Fees: "$fees"

            The candidate applying has the following profile details:
            Name (Naam): "$userName"
            Date of Birth (DOB): "$userDob"
            Qualification: "$userQualification"
            Category: "$userCategory"

            Your guide MUST be in simple, friendly, easy-to-understand Hindi (Devanagari script) and structured EXACTLY using these sections separated by empty lines:

            SECTION 1: CANDIDATE PROFILE READ CONFIRMATION (उम्मीदवार प्रोफ़ाइल पुष्टि)
            Confirm you have read their saved details (Naam, DOB, Qualification, Category). Give a warm 2-3 sentence greeting in Hindi.

            SECTION 2: STEP-BY-STEP OFFICIAL WEBSITE GUIDE (स्टेप-बाय-स्टेप आवेदन गाइड)
            Give 4 to 6 clear, sequential, numbered steps to apply on the official website ($jobOfficialLink).
            At EACH step, clearly instruct the user what they need to enter, explicitly substituting the candidate's actual saved details in the instruction. For example:
            - "कदम 1: सबसे पहले अधिकारिक वेबसाइट ($jobOfficialLink) खोलें और 'New Registration' पर क्लिक करें।"
            - "कदम 2: अब फॉर्म में अपना नाम '$userName' दर्ज करें। ध्यान रहे कि वर्तनी बिल्कुल आपके दस्तावेजों जैसी होनी चाहिए।"
            - "कदम 3: अपनी जन्मतिथि (DOB) '$userDob' भरें।"
            - "कदम 4: श्रेणी (Category) विकल्प में से '$userCategory' चुनें।"
            - "कदम 5: अपनी शैक्षणिक योग्यता '$userQualification' सिलेक्ट करें।"

            SECTION 3: SCANNED DOCUMENTS CHECKLIST (आवश्यक दस्तावेज जो स्कैन करके अपलोड करने हैं)
            Provide a detailed bulleted list in Hindi of documents they must scan and upload, including specific ones based on their eligibility and category (e.g. 10th marksheet for "$userQualification", Category Certificate for "$userCategory" if SC/ST/OBC, passport photo, signature, etc.).

            SECTION 4: FEE PAYMENT METHOD (आवेदन शुल्क भुगतान निर्देश)
            Explain how to pay the fee. Explicitly state the fee amount from "$fees" that applies to their category "$userCategory", and explain simple online payment options (UPI, Netbanking, Debit Card).

            SECTION 5: FINAL CHECKLIST BEFORE SUBMISSION (जमा करने से पहले अंतिम चेकलिस्ट)
            Provide a bulleted list of things they must verify before clicking the 'Submit' button.

            Keep the tone encouraging, and use simple language. Keep formatting clean.
        """.trimIndent()

        try {
            val responseText = makeApiCall(prompt, apiKey)
            if (responseText != null) {
                responseText.trim()
            } else {
                getMockHindiApplyGuide(jobTitle, jobOfficialLink, eligibility, fees, userName, userDob, userQualification, userCategory)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in generating apply guide: ${e.message}", e)
            getMockHindiApplyGuide(jobTitle, jobOfficialLink, eligibility, fees, userName, userDob, userQualification, userCategory)
        }
    }

    private fun getMockHindiApplyGuide(
        jobTitle: String,
        jobOfficialLink: String,
        eligibility: String,
        fees: String,
        userName: String,
        userDob: String,
        userQualification: String,
        userCategory: String
    ): String {
        return """
**उम्मीदवार प्रोफ़ाइल पुष्टि (Candidate Profile Read Confirmation)**
नमस्ते **$userName**, हमने आपकी प्रोफ़ाइल डिटेल्स सफलतापूर्वक पढ़ ली हैं:
* **नाम**: $userName
* **जन्मतिथि (DOB)**: $userDob
* **योग्यता**: $userQualification
* **श्रेणी (Category)**: $userCategory

---

**स्टेप-बाय-स्टेप अधिकारिक वेबसाइट गाइड (Step-by-Step Guide for Official Website)**
अधिकारिक वेबसाइट पर आवेदन करने के लिए इन सरल चरणों का पालन करें:
* **कदम 1:** सबसे पहले अधिकारिक लिंक ($jobOfficialLink) पर जाएं।
* **कदम 2:** होमपेज पर 'Apply Online' या 'New Registration' लिंक खोजें और उसपर क्लिक करें।
* **कदम 3:** पंजीकरण फॉर्म में अपना नाम **"$userName"** और अपनी जन्मतिथि **"$userDob"** बिल्कुल सही दर्ज करें।
* **कदम 4:** श्रेणी (Category) सेक्शन में जाकर **"$userCategory"** का चयन करें।
* **कदम 5:** शैक्षणिक योग्यता सेक्शन में **"$userQualification"** का चयन करें और यदि पूछा जाए तो अपने 10वीं/12वीं के रोल नंबर और अंक भरें।
* **कदम 6:** फॉर्म को सेव करें और आगे बढ़ें।

---

**आवश्यक दस्तावेज जो स्कैन करके अपलोड करने हैं (Scanned Documents List)**
कृपया सुनिश्चित करें कि निम्नलिखित दस्तावेज आपके पास स्कैन किए हुए तैयार हैं:
* 📸 **पासपोर्ट साइज फोटो**: (जेपीईजी फॉर्मेट, साइज 20KB - 50KB के बीच)
* ✍️ **हस्ताक्षर (Signature)**: (साइज 10KB - 20KB के बीच, काले पेन से हस्ताक्षर करें)
* 📜 **$userQualification अंक पत्र (Marksheet)**: मूल दस्तावेज़ को साफ-साफ स्कैन करें।
* 🆔 **आधार कार्ड (Aadhaar Card)**: पहचान और पते के प्रमाण के रूप में।
${if (userCategory != "General") "* 📑 **जाति प्रमाण पत्र (Category Certificate)**: चूंकि आपकी श्रेणी **$userCategory** है, आपको आयु सीमा में छूट या शुल्क लाभ के लिए जाति प्रमाण पत्र अपलोड करना होगा।" else ""}

---

**आवेदन शुल्क भुगतान निर्देश (Fee Payment Instructions)**
* आपकी श्रेणी **"$userCategory"** के अनुसार, शुल्क विवरण इस प्रकार है: **$fees**।
* शुल्क का भुगतान ऑनलाइन माध्यम जैसे **UPI (GPay/PhonePe), डेबिट कार्ड, या नेट बैंकिंग** के जरिए सुरक्षित रूप से कर सकते हैं।
* भुगतान सफल होने के बाद स्क्रीनशॉट लें या रसीद डाउनलोड करना न भूलें।

---

**जमा करने से पहले अंतिम चेकलिस्ट (Final Submission Checklist)**
अंतिम सबमिट बटन दबाने से पहले इन बातों की जांच अवश्य करें:
* [ ] क्या आपका नाम **"$userName"** और जन्मतिथि **"$userDob"** आपकी मार्कशीट से मेल खाते हैं?
* [ ] क्या आपकी श्रेणी **"$userCategory"** सही चुनी गई है?
* [ ] क्या फोटो और हस्ताक्षर स्पष्ट रूप से दिखाई दे रहे हैं?
* [ ] क्या आपने सही शैक्षणिक विवरण दर्ज किया है?
* [ ] क्या आपने भरी हुई जानकारी का प्रीव्यू चेक कर लिया है?
        """.trimIndent()
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

    suspend fun chatWithSarkariGuru(
        userMessage: String,
        userName: String,
        userDob: String,
        userQualification: String,
        userCategory: String,
        isKidMode: Boolean,
        chatHistory: List<Pair<String, String>>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API Key is not set, using mock chat response")
            return@withContext getMockChatResponse(userMessage, userName, isKidMode)
        }

        val contextPrompt = if (isKidMode) {
            "You are a friendly, sweet, and adorable AI voice assistant named SarkariGuru.AI for kids and students. Speak in extremely simple Hindi, using cute words and emojis. The candidate's name is '$userName', DOB is '$userDob', qualification is '$userQualification', category is '$userCategory'."
        } else {
            "You are SarkariGuru.AI, a highly helpful, intelligent AI assistant for government jobs. Speak in friendly, clear Hindi (Devanagari script). The candidate's name is '$userName', DOB is '$userDob', qualification is '$userQualification', category is '$userCategory'."
        }

        val historyText = chatHistory.joinToString("\n") { (u, a) -> "User: $u\nSarkariGuru: $a" }

        val prompt = """
            $contextPrompt
            
            Previous Conversation:
            $historyText
            
            User's new message: "$userMessage"
            
            Provide a short, delightful, and natural reply in Hindi (1 to 3 sentences maximum, so it's easy to read and read aloud via text-to-speech). Use emojis! Focus on helping them understand jobs or encouraging them. Keep it simple.
        """.trimIndent()

        try {
            val responseText = makeApiCall(prompt, apiKey)
            responseText?.trim() ?: getMockChatResponse(userMessage, userName, isKidMode)
        } catch (e: Exception) {
            Log.e(TAG, "Error in chatbot conversation: ${e.message}", e)
            getMockChatResponse(userMessage, userName, isKidMode)
        }
    }

    data class RoadmapStep(val stepNum: Int, val title: String, val desc: String)

    suspend fun generateJobRoadmap(jobTitle: String, sector: String, qualification: String): List<RoadmapStep> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getMockRoadmap(jobTitle)
        }

        val prompt = """
            You are SarkariGuru.AI. Create a customized study and preparation roadmap of exactly 4 phases for the job: "$jobTitle" in sector: "$sector" for qualification: "$qualification".
            Return ONLY a valid JSON array of exactly 4 objects. No markdown wraps.
            Each object MUST have exactly these fields:
            - "stepNum": integer (1, 2, 3, or 4)
            - "title": string (Preparation phase name in Hindi/English, e.g. "Phase 1: Syllabus Aur Study Materials")
            - "desc": string (Detailed, actionable advice in Hindi containing syllabus topics, best books, test series strategy, and time allocation)
        """.trimIndent()

        try {
            val responseText = makeApiCall(prompt, apiKey)
            if (responseText != null) {
                val cleanJson = responseText.trim().removeSurrounding("```json", "```").trim()
                val array = JSONArray(cleanJson)
                val list = mutableListOf<RoadmapStep>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        RoadmapStep(
                            stepNum = obj.optInt("stepNum", i + 1),
                            title = obj.optString("title", "Phase ${i + 1}"),
                            desc = obj.optString("desc", "")
                        )
                    )
                }
                if (list.isNotEmpty()) return@withContext list
            }
            getMockRoadmap(jobTitle)
        } catch (e: Exception) {
            Log.e(TAG, "Error in roadmap generation: ${e.message}", e)
            getMockRoadmap(jobTitle)
        }
    }

    private fun getMockRoadmap(jobTitle: String): List<RoadmapStep> {
        return listOf(
            RoadmapStep(
                stepNum = 1,
                title = "Phase 1: सिलेबस और परीक्षा पैटर्न समझें",
                desc = "आधिकारिक परीक्षा पैटर्न और सिलेबस डाउनलोड करें। पिछले 5 वर्षों के कट-ऑफ अंकों का विश्लेषण करें और महत्वपूर्ण विषयों को चिह्नित करें।"
            ),
            RoadmapStep(
                stepNum = 2,
                title = "Phase 2: सर्वश्रेष्ठ पुस्तकों से बुनियादी तैयारी",
                desc = "गणित (RS Aggarwal), रीजनिंग (Arihant) और सामान्य ज्ञान (Lucent's GK) की मानक पुस्तकों से सभी अवधारणाओं को मजबूत करें।"
            ),
            RoadmapStep(
                stepNum = 3,
                title = "Phase 3: दैनिक मॉक टेस्ट और PYQ अभ्यास",
                desc = "रोजाना कम से कम 1 फुल-लेंथ ऑनलाइन मॉक टेस्ट दें। गलत हुए प्रश्नों का विश्लेषण करें और अपनी गति एवं सटीकता (Accuracy) बढ़ाएं।"
            ),
            RoadmapStep(
                stepNum = 4,
                title = "Phase 4: अंतिम पुनरीक्षण और फॉर्मूला रिवीजन",
                desc = "अंतिम 15 दिनों में नए विषय न पढ़ें। बनाए गए शॉर्ट नोट्स, करंट अफेयर्स और सूत्रों का बार-बार रिवीजन करें तथा आत्मविश्वास बनाए रखें।"
            )
        )
    }

    private fun getMockChatResponse(userMessage: String, userName: String, isKidMode: Boolean): String {
        val msg = userMessage.lowercase()
        return if (isKidMode) {
            when {
                msg.contains("hello") || msg.contains("हाय") || msg.contains("नमस्ते") -> {
                    "नमस्ते $userName बेटा! 🌸 आप कैसे हो? आज हम कौन सी प्यारी सरकारी नौकरी के बारे में जानेंगे? 🎖️✨"
                }
                msg.contains("job") || msg.contains("नौकरी") -> {
                    "बेटा, आपके लिए देश की सेवा करने के कई सुनहरे मौके हैं! जैसे भारतीय सेना में अफ़सर बनना या पुलिस में देश की रक्षा करना! 👮‍♂️🎖️"
                }
                msg.contains("qualification") || msg.contains("पढ़ाई") -> {
                    "आप मन लगाकर पढ़ाई करो बेटा! 10वीं या 12वीं पास करते ही आप बहुत सी सेना और पुलिस की नौकरियों के लिए फॉर्म भर सकते हो! 📚✏️"
                }
                else -> {
                    "अरे वाह! कितनी प्यारी बात कही आपने! 😍 सरकारी गुरु हमेशा आपके साथ है। कुछ और पूछना है बेटा? 💫"
                }
            }
        } else {
            when {
                msg.contains("hello") || msg.contains("नमस्ते") || msg.contains("hi") -> {
                    "नमस्कार $userName जी! SarkariGuru.AI में आपका स्वागत है। आज मैं आपकी सरकारी नौकरी की तैयारी और आवेदन में क्या सहायता कर सकता हूँ? 👮‍♂️"
                }
                msg.contains("job") || msg.contains("नौकरी") || msg.contains("vacancy") -> {
                    "आपके प्रोफाइल के आधार पर अभी कई लाइव वैकेंसियां उपलब्ध हैं! जैसे सेना रैली, नेवी टेक्निकल और दिल्ली पुलिस कांस्टेबल। क्या आप इनके बारे में विस्तार से जानना चाहते हैं? 📄"
                }
                else -> {
                    "बहुत बढ़िया सवाल! सरकारी गुरु एआई हमेशा आपकी मदद के लिए तैयार है। कृपया सरकारी नौकरी से जुड़ा अपना कोई भी सवाल पूछें। 🎯"
                }
            }
        }
    }
}
