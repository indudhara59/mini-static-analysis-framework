package com.indudhara.staticanalysis.analysis;

import com.indudhara.staticanalysis.ir.BasicBlock;
import com.indudhara.staticanalysis.ir.ControlFlowGraph;

import java.util.Map;
import java.util.Set;

public interface DataflowAnalysis {
    void analyze(ControlFlowGraph cfg);

    Map<BasicBlock, Set<String>> getInSets();

    Map<BasicBlock, Set<String>> getOutSets();

    String getName();
}
