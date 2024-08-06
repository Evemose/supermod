package com.supermod.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class BaseClassShouldBeAbstract extends AbstractCheck {
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
            var modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
            if (modifiers == null || modifiers.findFirstToken(TokenTypes.ABSTRACT) == null) {
                log(ast.getLineNo(), "Base class should be abstract");
            }
        }
    }
}
