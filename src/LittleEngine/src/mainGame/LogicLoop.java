package mainGame;

import java.util.Stack;
import javax.swing.JOptionPane;
import logic.board.Board;
import logic.board.Move;
import logic.gametype.GameType;
import logic.gametype.RegularGame;
import logic.player.Player;
import logic.units.Unit;


public class LogicLoop {
    
    Player p1;
    Player p2;
    Player playing;
    Board b;
    GameType gameType;
    Stack<Move> moveStack;
    Unit chosenUnit;
    boolean inGame; 
    
    public LogicLoop(){
        this.p1 = new Player(1, true);
        this.p2 = new Player(-1, true);
        this.p1.setOpponent(p2);
        this.p2.setOpponent(p1);
        this.moveStack = new Stack<>();
        this.playing = this.p1;
        this.gameType = new RegularGame();
        this.p1.clearUnits();
        this.p2.clearUnits();
        this.b = this.gameType.startNewGame(p1, p2);
        this.chosenUnit = null;
        this.inGame = false;
    }
    
    public void makeMove(Move m) {
        if (!inGame){
            return;
        }
        moveStack.push(m);
        m.makeMove(true);
        playing = playing.getOpponent();
        boolean canPlayerMove = b.checkIfPlayerCanMove(playing);
        if (canPlayerMove) {
            if (gameType.gameOver(b, playing)){
                inGame = false;
                JOptionPane.showMessageDialog(null, "Game Over");
                System.exit(0);
            }else{
                inGame = false;
                System.exit(0);
            }
            return;
        }
        gameType.inCheckWarning(this, m);
    }
    
   
    
    public Unit getChosenUnit() {
        return chosenUnit;
    }
    
    public void setChosenUnit(Unit unit) {
        chosenUnit = unit;
    }
    
    public Board getBoard() {
        return b;
    }
    
    public void setBoard(Board b) {
        this.b = b;
    }

    public Player getPlaying() {
        return playing;
    }
    
    public void setPlaying(Player playing) {
        this.playing = playing;
    }
    
     public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Stack<Move> getMoveStack() {
        return moveStack;
    }

    public void setMoveStack(Stack<Move> moveStack) {
        this.moveStack = moveStack;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public Player getP1() {
        return p1;
    }
    
    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
         return p2;
    }
    
    public void setP2(Player p2) {
        this.p2 = p2;
    }
}
