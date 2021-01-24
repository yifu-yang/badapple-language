package org.blackjack;

public class Parser {
    private Lexer lexer;
    private String sourceCode;
    private int lineNum;
    private String nextToken;
    private Token nextTokenType;
    private int nextTokenLineNum;

    public Lexer getLexer() {
        return lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getNextToken() {
        return nextToken;
    }

    public void setNextToken(String nextToken) {
        this.nextToken = nextToken;
    }

    public Token getNextTokenType() {
        return nextTokenType;
    }

    public void setNextTokenType(Token nextTokenType) {
        this.nextTokenType = nextTokenType;
    }

    public int getNextTokenLineNum() {
        return nextTokenLineNum;
    }

    public void setNextTokenLineNum(int nextTokenLineNum) {
        this.nextTokenLineNum = nextTokenLineNum;
    }

    public String parsePrint() {
        lexer.NextTokenIs(Token.TOKEN_PRINT);
        lexer.NextTokenIs(Token.TOKEN_LEFT_PAREN);
        parseVariable();
        lexer.NextTokenIs(Token.TOKEN_RIGHT_PAREN);
        return null;
    }

    public String parseVariable() {
        return null;
    }

    public String parseAssignment() {
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

    public Token matchNext() throws Exception {
        switch (sourceCode.charAt(0)){
            case '$':
                skipCode(1);
                return Token.TOKEN_VAR_PREFIX;
            case '(':
                skipCode(1);
                return Token.TOKEN_LEFT_PAREN;
            case ')':
                skipCode(1);
                return Token.TOKEN_RIGHT_PAREN;
            case '=':
                skipCode(1);
                return Token.TOKEN_EQUAL;
            case '"':
                if(nextSourceIs("\"\"")){
                    skipCode(2);
                    return Token.TOKEN_DUOQUOTE;
                }
                skipCode(1);
                return Token.TOKEN_QUOTE;
            default:
                throw new Exception("unexpected char");
        }
    }

    private Boolean nextSourceIs(String prefix) {
        return sourceCode.startsWith(prefix);
    }

    private void skipCode(int i) {
        sourceCode = sourceCode.substring(1);
    }
}
