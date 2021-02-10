
package ai;

import logic.board.Move;
import logic.player.Player;
import logic.units.Unit;
import mainGame.LogicLoop;

public class RegularEvaluator implements Evaluator{

    @Override
    public int evaluate(LogicLoop loop, int depth) {
        return score(loop, loop.getP1(), depth) -  score(loop, loop.getP2(), depth);
    }
    
    private int score(LogicLoop loop, Player p, int depth){
        return valueOfPieces(p)  + checkmateScore(p, loop, depth) + checkScore(p, loop) + possibleMoves(p, loop) + attackScore(p, loop);
    }
    
    private static int valueOfPieces(Player p){
        int finalValue = 0;
        for(Unit u: p.getUnits()){
            finalValue += u.getValue();
        }
        return finalValue;
    }
    private static int depthScore(int depth){
        return depth == 0 ? 1: 100 * depth;
    }
    private static int checkmateScore(Player p, LogicLoop loop, int depth){
        return loop.getBoard().checkIfPlayerCanMove(p.getOpponent()) ? 10000 * depthScore(depth) : 0;
        
    }
    private static int checkScore(Player p, LogicLoop loop){
        return loop.getBoard().isEatable(p.getOpponent().getK().getPoint(), p.getOpponent()) ? 100 : 0;       
    }
    
    private static int possibleMoves(Player p, LogicLoop loop){
        return loop.getBoard().allPossibleMoves(p).size();
        
    }
    private static int attackScore(Player p , LogicLoop loop){
        int attackScore = 0;
        for(Unit u: p.getUnits()){
            for(Move m: u.attackMoves()){
                if(m.getUnit().getValue()<= loop.getBoard().getUnitAtLocation(m.getDestLoc().x, m.getDestLoc().y).getValue()){
                    attackScore++;
                }
            }
            
        }
        return attackScore * 2;
    }
}
