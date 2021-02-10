package logic.units;

import logic.board.Move;
import java.awt.Point;
import java.util.ArrayList;
import logic.board.Board;
import logic.player.Player;
import org.lwjgl.util.vector.Vector3f;

public abstract class Unit{
    
    protected boolean beenUsed;
    protected Player p;
    protected Board board;
    protected Point point;
    private int index;
    private float xRender;
    private float yRender;
   

    public Unit(int x, int y, Board board, Player p) {
        this.point = new Point(x, y);
        Vector3f drawLoc = board.actualLocationToRenderLocation(point);
        this.xRender = drawLoc.x;
        this.yRender = drawLoc.z;
        this.board = board;
        this.p = p;
        this.board.setUnitAtLocation(this, x, y);
        this.p.addUnit(this);
        this.beenUsed = false;
    }
    
    public abstract ArrayList<Move> allMoves();

    public ArrayList<Move> attackMoves() {
        ArrayList<Move> mList = new ArrayList<>();
        allMoves().stream().filter((m) -> 
                (board.containsOpponentUnit(m.getDestLoc(), p))).forEachOrdered((m) -> {
            mList.add(m);
        });                     
        return mList;
    }

    public ArrayList<Move> allowedMoves() {
        ArrayList<Move> mList = new ArrayList<>();
        allMoves().stream().filter((m) -> 
                (board.isAllowedMove(m))).forEachOrdered((m) -> {
            mList.add(m);
        });
        return mList;
    }

    public Unit unitToPoint(Point point, boolean isAllowed) {
        if (!isAllowed || allowedMoves().contains(new Move(point, this))) {
            if (isAllowed){
                beenUsed = true;   
            }                   
            Vector3f renderPoint = board.actualLocationToRenderLocation(point);
            xRender = renderPoint.x;
            yRender = renderPoint.z;
            return board.setUnitAtLocation(this, point.x, point.y);
        }
        throw new IllegalArgumentException("Invalid Point");
    }

    public int getIndex(){
        return index;
    }
    
    public void setIndex(int i){
        index = i;
    }
    
    public float getxRender(){
        return xRender;
    }
    
    public void setxRender(float xRender) {
        this.xRender = xRender;
    }
    
    public float getyRender(){
        return yRender;
    }
    
    public void setyRender(float yRender) {
        this.yRender = yRender;
    }
    
    public Player getPlayer() { 
        return p;
    }
    
    public void setPlayer(Player p) {
        this.p = p;
    }
    
    public Point getPoint() {
        return point;
    }

    public void setPoint(Point _location) {
        point.setLocation(_location);
    }
    
    public boolean getBeenUsed() {
        return beenUsed;
    }

    public void setBeenUsed(boolean beenUsed) {
        this.beenUsed = beenUsed;
    }
    
    public abstract int getValue();
    
    public abstract String getUnitClass();
	
}
