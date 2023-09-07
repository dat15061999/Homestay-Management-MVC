package org.example.untils;


import org.example.models.EBank;
import org.example.models.EGender;
import org.example.models.ERole;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

public class GetValue {
    static Scanner input = new Scanner(System.in);

    public static String getString(String str) {
        try {
            System.out.println(str);
            String data = input.nextLine();
            if (data.equals("")) {
                throw new Exception();
            }
            return data;
        } catch (Exception e) {
            System.out.println("Empty data. Input again!");
            return getString(str);
        }
    }

    public static String getGender(String str) {
        try {
            System.out.println(str);
            System.out.println("1. Male");
            System.out.println("2. FeMale");
            System.out.println("3. Other");
            int choose = Integer.parseInt(getString("Enter your choice: "));
            if (choose < 0 || choose > 1000) {
                throw new NumberFormatException("Number invalid");
            }
            switch (choose) {
                case 1:
                    return EGender.MALE.getName();
                case 2:
                    return EGender.FEMALE.getName();
                case 3:
                    return EGender.OTHERS.getName();
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getGender(str);
        }

        return getGender(str);
    }

    public static String getRole(String str) {
        try {
            System.out.println(str);
            System.out.println("1. Client");
            System.out.println("2. Cashier");
            int choose = Integer.parseInt(getString("Enter your choice: "));
            if (choose < 0 || choose > 1000) {
                throw new NumberFormatException("Number invalid");
            }
            switch (choose) {
                case 1:
                    return ERole.CASHIER.getName();
                case 2:
                    return ERole.CLIENT.getName();

            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getRole(str);
        }
        return getRole(str);
    }

    public static String getBankName(String str) {
        try {
            System.out.println(str);
            System.out.println("1. MBBANKN");
            System.out.println("2. TECHCOMBANK");
            System.out.println("3. VIETCOMBANK");
            System.out.println("4. AGRIBANK");
            int choose = Integer.parseInt(getString("Enter your choice: "));
            if (choose < 0 || choose > 1000) {
                throw new NumberFormatException("Number invalid");
            }
            switch (choose) {
                case 1:
                    return EBank.MBBANK.getName();
                case 2:
                    return EBank.TECHCOMBANK.getName();
                case 3:
                    return EBank.VIETCOMBANK.getName();
                case 4:
                    return EBank.AGRIBANK.getName();
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getBankName(str);
        }
        return getBankName(str);
    }
    public static String getBankCard(String str) {
        while (true) {
            String cardBank = getString(str);
            try {
                if (Validex.isValidBankCard(cardBank)) {
                    return cardBank;
                } else {
                    throw new IllegalArgumentException("Invalid card number format");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid phone number format. Please enter a valid number with format: xxxx-xxxx-xxxx.");
            }
        }
    }

    public static String getStringPassCard(String str) {
        try {
            System.out.println(str);
            String data = String.valueOf(getInt(str));
            if (data.equals("")) {
                throw new Exception();
            }
            if (data.length() == 3) {
                return data;
            }
            else {
                return getStringPassCard(str);
            }

        } catch (Exception e) {
            System.out.println("Empty data. Please input a value have 3 characters.");
            return getStringPassCard(str);
        }
    }

    public static String getPhone(String prompt) {
        while (true) {
            String phoneNumberStr = getString(prompt);
            try {
                if (Validex.validatePhoneNumber(phoneNumberStr)) {
                    return phoneNumberStr;
                } else {
                    throw new IllegalArgumentException("Invalid phone number format");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid phone number format. Please enter a valid number with format: 84-0xxxxxxxxx.");
            }
        }
    }

    public static String getEmail(String str) {
        while (true) {
            String email = getString(str);
            try {
                if (Validex.validateAndReturnEmail(email)) {
                    return email;
                } else {
                    throw new IllegalArgumentException("Invalid phone number format");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid email format. Please enter a valid email with format: a-z@a-z.com");
            }
        }
    }

    public static int getInt(String str) {
        try {
            int number = Integer.parseInt(GetValue.getString(str));
            if (number < 0 || number > 100000000) {
                throw new NumberFormatException("Number invalid");
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getInt(str);
        }
    }
    public static int getIntWithAbout(String str,int max, int min) {
        try {
            int number = Integer.parseInt(GetValue.getString(str));
            if (number <= min || number >= max ) {
                throw new NumberFormatException("Giá trị nhập không phù hợp. Nhập giá trị giữa "+ min + " và " + max + " ." );
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getInt(str);
        }
    }

    public static float getFloat(String str) {
        try {
            float number = Float.parseFloat(GetValue.getString(str));
            if (number < 0 || number >= 100) {
                throw new NumberFormatException("Number invalid and to 1 from 100.");
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getFloat(str);
        }
    }

    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive number");
        }

        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static LocalDate chooseTime(String str) {
        try {
            System.out.println(str);
            System.out.println("1. Lấy ngày hiện tại");
            System.out.println("2. Tự nhập ngày");
            int choose = getInt("Enter your choice: ");
            if (choose < 0 || choose > 1000) {
                throw new NumberFormatException("Number invalid");
            }
            switch (choose) {
                case 1:
                    return getDateNow();
                case 2:
                    return getDate("Nhập ngày tháng năm bắt đầu.");
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return chooseTime(str);
        }
        return chooseTime(str);
    }

    public static LocalDate plusMonth(LocalDate localDate, long time) {
        return localDate.plusMonths(time);
    }

    public static LocalDate plusDay(LocalDate localDate, long time) {
        return localDate.plusDays(time);
    }

    public static String formatLocalDate(LocalDate localDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(dateTimeFormatter);
    }

    public static LocalDate getDateNow() {
        return LocalDate.now();
    }

    public static LocalDate getDate(String str) {
        LocalDate minDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            String time = GetValue.getString(str);
            LocalDate date = LocalDate.parse(time, formatter);
            if (date.isBefore(minDate)) {
                System.out.println("Date must not be earlier than " + minDate.format(formatter) + ". Please enter a valid date.");
                return getDate(str);
            }

            return date;
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter in dd-MM-yyyy format.");
            return getDate(str);
        }
    }
    public static LocalDate getDateOut(LocalDate dateIn,String str) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            String time = GetValue.getString(str);
            LocalDate date = LocalDate.parse(time, formatter);
            if (date.isBefore(dateIn) || date.isBefore(dateIn.plusDays(1))) {
                System.out.println("Check-out date must not be less than 1 day " + dateIn.format(formatter) + ". Please enter a valid date.");
                return getDateOut(dateIn,str);
            }

            return date;
        } catch (Exception e) {
            System.out.println("Invalid date format. Please enter in dd-MM-yyyy format.");
            return getDateOut(dateIn,str);
        }
    }


    public static long getLong(String s) {
        try {
            long number = Long.parseLong(GetValue.getString(s));
            if (number < 0 || number > 100000000) {
                throw new NumberFormatException("Number invalid");
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getLong(s);
        }
    }
    public static double getDouble(String s,double max, double min) {
        try {
            double number = Long.parseLong(GetValue.getString(s));
            if (number < 0 || number > 10) {
                throw new NumberFormatException("Number invalid. Valid to " + min + " from " + max);
            }
            return number;
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return getDouble(s,max,min);
        }
    }
}
