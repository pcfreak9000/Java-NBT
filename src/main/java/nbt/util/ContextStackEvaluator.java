package nbt.util;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class ContextStackEvaluator {
    
    public static enum ContextType {
        Flat, Recursive;
    }
    
    public static interface IContext {
        
        IContext evaluate() throws IOException;
        
        ContextType type();
        
    }
    
    public static void evaluate(IContext c) throws IOException {
        Deque<IContext> stack = new ArrayDeque<>();
        stack.push(c);
        while (!stack.isEmpty()) {
            IContext top = stack.peek();
            IContext out = top.evaluate();
            switch (top.type()) {
            case Flat:
                stack.pop();
                if (out != null) {
                    stack.push(out);
                }
                break;
            case Recursive:
                if (out != null) {
                    stack.push(out);
                } else {
                    stack.pop();
                }
                break;
            default:
                throw new IllegalArgumentException("" + top.type());
            }
        }
    }
    
}
