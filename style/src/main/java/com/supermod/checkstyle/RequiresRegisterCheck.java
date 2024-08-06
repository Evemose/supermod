package com.supermod.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class RequiresRegisterCheck extends AbstractCheck {
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

        String baseClassName = null;
        var extendsClause = ast.findFirstToken(TokenTypes.EXTENDS_CLAUSE);

        if (extendsClause != null) {
            var parentClassName = extendsClause.findFirstToken(TokenTypes.IDENT).getText();
            if (parentClassName.matches("\\w+Base")) {
                baseClassName = parentClassName;
            }
        }

        if (baseClassName == null) {
            return;
        }

        for (ast = initialAst.findFirstToken(TokenTypes.OBJBLOCK).getFirstChild(); ast != null; ast = ast.getNextSibling()) {
            if (ast.getType() == TokenTypes.METHOD_DEF) {
                var mods = ast.findFirstToken(TokenTypes.MODIFIERS);
                if (mods == null || mods.findFirstToken(TokenTypes.LITERAL_STATIC) == null || mods.findFirstToken(TokenTypes.LITERAL_PUBLIC) == null) {
                    continue;
                }

                if (ast.findFirstToken(TokenTypes.PARAMETERS).getChildCount() != 1) {
                    continue;
                }

                if (ast.findFirstToken(TokenTypes.PARAMETERS).getFirstChild().findFirstToken(TokenTypes.TYPE)
                    .findFirstToken(TokenTypes.TYPE_ARGUMENTS) == null) {
                    continue;
                }

                var methodName = ast.findFirstToken(TokenTypes.LPAREN).getPreviousSibling().getText();
                var returnType = ast.findFirstToken(TokenTypes.TYPE).getFirstChild().getText();
                var returnTypeArgs = ast.findFirstToken(TokenTypes.TYPE).findFirstToken(TokenTypes.TYPE_ARGUMENTS);

                if (returnTypeArgs == null) {
                    continue;
                }

                var returnTypeArg = returnTypeArgs.findFirstToken(TokenTypes.TYPE_ARGUMENT).findFirstToken(TokenTypes.IDENT).getText();

                var firstParamType = ast.findFirstToken(TokenTypes.PARAMETERS).getFirstChild().findFirstToken(TokenTypes.TYPE).findFirstToken(TokenTypes.IDENT).getText();
                var typeArg = ast.findFirstToken(TokenTypes.PARAMETERS).getFirstChild().findFirstToken(TokenTypes.TYPE)
                    .findFirstToken(TokenTypes.TYPE_ARGUMENTS).findFirstToken(TokenTypes.TYPE_ARGUMENT).findFirstToken(TokenTypes.IDENT).getText();

                if (methodName.equals("register")
                    && ast.findFirstToken(TokenTypes.PARAMETERS).getChildCount() == 1
                    && firstParamType.equals("DeferredRegister")
                    && typeArg.equals(baseClassName.replace("Base", ""))
                    && returnType.equals("RegistryObject")
                    && returnTypeArg.equals(baseClassName.replace("Base", ""))
                ) {
                    return;
                }
            }
        }

        log(initialAst.getLineNo(),
            "As non-abstract class that extends {0}, {1} must declare a public static RegistryObject<{2}> register(DeferredRegister<{2}> register) method",
            baseClassName, className, baseClassName.replace("Base", ""));
    }
}
