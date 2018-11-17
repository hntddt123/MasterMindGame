package mastermindgame.gui;

import javax.swing.*;
import java.awt.*;

public class MasterMindGameColorCell extends JButton {
    public final int row;
    public final int col;

    public MasterMindGameColorCell(int rowIndex, int colIndex) {
        row = rowIndex;
        col = colIndex;

        setPreferredSize(new Dimension(50, 50));
    }

}
