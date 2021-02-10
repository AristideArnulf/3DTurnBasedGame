package logic.gametype;

import logic.board.Move;
import logic.board.Board;
import logic.player.Player;
import mainGame.LogicLoop;

public interface GameType {

    Board startNewGame(Player p1, Player p2);
    void inCheckWarning(final LogicLoop logicLoop, Move m);
    boolean isValidBoard(Board board, Player p, Move m);
    boolean gameOver(Board board, Player p);
	
}
