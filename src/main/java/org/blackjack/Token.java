package org.blackjack;

public enum Token{
    TOKEN_EOF("EOF"),
    TOKEN_VAR_PREFIX("$"),
    TOKEN_LEFT_PAREN("("),
    TOKEN_RIGHT_PAREN(")"),
    TOKEN_EQUAL("="),
    TOKEN_QUOTE("\""),
    TOKEN_DUOQUOTE("\"\""),
    TOKEN_NAME("[_A-Za-z][_0-9A-Za-z]*"),
    TOKEN_PRINT("print"),
    TOKEN_IGNORED("Ignored");


    private String tokenValue;

    private String value;
    private int lineNumber;

    private Token(String token) {
        this.tokenValue = token;
    }

    public String getTokenValue(){
        return tokenValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}