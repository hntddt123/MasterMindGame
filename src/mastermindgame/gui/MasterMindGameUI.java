package mastermindgame.gui;

import mastermindgame.MasterMindGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MasterMindGameUI extends JFrame {

    private static final int COLUMNSIZE = 6;
    private static final int ROWSIZE = 20;

    private int turns = 0;
    private Color[] colors;

    private MasterMindGame masterMindGame;
    private List<Integer> selectedColorIndices;
    private List<Integer> guessIndices;
    private Map<MasterMindGame.Result, Integer> resultIntegerMap;

    private JPanel jPanelBottom;
    private JPanel jPanelScrollPaneContainer;
    private JScrollPane jScrollPane;

    private ArrayList<ArrayList<MasterMindGameColorCell>> userColorCell;
    private ArrayList<ArrayList<MasterMindGameColorCell>> matchCell;

    public static void main(String[] args) {
        JFrame frame = new MasterMindGameUI();
        frame.setTitle("MasterMindGame");
        frame.setSize(800, 480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    protected void frameInit() {
        super.frameInit();
        initPanel();

        setLayout(new BorderLayout());
        setResizable(false);
        setColors();
        setPanelPreferredSize();
        addPanelToFrame();

        startGame();

        guessIndices = new ArrayList<>();
        userColorCell = new ArrayList<>();
        matchCell = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < ROWSIZE; rowIndex++) {
            for (int colIndex = 0; colIndex < COLUMNSIZE; colIndex++) {
                addButtonRowCell(userColorCell, rowIndex, colIndex, Color.WHITE);
            }

            jPanelScrollPaneContainer.add(Box.createHorizontalStrut(100));

            for (int colIndex = 0; colIndex < COLUMNSIZE; colIndex++) {
                addButtonRowCell(matchCell, rowIndex, colIndex, Color.WHITE);
            }
        }
        
        for (int colIndex = 0; colIndex < 10; colIndex++) {
            addColorIndexCell(0, colIndex);
        }

    }

    private class colorCellClickedHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            MasterMindGameColorCell colorCell = (MasterMindGameColorCell) actionEvent.getSource();
            if (guessIndices.size() < 6 && masterMindGame.gameStatus() == MasterMindGame.Status.INPROGRESS) {
                userColorCell.get(turns).get(guessIndices.size()).setBackground(colors[colorCell.col]);
                guessIndices.add(colorCell.col);
            }

            if (guessIndices.size() == 6 && masterMindGame.gameStatus() == MasterMindGame.Status.INPROGRESS) {
                resultIntegerMap = masterMindGame.guess(guessIndices);
                int inPosition = resultIntegerMap.get(MasterMindGame.Result.INPOSITION);
                int match = resultIntegerMap.get(MasterMindGame.Result.MATCH);

                for (int index = 0; index < inPosition; index++) {
                    matchCell.get(turns).get(index).setBackground(Color.BLACK);
                }

                for (int index = inPosition; index < inPosition + match; index++) {
                    matchCell.get(turns).get(index).setBackground(Color.LIGHT_GRAY);
                }
                turns++;
                guessIndices.clear();
            }
        }
    }

    private void initPanel() {
        jPanelScrollPaneContainer = new JPanel(new FlowLayout());
        jScrollPane = new JScrollPane(jPanelScrollPaneContainer);
        jPanelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
    }

    private void setPanelPreferredSize() {
        jPanelBottom.setPreferredSize(new Dimension(500, 80));
        jPanelScrollPaneContainer.setPreferredSize(new Dimension(200, 1200));
    }

    private void addPanelToFrame() {
        getContentPane().add(jScrollPane, BorderLayout.CENTER);
        getContentPane().add(jPanelBottom, BorderLayout.SOUTH);
    }

    private void setColors() {
        colors = new Color[] {
                Color.RED,
                Color.GREEN,
                Color.BLUE,
                Color.ORANGE,
                Color.GRAY,
                Color.MAGENTA,
                Color.YELLOW,
                Color.CYAN,
                Color.PINK,
                Color.LIGHT_GRAY
        };
    }

    private void startGame() {
        masterMindGame = new MasterMindGame();
        selectedColorIndices = masterMindGame.selectRandomDistinctColorIndices(10, 6, 1);
        masterMindGame.setColorCombinationIndices(selectedColorIndices);
    }

    private void addButtonRowCell(ArrayList<ArrayList<MasterMindGameColorCell>> listCell, int rowIndex, int colIndex, Color color) {
        ArrayList<MasterMindGameColorCell> rowCell = new ArrayList<>();
        listCell.add(rowCell);
        listCell.get(rowIndex).add(new MasterMindGameColorCell(rowIndex, colIndex));
        listCell.get(rowIndex).get(colIndex).setOpaque(true);
        listCell.get(rowIndex).get(colIndex).setBorderPainted(false);
        listCell.get(rowIndex).get(colIndex).setBackground(color);
        jPanelScrollPaneContainer.add(listCell.get(rowIndex).get(colIndex));
    }

    private void addColorIndexCell(int rowIndex, int colIndex){
        MasterMindGameColorCell colorIndexCell = new MasterMindGameColorCell(rowIndex, colIndex);
        colorIndexCell.setOpaque(true);
        colorIndexCell.setBorderPainted(false);
        colorIndexCell.setBackground(colors[colIndex]);
        colorIndexCell.addActionListener(new colorCellClickedHandler());
        jPanelBottom.add(colorIndexCell);
    }
}