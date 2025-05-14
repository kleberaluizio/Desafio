package com.luizalabs.api.txt.purchase.service.parameters;

public class InputErrors {
    private int currentLine;
    private StringBuilder errors;

    public InputErrors() {}

    public int getCurrentLine() {
        return currentLine;
    }

    public StringBuilder getErrors() {
        return errors;
    }

    public void incrementCurrentLine() {
        this.currentLine++;
    }
}
