package org.blackjack;

import org.blackjack.entity.Assignment;
import org.blackjack.entity.Print;
import org.blackjack.entity.SourceCode;
import org.blackjack.entity.Statement;

import java.util.HashMap;
import java.util.Map;

public class Backend {
    private Map<String, String> variables;
    private SourceCode ast;
    private Parser parser;

    public Backend() {
        this.variables = new HashMap<>();
        this.parser = new Parser();
    }

    public void execute(String code) throws Exception {
        ast = parser.parse(code);
        resolveAST(ast);
    }

    public void resolveAST(SourceCode sourceCode) throws Exception {
        if (sourceCode.getStatements().size() == 0) {
            throw new Exception("no code");
        }
        for (Statement statement : sourceCode.getStatements()) {
            resolveStatement(statement);
        }
    }

    public void resolveStatement(Statement statement) throws Exception {
        if (statement instanceof Assignment) {
            resolveAssignment(statement);
        } else if (statement instanceof Print) {
            resolvePrint(statement);
        } else {
            throw new Exception("unknown statement");
        }
    }

    public void resolveAssignment(Statement assignment) throws Exception {
        String varName = ((Assignment) assignment).getVariable().getName();
        if (varName == null || "".equals(varName)) {
            throw new Exception("empty var name");
        }
        variables.put(varName, ((Assignment) assignment).getString());
    }

    public void resolvePrint(Statement print) throws Exception {
        String varName = ((Print) print).getVariable().getName();
        if (varName == null || "".equals(varName)) {
            throw new Exception("empty var name");
        }
        String value = variables.getOrDefault(varName, null);
        if(value==null){
            throw new Exception("value is null");
        }
        System.out.println(value);
    }
}
