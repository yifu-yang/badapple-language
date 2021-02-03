package org.blackjack;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.blackjack.Token.*;

public class Lexer {
    private static final String NAME_PATTERN = "^[_\\d\\w]+";
    private String sourceCode;
    private int lineNum;
    private String nextToken;
    private Token nextTokenType;
    private int nextTokenLineNum;
    private Map<String,Token> keywordMap;

    public Lexer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.lineNum = 1;
        this.nextToken = "";
        this.nextTokenType = Token.TOKEN_EOF;
        this.nextTokenLineNum = 0;

        keywordMap = new HashMap<>();
        keywordMap.put("$", TOKEN_VAR_PREFIX);
        keywordMap.put("\"",TOKEN_QUOTE);
        keywordMap.put("(",TOKEN_LEFT_PAREN);
        keywordMap.put(")",TOKEN_RIGHT_PAREN);
        keywordMap.put("=",TOKEN_EQUAL);
        keywordMap.put("print",TOKEN_PRINT);
        keywordMap.put("\"\"",TOKEN_DUOQUOTE);

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

    public void lookAheadAndSkip(Token token) throws Exception {
        int nowLineNum = lineNum;
        Token t = getNextTokenObject();
        if (!t.getTokenValue().equals(token.getTokenValue())) {
            lineNum = nowLineNum;
            nextTokenLineNum = t.getLineNumber();
            nextTokenType = t;
            nextToken = t.getValue();
        }
    }

    public Token lookAhead() throws Exception {
        if (nextTokenLineNum > 0) {
            return nextTokenType;
        }
        int nowLineNum = lineNum;
        Token t = getNextTokenObject();
        lineNum = nowLineNum;
        nextTokenLineNum = t.getLineNumber();
        nextTokenType = t;
        nextToken = t.getValue();
        return t;
    }

    public Token matchNext() throws Exception {
        if (isIgnore()) {
            Token t1 = Token.TOKEN_IGNORED;
            t1.setLineNumber(this.lineNum);
            t1.setValue("Ignored");
            return t1;
        }

        if (sourceCode.length() == 0) {
            Token t1 = Token.TOKEN_EOF;
            t1.setLineNumber(this.lineNum);
            t1.setValue("EOF");
            return t1;
        }

        switch (sourceCode.charAt(0)) {
            case '$':
                skipCode(1);
                Token t1 = TOKEN_VAR_PREFIX;
                t1.setLineNumber(this.lineNum);
                t1.setValue("$");
                return t1;
            case '(':
                skipCode(1);
                Token t2 = Token.TOKEN_LEFT_PAREN;
                t2.setLineNumber(this.lineNum);
                t2.setValue("(");
                return t2;
            case ')':
                skipCode(1);
                Token t3 = Token.TOKEN_RIGHT_PAREN;
                t3.setLineNumber(this.lineNum);
                t3.setValue(")");
                return t3;
            case '=':
                skipCode(1);
                Token t4 = Token.TOKEN_EQUAL;
                t4.setLineNumber(this.lineNum);
                t4.setValue("=");
                return t4;
            case '"':
                if (nextSourceIs("\"\"")) {
                    skipCode(2);
                    Token t5 = Token.TOKEN_DUOQUOTE;
                    t5.setLineNumber(this.lineNum);
                    t5.setValue("\"\"");
                    return t5;
                }
                skipCode(1);
                Token t5 = Token.TOKEN_QUOTE;
                t5.setLineNumber(this.lineNum);
                t5.setValue("\"");
                return t5;
        }

        // for many char token  todo need use keyword map to check keyword
        if (sourceCode.charAt(0) == '_' || isLetter(sourceCode.charAt(0))) {
            Pattern r = Pattern.compile(NAME_PATTERN);
            Matcher matcher = r.matcher(sourceCode);
            if (matcher.find()) {
                String matchStr = matcher.group();
                skipCode(matchStr.length());
                Token t5 = Token.TOKEN_NAME;
                if(keywordMap.containsKey(matchStr)){
                    t5 = keywordMap.get(matchStr);
                }
                t5.setLineNumber(this.lineNum);
                t5.setValue(matchStr);
                return t5;
            }
        }

        throw new Exception("unknown many token:" + sourceCode);
    }

    public Token getNextTokenObject() throws Exception {
        if (nextTokenLineNum > 0) {
            Token t = nextTokenType;
            t.setLineNumber(nextTokenLineNum);
            t.setValue(nextToken);
            lineNum = nextTokenLineNum;
            nextTokenLineNum = 0;
            return t;
        }
        return matchNext();
    }

    public Token nextTokenIs(Token tokenType) throws Exception {
        Token t = getNextTokenObject();
        if (!tokenType.getTokenValue().equals(t.getTokenValue())) {
            throw new Exception("wrong token type");
        }
        return t;
    }

    public String scanBeforeToken(String token) {
        String[] s = sourceCode.split(token);
        if (s.length < 2) {
            System.out.println("unreachable");
            return "";
        }
        skipCode(s[0].length());
        return s[0];
    }

    private boolean isLetter(char c) {
        return c <= 'Z' && c >= 'A' || c <= 'z' && c >= 'a';
    }

    private boolean isIgnore() {
        boolean isIgnore = false;
        while (sourceCode.length() > 0) {
            if (nextSourceIs("\r\n") || nextSourceIs("\n\r")) {
                skipCode(2);
                lineNum++;
                isIgnore = true;
            } else if (nextSourceIs("\n") || nextSourceIs("\r")) {
                skipCode(1);
                lineNum++;
                isIgnore = true;
            } else if ("\t\f ".contains(String.valueOf(sourceCode.charAt(0)))) {
                skipCode(1);
                isIgnore = true;
            } else {
                break;
            }
        }
        return isIgnore;
    }

    private Boolean nextSourceIs(String prefix) {
        return sourceCode.startsWith(prefix);
    }

    private void skipCode(int i) {
        sourceCode = sourceCode.substring(i);
    }
}
