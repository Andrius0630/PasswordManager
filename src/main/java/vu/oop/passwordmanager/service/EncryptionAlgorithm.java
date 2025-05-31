package vu.oop.passwordmanager.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * EncryptionAlgorithm class provides methods for encrypting and decrypting data
 * using AES encryption with a master key.
 * 
 * @author Dovydas Kelečius
 * * Contact: kelecius.dovydas@gmail.com
 * @version 1.1
 * @since 2025-05-31
 * This class is part of the vu.oop.passwordmanager.service package,
 * which handles encryption-related functionalities for the password manager application.
 */
public class EncryptionAlgorithm {

    private SecretKey masterKey; // Example master key, should be securely stored

    public EncryptionAlgorithm(String masterKeyString) throws Exception {
        // Generate the key once when the object is created
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, new java.security.SecureRandom()); // For AES-256
        this.masterKey = encryptKey(masterKeyString);
    }

    public SecretKey getMasterKey() {
        return this.masterKey;
    }

    public void setMasterKey(SecretKey masterKey) {
        this.masterKey = masterKey;
    }

    /**
     * Encrypts the master key string to create a SecretKeySpec for AES encryption.
     * @param masterKeyString The master key string to encrypt.
     * @return The SecretKeySpec representing the encrypted master key.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available.
     */
    public SecretKeySpec encryptKey(String masterKeyString) throws NoSuchAlgorithmException {
            byte[] keyBytes = masterKeyString.getBytes(StandardCharsets.UTF_8);
            // Hash the key to ensure it's 256 bits (32 bytes) for AES-256
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);
            SecretKeySpec customMasterKey = new SecretKeySpec(keyBytes, "AES");
            return customMasterKey;
    }

    /**
     * Converts the SecretKey to a Base64 encoded String.
     * @param key The SecretKey to convert.
     * @return The Base64 encoded String representation of the key.
     * @throws Exception If the key is null or empty.
     */
    public String getSecretKeyString(SecretKey key) throws Exception {
        if (key.getEncoded() == null || key.getEncoded().length == 0) {
            throw new IllegalArgumentException("Master key must not be null or empty");
        }
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Encrypts the given data using the stored master key.
     * @param data The data to encrypt.
     * @param key The master key used for encryption.
     * @return The encrypted data as a Base64 encoded String.
     * @throws Exception If encryption fails.
     */
    public String encrypt(String data) throws Exception {
        String transformation = "AES/ECB/PKCS5Padding";

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, masterKey); // Use the stored key

        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);

        System.out.println("[CRYPT] Encrypted data.");
        return encryptedBase64;
    }

    /**
     * Decrypts the given encrypted data using the stored master key.
     * * @param encryptedData The encrypted data to decrypt.
     * * @param key The master key used for decryption.
     * * @return The decrypted data as a String.
     * * @throws Exception If decryption fails.
     */
    public String decrypt(String encryptedData) throws Exception {
        String transformation = "AES/ECB/PKCS5Padding";

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, masterKey); // Use the stored key

        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        System.out.println("[CRYPT] Decrypted data.");
        return decryptedText;
    }
}