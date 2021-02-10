package ai;


import mainGame.LogicLoop;

public interface Evaluator {
    int evaluate(LogicLoop loop, int depth);
    
}
