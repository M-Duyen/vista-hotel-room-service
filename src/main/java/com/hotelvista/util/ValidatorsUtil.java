package com.hotelvista.util;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
public class ValidatorsUtil {
    public static String validateRoomNumber(String roomNumber) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return "Room number is required";
        }
        String trimmed = roomNumber.trim();
        if (trimmed.length() > 10) {
            return "Room number must not exceed 10 characters";
        }
        if (!trimmed.matches("^[A-Za-z0-9-]+$")) {
            return "Room number can only contain letters, numbers, and dashes";
        }
        return null;
    }
    public static String validateFloor(Integer floor) {
        if (floor == null) {
            return "Floor is required";
        }
        if (floor < 1) {
            return "Floor must be greater than 0";
        }
        if (floor > 100) {
            return "Floor must not exceed 100";
        }
        return null;
    }
    public static String validateRoomTypeId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "Room type ID is required";
        }
        return null;
    }
    public static String validateRoomTypeName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Room type name is required";
        }
        return null;
    }
    public static String validateCapacity(Integer capacity) {
        if (capacity == null) {
            return "Capacity is required";
        }
        if (capacity < 1) {
            return "Capacity must be at least 1";
        }
        return null;
    }
    public static String validateRoomPrice(Double price) {
        if (price == null) {
            return "Price is required";
        }
        if (price < 0) {
            return "Price must be greater than or equal to 0";
        }
        return null;
    }
    public static String validateRoomSize(Double size) {
        if (size == null) {
            return "Room size is required";
        }
        if (size < 0) {
            return "Room size must be greater than or equal to 0";
        }
        return null;
    }

    public static String validatePromotionId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return "Promotion ID is required";
        }
        return null;
    }

    public static String validatePromotionName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Promotion name is required";
        }
        return null;
    }

    public static String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return "Description is required";
        }
        return null;
    }

    public static String validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return fieldName + " is required";
        }
        return null;
    }
    public static String validateDiscountPercentage(Double percentage) {
        if (percentage == null) {
            return "Discount percentage is required";
        }
        if (percentage <= 0) {
            return "Discount percentage must be greater than 0";
        }
        return null;
    }
    public static String validateStartDate(LocalDate startDate) {
        if (startDate == null) {
            return "Start date is required";
        }
        if (startDate.isBefore(LocalDate.now().minusDays(1))) {
            return "Start date cannot be in the past";
        }
        return null;
    }
    public static String validateEndDate(LocalDate endDate) {
        if (endDate == null) {
            return "End date is required";
        }
        return null;
    }
    public static String validatePromotionDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            return "Start date is required";
        }
        if (endDate == null) {
            return "End date is required";
        }
        if (!endDate.isAfter(startDate)) {
            return "End date must be after start date";
        }
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 365) {
            return "Promotion duration cannot exceed 1 year";
        }
        return null;
    }
}
