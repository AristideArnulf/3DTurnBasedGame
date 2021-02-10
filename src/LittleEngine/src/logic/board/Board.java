package logic.board;

import logic.units.Unit;
import logic.player.Player;
import logic.gametype.GameType;
import java.awt.Point;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;


public abstract class Board {
	
    protected GameType gameType;

    protected Board(GameType gameType) {
        this.gameType = gameType;
    }
    
    public abstract boolean validLocation(int x, int y);

    public abstract boolean containsOpponentUnit(int x, int y, Player p);

    public abstract boolean validLocationToMoveTo(int x, int y, Player p,
            boolean eatable);
    
    public boolean isEatable(int x, int y, Player p) {
        return p.getOpponent().getUnits().stream().anyMatch((unit) -> 
                (unit.attackMoves().stream().anyMatch((m) -> 
                        (m.getDestLoc().equals(new Point(x, y))))));
    }

    public boolean isEatable(Point point, Player p) {
        return Board.this.isEatable(point.x, point.y, p);
    }

    public boolean isAllowedMove(Move m) {
        m.makeMove(false);
        boolean num = gameType.isValidBoard(this, m.getUnit().getPlayer(), m);
        m.goBack();
        return num;
    }

    public ArrayList<Move> allPossibleMoves(Player p) {
        ArrayList<Move> mList = new ArrayList<>();
        p.getUnits().forEach((unit) -> {
            mList.addAll(unit.allowedMoves());
        });
        return mList;
    }

    public boolean checkIfPlayerCanMove(Player p) { 
        return allPossibleMoves(p).isEmpty();
    }

    public boolean validLocation(Point point) {
        return Board.this.validLocation(point.x, point.y);
    }

    public boolean containsOpponentUnit(Point point, Player p) {
        return Board.this.containsOpponentUnit(point.x, point.y, p);
    }

    public boolean validLocationToMoveTo(Point point, Player p,
            boolean eatable) {
        return Board.this.validLocationToMoveTo(point.x, point.y, p, eatable);
    } 
	
    public abstract ArrayList<Point> movesKnight(int x, int y);
    
    public abstract ArrayList<Point> movesKing(int x, int y);
    
    public abstract ArrayList<Point> movesGnome(int x, int y);
    
    public abstract Vector3f actualLocationToRenderLocation(Point point);
    
    public abstract Vector3f actualLocationToRenderLocationBoard(Point point);
    
    public abstract Point renderLocationToActualLocation(Vector3f point);

    public abstract Point renderLocationToActualLocationBoard(Vector3f point);

    public abstract Unit getUnitAtLocation(int x, int y);

    public abstract Unit setUnitAtLocation(Unit unit, int x, int y);

    public abstract String getBoardString();
}