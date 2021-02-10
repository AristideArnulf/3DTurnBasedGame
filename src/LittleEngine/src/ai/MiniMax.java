
package ai;

import logic.board.Move;
import mainGame.LogicLoop;
import java.util.Stack;

public class MiniMax implements Algo{
    
    private final Evaluator eval;
    private int searchDepth;
    
    public MiniMax(int searchDepth){
        this.eval = new RegularEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }
    
    
    @Override
    public Move execute(LogicLoop loop) {
        Move best = null;
        int high = Integer.MIN_VALUE;
        int low = Integer.MAX_VALUE;
        int value = 0;
        Stack<Move> moveStack = new Stack();
        for(Move m : loop.getBoard().allPossibleMoves(loop.getPlaying())){
            moveStack.push(m);
        }
        if(moveStack!=null){
            while(moveStack.size()>0){
                Move m = moveStack.pop();
                m.makeMove(true);
                value = loop.getPlaying().getSide() == 1 ? 
                min(loop, searchDepth-1) : 
                max(loop, searchDepth-1);
                if(loop.getPlaying().getSide() == 1 && value >= high){
                    high = value;
                    best = m;
                } else if(loop.getPlaying().getSide() == -1 && value<= low) {
                    low = value;
                    best = m;
                }
                m.goBack();

            }

        }
        System.out.println(value);

        return best;
    }
    
    public int min(LogicLoop loop, int depth){
        if(depth == 0|| loop.getBoard().checkIfPlayerCanMove(loop.getPlaying())){
            return this.eval.evaluate(loop, depth);
        }        
        int smallestValue = Integer.MAX_VALUE;
        Stack<Move> moveStack = new Stack();
        for(Move m : loop.getBoard().allPossibleMoves(loop.getPlaying())){
            moveStack.push(m);
        }
        if(moveStack!=null){
            while(moveStack.size()>0){
                Move nextMove = moveStack.pop();
                nextMove.makeMove(true);
                int value = max(loop, depth-1);
                if(value <= smallestValue){
                    smallestValue = value;                  
                }
                nextMove.goBack();
                
            }            
        }
        return smallestValue; 
    }
    
    public int max(LogicLoop loop, int depth){
        if(depth == 0 || loop.getBoard().checkIfPlayerCanMove(loop.getPlaying())){
            return this.eval.evaluate(loop, depth);
        } 
        int highestValue = Integer.MIN_VALUE;
        Stack<Move> moveStack = new Stack();
        for(Move m : loop.getBoard().allPossibleMoves(loop.getPlaying())){
            moveStack.push(m);
        }
        if(moveStack!=null){
            while(moveStack.size()>0){
                Move nextMove = moveStack.pop();
                nextMove.makeMove(true);
                int value = min(loop, depth-1);
                if(value >= highestValue){
                    highestValue = value;                  
                }
                nextMove.goBack();

            }            
        }
        return highestValue; 
    }

    

    

    
    
}
