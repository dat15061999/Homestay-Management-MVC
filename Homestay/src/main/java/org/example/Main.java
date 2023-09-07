package org.example;
import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();

        String defaultFormattedDateTime = now.toString();

        System.out.println("Default Formatted LocalDateTime: " + defaultFormattedDateTime);
        try {
            int x = 0;
            int y = 5 / x;
        }catch (ArithmeticException e) {
            System.out.println("Arithmetic");
        }catch (Exception ae) {
            System.out.println("Exception");
        }
        System.out.println("finished");

    }
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        for (Character i : s.toCharArray()) {
            if (i == '(' || i == '{' || i == '[') {
                stack.push(i);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if ((top == '(' && i != ')') || (top == '{' && i != '}') || (top == '[' && i != ']')) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }


}