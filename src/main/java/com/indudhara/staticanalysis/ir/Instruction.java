package com.indudhara.staticanalysis.ir;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class Instruction {
    private final String definedVariable;
    private final Set<String> usedVariables;
    private final String statement;

    public Instruction(String definedVariable, Set<String> usedVariables, String statement) {
        this.definedVariable = definedVariable;
        this.usedVariables = Collections.unmodifiableSet(new LinkedHashSet<>(usedVariables));
        this.statement = Objects.requireNonNull(statement, "statement");
    }

    public static Instruction assign(String definedVariable, Set<String> usedVariables, String statement) {
        return new Instruction(definedVariable, usedVariables, statement);
    }

    public String getDefinedVariable() {
        return definedVariable;
    }

    public Set<String> getUsedVariables() {
        return usedVariables;
    }

    public String getStatement() {
        return statement;
    }

    public boolean definesVariable() {
        return definedVariable != null && !definedVariable.isBlank();
    }

    @Override
    public String toString() {
        return statement;
    }
}
