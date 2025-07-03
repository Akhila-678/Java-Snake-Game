package com.zetcode;

import javax.swing.JFrame;

public class Snake extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Snake() {
        initUI();
    }

    private void initUI() {
        add(new Board());
        setResizable(false);
        pack();
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Snake ex = new Snake();
            ex.setVisible(true);
        });
    }
}
