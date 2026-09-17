package com.example;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Defensive utility class providing bulletproof input validation,
 * stream sanitation, and formatting routines.
 */
public final class ValidationUtils {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{7,15}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private ValidationUtils() {
    }

    /**
     * Safely reads a line from Scanner without throwing NoSuchElementException on EOF.
     */
    public static String readLineOrNull(Scanner scanner) {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
        return null;
    }

    public static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Error: Input cannot be blank. Please try again.");
        }
    }

    public static Integer readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            try {
                int val = Integer.parseInt(line.trim());
                if (val > 0) {
                    return val;
                }
                System.out.println("Error: Value must be a positive integer (> 0).");
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid whole number.");
            }
        }
    }

    public static Double readDoubleInRange(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            try {
                double val = Double.parseDouble(line.trim());
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("Error: Value must be between %.2f and %.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input. Please enter a valid decimal number.");
            }
        }
    }

    public static String readEmail(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (EMAIL_PATTERN.matcher(line).matches()) {
                return line;
            }
            System.out.println("Error: Invalid email format (e.g. name@university.edu).");
        }
    }

    public static String readPhone(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) return null;
            line = line.trim();
            if (PHONE_PATTERN.matcher(line).matches()) {
                return line;
            }
            System.out.println("Error: Invalid phone number format (7-15 digits, optional '+').");
        }
    }

    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }
}
