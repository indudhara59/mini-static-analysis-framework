package com.indudhara.staticanalysis;

import com.indudhara.staticanalysis.analysis.DataflowAnalysis;
import com.indudhara.staticanalysis.analysis.LiveVariableAnalysis;
import com.indudhara.staticanalysis.analysis.ReachingDefinitionsAnalysis;
import com.indudhara.staticanalysis.examples.ExampleCFGFactory;
import com.indudhara.staticanalysis.ir.BasicBlock;
import com.indudhara.staticanalysis.ir.ControlFlowGraph;
import com.indudhara.staticanalysis.ir.Instruction;

import java.util.List;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ControlFlowGraph cfg = ExampleCFGFactory.createExampleCfg();

        printControlFlowGraph(cfg);

        List<DataflowAnalysis> analyses = List.of(
                new ReachingDefinitionsAnalysis(),
                new LiveVariableAnalysis()
        );

        for (DataflowAnalysis analysis : analyses) {
            analysis.analyze(cfg);
            printAnalysisResults(cfg, analysis);
        }
    }

    private static void printControlFlowGraph(ControlFlowGraph cfg) {
        System.out.println("=== Example Control Flow Graph ===");
        for (BasicBlock block : cfg.getBlocks()) {
            System.out.println(block.getName() + ":");
            for (Instruction instruction : block.getInstructions()) {
                System.out.println("  " + instruction.getStatement());
            }
            System.out.println("  successors: " + block.getSuccessors());
            System.out.println();
        }
    }

    private static void printAnalysisResults(ControlFlowGraph cfg, DataflowAnalysis analysis) {
        System.out.println("=== " + analysis.getName() + " ===");
        for (BasicBlock block : cfg.getBlocks()) {
            System.out.println(block.getName());
            System.out.println("  IN : " + analysis.getInSets().get(block));
            System.out.println("  OUT: " + analysis.getOutSets().get(block));
        }
        System.out.println();
    }
}
