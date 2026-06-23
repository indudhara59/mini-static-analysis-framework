package com.indudhara.staticanalysis.examples;

import com.indudhara.staticanalysis.ir.BasicBlock;
import com.indudhara.staticanalysis.ir.ControlFlowGraph;
import com.indudhara.staticanalysis.ir.Instruction;

import java.util.Set;

public final class ExampleCFGFactory {
    private ExampleCFGFactory() {
    }

    public static ControlFlowGraph createExampleCfg() {
        ControlFlowGraph cfg = new ControlFlowGraph();

        BasicBlock b1 = new BasicBlock("B1");
        b1.addInstruction(Instruction.assign("x", Set.of(), "x = 1"));
        b1.addInstruction(Instruction.assign("y", Set.of(), "y = 2"));

        BasicBlock b2 = new BasicBlock("B2");
        b2.addInstruction(Instruction.assign("z", Set.of("x", "y"), "z = x + y"));

        BasicBlock b3 = new BasicBlock("B3");
        b3.addInstruction(Instruction.assign("x", Set.of("z"), "x = z + 1"));

        BasicBlock b4 = new BasicBlock("B4");
        b4.addInstruction(Instruction.assign("y", Set.of("z"), "y = z + 2"));

        BasicBlock b5 = new BasicBlock("B5");
        b5.addInstruction(Instruction.assign("result", Set.of("x", "y"), "result = x + y"));

        cfg.addBlock(b1);
        cfg.addBlock(b2);
        cfg.addBlock(b3);
        cfg.addBlock(b4);
        cfg.addBlock(b5);

        cfg.addEdge(b1, b2);
        cfg.addEdge(b2, b3);
        cfg.addEdge(b2, b4);
        cfg.addEdge(b3, b5);
        cfg.addEdge(b4, b5);

        return cfg;
    }
}
