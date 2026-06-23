package com.indudhara.staticanalysis.analysis;

import com.indudhara.staticanalysis.ir.BasicBlock;
import com.indudhara.staticanalysis.ir.ControlFlowGraph;
import com.indudhara.staticanalysis.ir.Instruction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class LiveVariableAnalysis implements DataflowAnalysis {
    private final Map<BasicBlock, Set<String>> inSets = new LinkedHashMap<>();
    private final Map<BasicBlock, Set<String>> outSets = new LinkedHashMap<>();
    private final Map<BasicBlock, Set<String>> useSets = new HashMap<>();
    private final Map<BasicBlock, Set<String>> defSets = new HashMap<>();

    @Override
    public void analyze(ControlFlowGraph cfg) {
        initialize(cfg);

        boolean changed;
        do {
            changed = false;
            List<BasicBlock> reverseBlocks = new ArrayList<>(cfg.getBlocks());
            Collections.reverse(reverseBlocks);

            // Backward fixed-point iteration:
            // OUT[B] is the union of IN sets from all successors.
            // IN[B] is USE[B] plus variables live after B that are not defined in B.
            for (BasicBlock block : reverseBlocks) {
                Set<String> newOut = new LinkedHashSet<>();
                for (BasicBlock successor : block.getSuccessors()) {
                    newOut.addAll(inSets.get(successor));
                }

                Set<String> newIn = new LinkedHashSet<>(newOut);
                newIn.removeAll(defSets.get(block));
                newIn.addAll(useSets.get(block));

                if (!newIn.equals(inSets.get(block)) || !newOut.equals(outSets.get(block))) {
                    inSets.put(block, newIn);
                    outSets.put(block, newOut);
                    changed = true;
                }
            }
        } while (changed);
    }

    private void initialize(ControlFlowGraph cfg) {
        inSets.clear();
        outSets.clear();
        useSets.clear();
        defSets.clear();

        for (BasicBlock block : cfg.getBlocks()) {
            Set<String> usesBeforeDefinition = new LinkedHashSet<>();
            Set<String> definitions = new LinkedHashSet<>();

            for (Instruction instruction : block.getInstructions()) {
                for (String usedVariable : instruction.getUsedVariables()) {
                    if (!definitions.contains(usedVariable)) {
                        usesBeforeDefinition.add(usedVariable);
                    }
                }
                if (instruction.definesVariable()) {
                    definitions.add(instruction.getDefinedVariable());
                }
            }

            useSets.put(block, usesBeforeDefinition);
            defSets.put(block, definitions);
            inSets.put(block, new LinkedHashSet<>());
            outSets.put(block, new LinkedHashSet<>());
        }
    }

    @Override
    public Map<BasicBlock, Set<String>> getInSets() {
        return inSets;
    }

    @Override
    public Map<BasicBlock, Set<String>> getOutSets() {
        return outSets;
    }

    @Override
    public String getName() {
        return "Live Variable Analysis";
    }
}
