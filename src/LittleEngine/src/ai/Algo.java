
package ai;

import logic.board.Move;
import mainGame.LogicLoop;

public interface Algo {
    Move execute(LogicLoop gl);
}
