package vu.oop.passwordmanager.service;

import java.security.SecureRandom;
import java.util.Random;
/**
 * RandomPasswordGenerator class provides methods to generate random passwords
 * based on specified criteria such as length and character types.
 * 
 * @author Dovydas Kelečius
 * Contact:
 * @version 1.0
 * @since 2025-06-01
 * This class is part of the vu.oop.passwordmanager.service package,
 */
public class RandomPasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase();
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()_+{}[]";
    private Boolean includeUpperCase = true;
    private Boolean includeLowerCase = true;
    private Boolean includeDigits = true;
    private Boolean includeSpecialChars = true;
    private Boolean includeAllChars = true;

    /**
     * It uses SecureRandom for better security in password generation.
     */
    private final Random random;

    /**
     * Constructs a RandomPasswordGenerator instance.
     * Initializes the random number generator using SecureRandom.
     */
    public RandomPasswordGenerator() {
        this.random = new SecureRandom();

    }

    /**
     * Generates a random password based on the specified criteria.
     * 
     * @param length The desired length of the password.
     * @param includeUpperCase Whether to include uppercase letters.
     * @param includeLowerCase Whether to include lowercase letters.
     * @param includeDigits Whether to include digits.
     * @param includeSpecialChars Whether to include special characters.
     * @return A randomly generated password as a String.
     * @throws IllegalArgumentException If the length is less than 1 or no character types are selected.
     */
    public String generate(int length,
                           boolean includeUpperCase,
                           boolean includeLowerCase,
                           boolean includeDigits,
                           boolean includeSpecialChars) {
        if (length < 1) {
            throw new IllegalArgumentException("Password length must be at least 1");
        }

        // Build the character set based on the flags
        StringBuilder charSet = new StringBuilder();

        if (includeUpperCase)           charSet.append(UPPER);
        if (includeLowerCase)           charSet.append(LOWER);
        if (includeDigits)              charSet.append(DIGITS);
        if (includeSpecialChars)        charSet.append(SPECIAL_CHARS);
        if (charSet.length() == 0)      throw new IllegalArgumentException("At least one character type must be selected");

        // Generate the password
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charSet.length());
            password.append(charSet.charAt(index));
        }
        return password.toString();
    }
}