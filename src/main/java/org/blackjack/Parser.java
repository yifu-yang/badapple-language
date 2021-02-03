package org.blackjack;

import org.blackjack.entity.*;

import java.util.ArrayList;
import java.util.List;

import static org.blackjack.Token.*;

public class Parser {

    private Lexer lexer;


    public Lexer getLexer() {
        return lexer;
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    public Print parsePrint() throws Exception {
        Print print = new Print();
        print.setLineNumber(lexer.getLineNum());
        lexer.nextTokenIs(TOKEN_PRINT);
        lexer.nextTokenIs(Token.TOKEN_LEFT_PAREN);
        lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
        print.setVariable(parseVariable());
        lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
        lexer.nextTokenIs(Token.TOKEN_RIGHT_PAREN);
        lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
        return print;
    }

    public Statement parseStatement() throws Exception {
        switch (lexer.lookAhead()) {
            case TOKEN_PRINT:
                return parsePrint();
            case TOKEN_VAR_PREFIX:
                return parseAssignment();
            default:
                throw new Exception("unknown token");
        }
    }

    public List<Statement> parseStatements() throws Exception {
        List<Statement> statements = new ArrayList<>();
        while(!isSourceEnd(lexer.lookAhead())){
            statements.add(parseStatement());
        }
        return statements;
    }

    public SourceCode parseSourceCode() throws Exception {
        SourceCode sourceCode = new SourceCode();
        sourceCode.setLineNumber(lexer.getLineNum());
        sourceCode.setStatements(parseStatements());
        return sourceCode;
    }

    public SourceCode parse(String code) throws Exception {
        SourceCode sourceCode = new SourceCode();
        lexer = new Lexer(code);
        sourceCode = parseSourceCode();
        lexer.nextTokenIs(TOKEN_EOF);
        return sourceCode;
    }


    private boolean isSourceEnd(Token token){
        return token == TOKEN_EOF;
    }

    public Variable parseVariable() throws Exception {
        Variable variable = new Variable();
        variable.setLineNumber(lexer.getLineNum());
        lexer.nextTokenIs(Token.TOKEN_VAR_PREFIX);
        variable.setName(parseName());
        lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
        return variable;
    }

    public Statement parseAssignment() throws Exception {
        Assignment assignment = new Assignment();
        assignment.setLineNumber(lexer.getLineNum());
        assignment.setVariable(parseVariable());
        lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
        lexer.nextTokenIs(Token.TOKEN_EQUAL);
        lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
        assignment.setString(parseString());
        lexer.lookAheadAndSkip(TOKEN_IGNORED);
        return assignment;
    }

    public String parseName() throws Exception {
        Token t = lexer.nextTokenIs(Token.TOKEN_NAME);
        return t.getValue();
    }

    public String parseString() throws Exception {
        String s = "";
        switch (lexer.lookAhead()) {
            case TOKEN_DUOQUOTE:
                lexer.nextTokenIs(Token.TOKEN_DUOQUOTE);
                lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
                return s;
            case TOKEN_QUOTE:
                lexer.nextTokenIs(Token.TOKEN_QUOTE);
                s = lexer.scanBeforeToken("\"");
                lexer.nextTokenIs(Token.TOKEN_QUOTE);
                lexer.lookAheadAndSkip(Token.TOKEN_IGNORED);
                return s;
            default:
                throw new Exception("not a String");
        }
    }
}
