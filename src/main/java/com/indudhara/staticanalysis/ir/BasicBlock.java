package com.indudhara.staticanalysis.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BasicBlock {
    private final String name;
    private final List<Instruction> instructions = new ArrayList<>();
    private final List<BasicBlock> successors = new ArrayList<>();
    private final List<BasicBlock> predecessors = new ArrayList<>();

    public BasicBlock(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    public String getName() {
        return name;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(Objects.requireNonNull(instruction, "instruction"));
    }

    public List<Instruction> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    public void addSuccessor(BasicBlock successor) {
        Objects.requireNonNull(successor, "successor");
        if (!successors.contains(successor)) {
            successors.add(successor);
        }
        if (!successor.predecessors.contains(this)) {
            successor.predecessors.add(this);
        }
    }

    public List<BasicBlock> getSuccessors() {
        return Collections.unmodifiableList(successors);
    }

    public List<BasicBlock> getPredecessors() {
        return Collections.unmodifiableList(predecessors);
    }

    @Override
    public String toString() {
        return name;
    }
}
