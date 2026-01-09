package com.cnl.istudy_sts.common.managers

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class SecureProtocolManager(
//    private val userRepository: UserRepository,
//    private val profileRepository: UserProfileRepository,
//    private val grantRepository: AccessGrantRepository
) {

    /**
     * 1. ЗБЕРЕЖЕННЯ ПРОФІЛЮ (СЕЙФ)
     * Ми не шифруємо тут, але ми перевіряємо, чи клієнт надіслав коректний формат.
     */
    @Transactional
    fun saveEncryptedProfile(/*user: User, */ encryptedPayload: String, iv: String) {
        // Валідація: перевіряємо, чи це справді Base64, а не SQL-ін'єкція чи пустишка
        validateBase64(encryptedPayload)
        validateBase64(iv)

//        val profile = profileRepository.findByUserId(user.id!!)
//            ?: UserProfile(user = user, encryptedData = "", iv = "")
//
//        // Оновлюємо "сейф"
//        profile.encryptedData = encryptedPayload
//        profile.iv = iv
//
//        profileRepository.save(profile)
    }

    /**
     * 2. ОБМІН КЛЮЧАМИ (HANDSHAKE)
     * Учень (owner) передає ключ Репетитору (reader).
     */
    @Transactional
    fun grantAccessKey(/*owner: User, */ readerId: Long, encryptedDek: String) {
//        val reader = userRepository.findById(readerId)
//            .orElseThrow { RuntimeException("User not found") }
//
//        // Валідація: чи це валідний зашифрований ключ?
//        validateBase64(encryptedDek)
//
//        // Перевіряємо, чи не дублюємо доступ
//        val existingGrant = grantRepository.findByOwnerIdAndReaderId(owner.id!!, readerId)
//        if (existingGrant != null) {
//            // Оновлюємо ключ (якщо учень змінив дані і перешифрував їх)
//            existingGrant.encryptedDek = encryptedDek
//            grantRepository.save(existingGrant)
//        } else {
//            // Створюємо новий доступ
//            val grant = AccessGrant(
//                owner = owner,
//                reader = reader,
//                encryptedDek = encryptedDek
//            )
//            grantRepository.save(grant)
//        }
    }

    /**
     * 3. ОТРИМАННЯ "ПАКЕТУ" ДЛЯ РОЗШИФРОВКИ
     * Збираємо докупи всі пазли, щоб репетитор міг відкрити сейф.
     */
    @Transactional(readOnly = true)
    fun buildDecryptionContext(/* reader: User, */ targetStudentId: Long): DecryptionContextDto {
//        // а) Беремо сейф
//        val profile = profileRepository.findByUserId(targetStudentId)
//            ?: throw RuntimeException("Profile not filled yet")
//
//        // б) Беремо ключ від сейфа, призначений для цього репетитора
//        val grant = grantRepository.findByOwnerIdAndReaderId(targetStudentId, reader.id!!)
//            ?: throw RuntimeException("Access Denied: You don't have the key")
//
//        // в) Віддаємо "набір юного хакера" репетитору
//        return DecryptionContextDto(
//            encryptedPayload = profile.encryptedData,
//            iv = profile.iv,
//            encryptedKeyForReader = grant.encryptedDek
//        )

        return DecryptionContextDto(
            encryptedPayload = "",
            iv = "",
            encryptedKeyForReader = ""
        )
    }

    // --- Helper Validation ---
    private fun validateBase64(value: String) {
        try {
            Base64.getDecoder().decode(value)
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Security Violation: Invalid Base64 data received")
        }
    }
}

// DTO для зручної передачі на фронт
data class DecryptionContextDto(
    val encryptedPayload: String, // Дані учня (AES)
    val iv: String,               // Вектор
    val encryptedKeyForReader: String // Ключ (RSA), який репетитор відкриє своїм приватним ключем
)