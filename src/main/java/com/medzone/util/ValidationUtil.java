package com.medzone.util;

import jakarta.servlet.http.Part;

public class ValidationUtil {
	
	private static final String NAME_REGEX = "^[a-zA-Z]+[ a-zA-Z]*$";
	private static final String USERNAME_REGEX = "^[a-zA-Z][a-zA-Z\\d_.]*$";
	private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[\\d])[A-Za-z\\d!@#$%^&*()]{8,}$";
	private static final String PHONE_REGEX = "^(98|97)\\d{8}$";
	private static final String EMAIL_REGEX = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
	
	// Validation for empty or null text fields.
	public static boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}
	
	// Validation for first name and last name with pattern.
	public static boolean validateName(String value) {
		return value.matches(NAME_REGEX);
	}
	
	// Validation for username with pattern.
	public static boolean validateUserName(String value) {
		return value.matches(USERNAME_REGEX);
	}
	
	// Validation for phone number with pattern.
	public static boolean validatePhoneNum(String value) {
		return value.matches(PHONE_REGEX);
	}
	
	// Validation for password with pattern.
	public static boolean validatePassword(String value) {
		return value.matches(PASSWORD_REGEX);
	}
	
	// Validation for email with pattern.
	public static boolean validateEmail(String value) {
		return value.matches(EMAIL_REGEX);
	}
	
	// Validation for matching passwords while registering and changing passwords.
	public static boolean matchPasswords(String pass, String cpass) {
		return pass.equals(cpass);
	}
	
	// Validate if a Part's file extension matches with image extensions (jpg, jpeg, png, gif)
    public static boolean isValidImageExtension(Part imagePart) {
        if (imagePart == null || isNullOrEmpty(imagePart.getSubmittedFileName())) {
            return false;
        }
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }
}
