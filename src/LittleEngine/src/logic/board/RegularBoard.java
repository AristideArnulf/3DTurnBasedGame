package logic.board;

import logic.units.Unit;
import logic.player.Player;
import logic.gametype.GameType;
import java.awt.Point;
import java.util.ArrayList;
import org.lwjgl.util.vector.Vector3f;

public class RegularBoard extends Board {

    private final static float RENDER = 2.8f;
    private final static float BOARDRENDER = 3.17f;
    private Unit[][] boardRep;
    private int width;
    private int height;
    private String boardTxt;

    public RegularBoard(GameType gameType) {
        this(8, 8, gameType);
    }

    public RegularBoard(int width, int height, GameType gameType) {
        super(gameType);
        this.boardRep = new Unit[width][height];
        this.width = width;
        this.height = height;
        this.boardTxt = "8x8 GameBoard";
    }

    @Override
    public ArrayList<Point> movesKnight(int x, int y) {
        ArrayList<Point> mList = new ArrayList<>();
        mList.add(new Point(x, y+1));
        mList.add(new Point(x+1, y+1));
        mList.add(new Point(x+1, y));
        mList.add(new Point(x+1, y-1));
        mList.add(new Point(x, y-1));
        mList.add(new Point(x-1, y-1));
        mList.add(new Point(x-1, y));
        mList.add(new Point(x-1, y+1));
        mList.add(new Point(x+2, y+1));
        mList.add(new Point(x+2, y+2));
        mList.add(new Point(x+2, y));
        mList.add(new Point(x+2, y-1));
        mList.add(new Point(x+2, y-2));
        mList.add(new Point(x-2, y+1));
        mList.add(new Point(x-2, y+2));
        mList.add(new Point(x-2, y));
        mList.add(new Point(x-2, y-1));
        mList.add(new Point(x-2, y-2));
        mList.add(new Point(x+1, y+2));
        mList.add(new Point(x+1, y-2));
        mList.add(new Point(x-1, y+2));
        mList.add(new Point(x-1, y-2));
        mList.add(new Point(x, y+2));
        mList.add(new Point(x, y-2));
        return mList;
    }
    
    @Override
    public ArrayList<Point> movesKing(int x, int y) {
        ArrayList<Point> mList = new ArrayList<>();
        mList.add(new Point(x, y+1));
        mList.add(new Point(x+1, y+1));
        mList.add(new Point(x+1, y));
        mList.add(new Point(x+1, y-1));
        mList.add(new Point(x, y-1));
        mList.add(new Point(x-1, y-1));
        mList.add(new Point(x-1, y));
        mList.add(new Point(x-1, y+1));
        return mList;
    }
    
    @Override
    public ArrayList<Point> movesGnome(int x, int y) {
        ArrayList<Point> mList = new ArrayList<>();
        mList.add(new Point(x, y+1));
        mList.add(new Point(x+1, y+1));
        mList.add(new Point(x+1, y));
        mList.add(new Point(x+1, y-1));
        mList.add(new Point(x, y-1));
        mList.add(new Point(x-1, y-1));
        mList.add(new Point(x-1, y));
        mList.add(new Point(x-1, y+1));
        return mList;
    }
    
    @Override
    public boolean validLocation(int x, int y) {
        return !(x >= width || y >= height || y < 0 || x < 0);
    }

    @Override
    public boolean containsOpponentUnit(int x, int y, Player p) {
        return boardRep[x][y] != null && 
                boardRep[x][y].getPlayer() == p.getOpponent() &&
                validLocation(x, y);
    }

    @Override
    public boolean validLocationToMoveTo(int x, int y, Player p, 
            boolean eatable) {
        return validLocation(x, y) && 
                (boardRep[x][y] == null || 
                (containsOpponentUnit(x, y, p) && eatable));
    }
    
    @Override
    public Point renderLocationToActualLocation(Vector3f point) {
        Point p = new Point();
        p.x = Math.round(((point.x / (2f * RENDER)) + .5f) * (width-1));
        p.y = Math.round(((point.z / (2f * RENDER)) + .5f) * (height-1));
        return p;
    }
    
    public Point renderLocationToActualLocationBoard(Vector3f point) {
        Point p = new Point();
        p.x = (int) (((point.x / (2f * BOARDRENDER)) + .5f) * (width));
        p.y = (int) (((point.z / (2f * BOARDRENDER)) + .5f) * (height));
        return p;
    }
    
    @Override
    public Vector3f actualLocationToRenderLocation(Point point) {
        Vector3f p = new Vector3f();
        p.x = ((point.x / (float) (width - 1)) - .5f) * 2f * RENDER;
        p.z = ((point.y / (float) (height - 1)) - .5f) * 2f * RENDER;
        return p;
    }

    @Override
    public Vector3f actualLocationToRenderLocationBoard(Point point) {
        Vector3f p = new Vector3f();
        p.x = ((point.x / (float) (width)) - .5f) * 2f * BOARDRENDER;
        p.z = ((point.y / (float) (height)) - .5f) * 2f * BOARDRENDER;
        return p;
    }

    @Override
    public String getBoardString() {
        return boardTxt;
    }

    @Override
    public Unit getUnitAtLocation(int x, int y) {
        return boardRep[x][y];
    }

    @Override
    public Unit setUnitAtLocation(Unit unit, int x, int y) {
        Unit last = boardRep[x][y]; 
        if (unit.getPoint().x >= 0 || unit.getPoint().y >= 0){
            boardRep[unit.getPoint().x][unit.getPoint().y] = null;   
        }
        if (last != null){
            last.setPoint(new Point(-1, -1)); 
        }
        boardRep[x][y] = unit;
        unit.setPoint(new Point(x, y));
        return last;
    }

}
