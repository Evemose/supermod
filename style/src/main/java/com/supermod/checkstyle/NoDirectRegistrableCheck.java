package com.supermod.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class NoDirectRegistrableCheck extends AbstractCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[] {TokenTypes.CLASS_DEF};
    }

    @Override
    public int[] getAcceptableTokens() {
        return new int[] {TokenTypes.CLASS_DEF};
    }

    @Override
    public int[] getRequiredTokens() {
        return new int[] {TokenTypes.CLASS_DEF};
    }

    @Override
    public void visitToken(DetailAST ast) {
        var className = ast.findFirstToken(TokenTypes.IDENT).getText();
        if (className.matches("\\w+Base")) {
            return;
        }

        var implementsClause = ast.findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE);
        if (implementsClause == null) {
            return;
        }

        var implementsList = implementsClause.findFirstToken(TokenTypes.COMMA);
        if (implementsList == null) {
            return;
        }

        for (var implementsAst = implementsList.getFirstChild(); implementsAst != null; implementsAst = implementsAst.getNextSibling()) {
            if (implementsAst.getType() == TokenTypes.IDENT && implementsAst.getText().equals("Registrable")) {
                log(implementsAst.getLineNo(), "Registrable should not be implemented directly, instead inherit from corresponding to your entity XBase class");
            }
        }
    }
}
