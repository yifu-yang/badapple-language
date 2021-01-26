package org.blackjack;

public enum Token{
    //public static final TOKEN_EOF         = iota;  // end-of-file
    TOKEN_VAR_PREFIX("$"),
    TOKEN_LEFT_PAREN("("),
    TOKEN_RIGHT_PAREN(")"),
    TOKEN_EQUAL("="),
    TOKEN_QUOTE("\""),
    TOKEN_DUOQUOTE("\"\""),
    TOKEN_NAME("[_A-Za-z][_0-9A-Za-z]*"),
    TOKEN_PRINT("print");

    private String tokenValue;

    private Token(String token) {
        this.tokenValue = token;
    }

    public String getTokenValue(){
        return tokenValue;
    }
}