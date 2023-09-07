package org.example.untils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormat {
    public static String covertPriceToString(double price) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(price);
    }
    public static double parseDouble(String price) {
        String priceNew = price.replaceAll("\\D+", "");
        return Double.parseDouble(priceNew);
    }
    public static int parseInteger(double price) {
        String price1 = String.valueOf(price);
        String priceNew = price1.replaceAll("\\D+\\d", "");
        return Integer.parseInt(priceNew);
    }
}
