package logic.board;

import java.awt.Point;
import logic.units.Unit;

public class Move {
    
    private Point destLoc;
    private Unit unit;
    private Unit takenUnit;
    private Point initLoc;
    private boolean beenUsed;

    public Move(Point destLoc, Unit unit) {
        this.initLoc = new Point(unit.getPoint());
        this.destLoc = destLoc;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object other) {
        Move m = (Move) other;
        return m.initLoc.equals(initLoc) && 
                m.destLoc.equals(destLoc) &&
                m.unit == unit;
    }

    public void makeMove(boolean isAllowed) {
        if (unit.getPoint().equals(destLoc)){
            return;
        }
        beenUsed = unit.getBeenUsed();
        takenUnit = unit.unitToPoint(destLoc, isAllowed);
        if (takenUnit != null){
            takenUnit.getPlayer().unitTaken(takenUnit);	
        }
    }

    public void goBack() {
        if (unit.getPoint().equals(initLoc)) {
            return;
        }
        unit.unitToPoint(initLoc, false);
        unit.setBeenUsed(beenUsed);
        if (takenUnit != null) {
            takenUnit.getPlayer().unitReleased(takenUnit);	
            takenUnit.unitToPoint(destLoc, false);
            takenUnit = null;
        }
    }
    
     public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Object getTakenUnit() {
        return takenUnit;
    }

    public Point getInitLoc() {
        return initLoc;
    }

    public void setInitLoc(Point initLoc) {
        this.initLoc = initLoc;
    }
    
    public Point getDestLoc() {
        return destLoc;
    }
    
    public void setDestLoc(Point destLoc) {
        this.destLoc = destLoc;
    }

    public boolean isBeenUsed() {
        return beenUsed;
    }

    public void setBeenUsed(boolean beenUsed) {
        this.beenUsed = beenUsed;
    }    
    
}
