package org.blackjack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.blackjack.Token.TOKEN_PRINT;

public class Parser {

    private Lexer lexer;


    public Lexer getLexer() {
        return lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public String parsePrint() throws Exception {
        lexer.NextTokenIs(TOKEN_PRINT);
        lexer.NextTokenIs(Token.TOKEN_LEFT_PAREN);
        parseVariable();
        lexer.NextTokenIs(Token.TOKEN_RIGHT_PAREN);
        return null;
    }

    public String parseStatement() throws Exception {
        switch (lexer.LookAhead()) {
            case TOKEN_PRINT:
                return parsePrint();
            case TOKEN_VAR_PREFIX:
                return parseAssignment();
            default:
                throw new Exception("unknown token");
        }
    }

    public String parseVariable() {
        return null;
    }

    public String parseAssignment() {
        return null;
    }
}
