package com.supermod.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class RequiresFactoryCheck extends AbstractCheck {
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
        var initialAst = ast;
        var className = ast.findFirstToken(TokenTypes.IDENT).getText();

        if (ast.findFirstToken(TokenTypes.MODIFIERS) != null && ast.findFirstToken(TokenTypes.MODIFIERS).findFirstToken(TokenTypes.ABSTRACT) != null) {
            return;
        }

        var foundCreatable = false;
        var implementsClause = ast.findFirstToken(TokenTypes.IMPLEMENTS_CLAUSE);

        if (implementsClause != null) {
            for (var child = implementsClause.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getType() == TokenTypes.IDENT) {
                    var interfaceName = child.getText();

                    if (interfaceName.equals("Creatable")) {
                        foundCreatable = true;
                        break;
                    }
                }
            }
        }

        String baseClassName = null;
        if (!foundCreatable) {
            var extendsClause = ast.findFirstToken(TokenTypes.EXTENDS_CLAUSE);

            if (extendsClause != null) {
                var parentClassName = extendsClause.findFirstToken(TokenTypes.IDENT).getText();
                if (parentClassName.matches("\\w+Base")) {
                    baseClassName = parentClassName;
                }
            }
        }

        if (!foundCreatable && baseClassName == null) {
            return;
        }

        for (ast = initialAst.findFirstToken(TokenTypes.OBJBLOCK).getFirstChild(); ast != null; ast = ast.getNextSibling()) {
            if (ast.getType() == TokenTypes.METHOD_DEF) {
                var mods = ast.findFirstToken(TokenTypes.MODIFIERS);
                if (mods == null || mods.findFirstToken(TokenTypes.LITERAL_STATIC) == null || mods.findFirstToken(TokenTypes.LITERAL_PUBLIC) == null) {
                    continue;
                }
                var methodName = ast.findFirstToken(TokenTypes.LPAREN).getPreviousSibling().getText();
                var returnType = ast.findFirstToken(TokenTypes.TYPE).getFirstChild().getText();
                if (methodName.equals("createInstance")
                    && ast.findFirstToken(TokenTypes.PARAMETERS).getChildCount() == 0
                    && className.equals(returnType)) {
                    return;
                }
            }
        }

        if (foundCreatable) {
            log(initialAst.getLineNo(),
                "As non-abstract class that implements Creatable, {0} must declare a public static {0} createInstance() method", className);
        } else {
            log(initialAst.getLineNo(),
                "As non-abstract class that extends {0}, {1} must declare a public static {1} createInstance() method", baseClassName, className);
        }
    }
}
