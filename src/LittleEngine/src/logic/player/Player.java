package logic.player;

import java.util.ArrayList;
import logic.units.Unit;
import logic.units.King;

public class Player {
    
    private ArrayList<Unit> units;
    private ArrayList<Unit> takenUnits;
    private float cameraSide;
    private int side;
    private King k;
    private Player opponent;
    private String name;
    private String n;
    
    public Player(int side, boolean newName) {
        this.side = side;
        if(side == 1){
            this.cameraSide = 3.1415f * 3/2;
        } else {
            this.cameraSide = 3.1415f / 2;
        }
        this.units = new ArrayList<>();
        this.takenUnits = new ArrayList<>();
        if (newName) {
            if(side == 1){
                n = "1";
            }else{
                n = "2";
            }
            name = "Player" + n;
        }
    }

    public void unitTaken(Unit takenUnit) {
        takenUnits.add(takenUnit);
        units.remove(takenUnit);
    }

    public void unitReleased(Unit releasedUnit) {
        units.add(releasedUnit);
        takenUnits.remove(releasedUnit);
    }
    
     public void clearUnits() {
        units = new ArrayList<>();
        takenUnits = new ArrayList<>();
        k = null;
    }

    public void addUnit(Unit unit) {
        if (unit.getUnitClass().equals("King")){
            k = (King) unit;
        }
        units.add(unit);
    }
    
    public int getSide() {
        return side;
    }
    
    public void setSide(int side) {
        this.side = side;
    }

    public float getCameraSide() {
        return cameraSide;
    }
    
    public void setCameraSide(float cameraSide) {
        this.cameraSide = cameraSide;
    }
    
    public Player getOpponent() {
        return opponent;
    }
    
    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }
    
    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }
    
    public ArrayList<Unit> getTakenUnits() {
        return takenUnits;
    }
    public void setTakenUnits(ArrayList<Unit> takenUnits) {
        this.takenUnits = takenUnits;
    }

    public King getK() {
        return k;
    }
    public void setK(King k) {
        this.k = k;
    }   
	
}
