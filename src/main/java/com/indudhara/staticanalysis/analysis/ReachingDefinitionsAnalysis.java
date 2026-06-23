package com.indudhara.staticanalysis.analysis;

import com.indudhara.staticanalysis.ir.BasicBlock;
import com.indudhara.staticanalysis.ir.ControlFlowGraph;
import com.indudhara.staticanalysis.ir.Instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ReachingDefinitionsAnalysis implements DataflowAnalysis {
    private final Map<BasicBlock, Set<String>> inSets = new LinkedHashMap<>();
    private final Map<BasicBlock, Set<String>> outSets = new LinkedHashMap<>();
    private final Map<BasicBlock, Set<String>> genSets = new HashMap<>();
    private final Map<BasicBlock, Set<String>> killSets = new HashMap<>();
    private final Map<String, Set<String>> definitionsByVariable = new HashMap<>();

    @Override
    public void analyze(ControlFlowGraph cfg) {
        initialize(cfg);

        boolean changed;
        do {
            changed = false;

            // Forward fixed-point iteration:
            // IN[B] is the union of OUT sets from all predecessors.
            // OUT[B] is GEN[B] plus any incoming definitions not killed by B.
            for (BasicBlock block : cfg.getBlocks()) {
                Set<String> newIn = new LinkedHashSet<>();
                for (BasicBlock predecessor : block.getPredecessors()) {
                    newIn.addAll(outSets.get(predecessor));
                }

                Set<String> newOut = new LinkedHashSet<>(newIn);
                newOut.removeAll(killSets.get(block));
                newOut.addAll(genSets.get(block));

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
        genSets.clear();
        killSets.clear();
        definitionsByVariable.clear();

        Map<BasicBlock, List<String>> blockDefinitions = new HashMap<>();

        for (BasicBlock block : cfg.getBlocks()) {
            List<String> definitions = new ArrayList<>();
            int instructionNumber = 1;
            for (Instruction instruction : block.getInstructions()) {
                if (instruction.definesVariable()) {
                    String definition = formatDefinition(block, instructionNumber, instruction);
                    definitions.add(definition);
                    definitionsByVariable
                            .computeIfAbsent(instruction.getDefinedVariable(), ignored -> new LinkedHashSet<>())
                            .add(definition);
                }
                instructionNumber++;
            }
            blockDefinitions.put(block, definitions);
        }

        for (BasicBlock block : cfg.getBlocks()) {
            Set<String> gen = computeGenSet(block);
            Set<String> kill = new LinkedHashSet<>();

            for (String generatedDefinition : gen) {
                String variable = variableFromDefinition(generatedDefinition);
                kill.addAll(definitionsByVariable.getOrDefault(variable, Set.of()));
            }
            kill.removeAll(gen);

            genSets.put(block, gen);
            killSets.put(block, kill);
            inSets.put(block, new LinkedHashSet<>());
            outSets.put(block, new LinkedHashSet<>());
        }
    }

    private Set<String> computeGenSet(BasicBlock block) {
        Set<String> generated = new LinkedHashSet<>();
        Map<String, String> latestDefinitionByVariable = new LinkedHashMap<>();

        int instructionNumber = 1;
        for (Instruction instruction : block.getInstructions()) {
            if (instruction.definesVariable()) {
                latestDefinitionByVariable.put(
                        instruction.getDefinedVariable(),
                        formatDefinition(block, instructionNumber, instruction)
                );
            }
            instructionNumber++;
        }

        generated.addAll(latestDefinitionByVariable.values());
        return generated;
    }

    private String formatDefinition(BasicBlock block, int instructionNumber, Instruction instruction) {
        return instruction.getDefinedVariable() + "@" + block.getName() + "." + instructionNumber;
    }

    private String variableFromDefinition(String definition) {
        int separator = definition.indexOf('@');
        return separator >= 0 ? definition.substring(0, separator) : definition;
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
        return "Reaching Definitions Analysis";
    }
}
