package com.example.application.xxx.enums;

//Create a new enum in your project, e.g., in the package com.example.application.entity


//public enum TaskStatus {
// NOT_STARTED,
// IN_PROGRESS,
// COMPLETED,
// ON_HOLD,
// CANCELLED,
// NOT_COMPLETED
//}

public enum TaskStatus {
    NOT_STARTED("#FF9800"), // Orange
    IN_PROGRESS("#2196F3"), // Blue
    COMPLETED("#4CAF50"),   // Green
    ON_HOLD("#F44336"), // Red
    NOT_COMPLETED("#F44336"), // Red
    CANCELLED("#F44336"); // Red

    private final String colorCode;

    TaskStatus(String colorCode) {
        this.colorCode = colorCode;
    }

    public String color() {
        return colorCode;
    }
}
