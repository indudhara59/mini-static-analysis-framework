package com.indudhara.staticanalysis.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ControlFlowGraph {
    private final List<BasicBlock> blocks = new ArrayList<>();
    private BasicBlock entryBlock;

    public void addBlock(BasicBlock block) {
        Objects.requireNonNull(block, "block");
        if (blocks.isEmpty()) {
            entryBlock = block;
        }
        blocks.add(block);
    }

    public void addEdge(BasicBlock from, BasicBlock to) {
        from.addSuccessor(to);
    }

    public List<BasicBlock> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public BasicBlock getEntryBlock() {
        return entryBlock;
    }
}
