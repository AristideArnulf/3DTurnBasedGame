package logic.units;

import java.awt.Point;
import java.util.ArrayList;

import logic.board.Move;
import logic.board.Board;
import logic.player.Player;

public class Knight extends Unit {

    public Knight(int x, int y, Board b, Player p) {
            super(x, y, b, p);
    }
    
    @Override
    public String getUnitClass() {
            return "Knight";
    }
    
    @Override
    public ArrayList<Move> allMoves() {
        ArrayList<Move> mList = new ArrayList<>();
        board.movesKnight(point.x, point.y).forEach((Point pt) -> {
            Move m = new Move(pt, this);
            if (board.validLocationToMoveTo(pt, p, true)) {
                mList.add(m);
            }
        });
        return mList;
    }

    
    public int getValue() {
        return 1000;
    }

}
