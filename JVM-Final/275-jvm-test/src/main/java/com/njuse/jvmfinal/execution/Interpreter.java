package com.njuse.jvmfinal.execution;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;


public class Interpreter {
    private static ByteBuffer codeReader;

    public static void interpret(JThread thread) {
        initCodeReader(thread);
        loop(thread);
    }

    private static void initCodeReader(JThread thread) {
        byte[] code = thread.getTopFrame().getMethod().getCode();
        codeReader = ByteBuffer.wrap(code);
        int nextPC = thread.getTopFrame().getNextPC();
        codeReader.position(nextPC);
    }

    private static void loop(JThread thread) {
        while (true) {
            StackFrame oriTop = thread.getTopFrame();
            //parse code attribute for VO
            Method method = oriTop.getMethod();
            if (!method.isParsed()) {
                method.parseCode();
            }
            //set the reader's position to nextPC
            codeReader.position(oriTop.getNextPC());
            //fetch and decode
            int opcode = codeReader.get() & 0xff;
            Instruction instruction = Decoder.decode(opcode);
            //get operands
            instruction.fetchOperands(codeReader);
            //set nextPC to reader's position
            int nextPC = codeReader.position();
            oriTop.setNextPC(nextPC);
            instruction.execute(oriTop);
            //check whether there's a new frame
            //and whether there's more frame to exec
            StackFrame newTop = thread.getTopFrame();
            if (newTop == null) {
                return;
            }

            if (oriTop != newTop) {
                initCodeReader(thread);
            }
        }

    }

}
