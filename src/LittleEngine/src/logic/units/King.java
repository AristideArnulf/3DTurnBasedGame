package logic.units;

import java.util.ArrayList;

import logic.board.Move;
import logic.board.Board;
import logic.player.Player;

import java.awt.Point;

public class King extends Unit {
	
    public King(int x, int y, Board b, Player p) {
            super(x, y, b, p);
    }

    @Override
    public String getUnitClass() {
            return "King";
    }
    
    @Override
    public ArrayList<Move> allMoves() {
        ArrayList<Move> mList = new ArrayList<>();
        board.movesKing(point.x, point.y).forEach((Point pt) -> {
            Move m = new Move(pt, this);
            if (board.validLocationToMoveTo(pt, p, true)) {
                mList.add(m);
            }
        });
        return mList;
    }

    public int getValue() {
        return 10000;
    }

}
