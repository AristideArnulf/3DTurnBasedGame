package logic.gametype;

import javax.swing.JOptionPane;
import logic.board.Move;
import logic.board.Board;
import logic.board.RegularBoard;
import logic.units.King;
import logic.units.Gnome;
import logic.units.Knight;
import mainGame.LogicLoop;
import logic.player.Player;

public class RegularGame implements GameType {

    @Override
    public Board startNewGame(Player p1, Player p2) {
        RegularBoard regular = new RegularBoard(this);
        
        // Player 1
        new King(4, 0, regular,p1);
        new Knight(3, 0, regular,p1);
        new Knight(5, 0, regular,p1);
        new Gnome(3, 1, regular,p1);
        new Gnome(4, 1, regular,p1);
        new Gnome(5, 1, regular,p1);
        
        // Player 2
        new King(3, 7, regular,p2);
        new Knight(2, 7, regular ,p2);
        new Knight(4, 7, regular,p2);
        new Gnome(2, 6, regular,p2);
        new Gnome(3, 6, regular,p2);
        new Gnome(4, 6, regular,p2);
        
        return regular;
    }	

    @Override
    public boolean isValidBoard(Board board, Player p, Move m) {
        return !board.isEatable(p.getK().getPoint(), p);
    }

    @Override
    public boolean gameOver(Board board, Player p) {
        return board.isEatable(p.getK().getPoint(), p);
    }

    @Override
    public void inCheckWarning(final LogicLoop gameLoop, Move m) {
        final Player playing = gameLoop.getPlaying();
        if (gameLoop.getBoard().isEatable(playing.getK().getPoint(), playing)) {
            Thread thread = new Thread(() -> {
                JOptionPane.showMessageDialog(null, "Check!");
            });
            thread.start();
        }	
    }
	
}
