package org.example.untils;

import org.example.models.Bank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validex {
    public static boolean validatePhoneNumber(String phoneNumber) {
        String regex = "^\\d{2}-0\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }
    public static boolean validateAndReturnEmail(String input) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static boolean isValidBankCard(String cardNumber) {
        String regex = "\\d{4}-\\d{4}-\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cardNumber);
        return matcher.matches();
    }
}
