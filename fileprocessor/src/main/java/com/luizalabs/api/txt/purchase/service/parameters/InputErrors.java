package com.luizalabs.api.txt.purchase.service.parameters;

public class InputErrors {
    private int currentLine = 1;
    private final StringBuilder message = new StringBuilder();
    private final String fileName;

    public InputErrors(String fileName) {
        this.fileName = fileName;
    }

    public String getErrorDescription() {
        message.deleteCharAt(message.length() - 1); // Remove last comma
        message.append(".");
        return message.toString();
    }

    public boolean isEmpty() {
        return message.isEmpty();
    }

    public void addCurrentLineToErrorListAndIncrement() {
        if (message.isEmpty()) {
            message.append("The following lines in the file (").append(fileName).append(") are not in the correct format:");
        }
        message.append(" ").append(currentLine).append(",");
        this.currentLine++;
    }

    public void incrementCurrentLine() {
        this.currentLine++;
    }
}
