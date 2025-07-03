package com.zetcode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int score;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean paused = false;

    private Timer timer;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        addKeyListener(new TAdapter());
        initGame();
    }

    private void initGame() {
        dots = 3;
        score = 0;
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
        inGame = true;
        paused = false;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;
            y[z] = 50;
        }

        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (inGame) {
            g.setColor(Color.RED);
            g.fillOval(apple_x, apple_y, DOT_SIZE, DOT_SIZE);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.YELLOW);
                }
                g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
            }

            // Score display
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Score: " + score, 10, 20);

            if (paused) {
                g.drawString("Paused (P)", B_WIDTH / 2 - 40, B_HEIGHT / 2);
            }

        } else {
            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {
        String msg1 = "Game Over";
        String msg2 = "Final Score: " + score;
        String msg3 = "Press Enter to Restart";

        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 14));

        FontMetrics metr = getFontMetrics(g.getFont());

        g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2, B_HEIGHT / 2 - 20);
        g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2, B_HEIGHT / 2);
        g.drawString(msg3, (B_WIDTH - metr.stringWidth(msg3)) / 2, B_HEIGHT / 2 + 20);
    }

    private void checkApple() {
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score += 10;
            locateApple();
        }
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1];
            y[z] = y[z - 1];
        }

        if (leftDirection) x[0] -= DOT_SIZE;
        if (rightDirection) x[0] += DOT_SIZE;
        if (upDirection) y[0] -= DOT_SIZE;
        if (downDirection) y[0] += DOT_SIZE;
    }

    private void checkCollision() {
        for (int z = 1; z < dots; z++) {
            if ((x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (x[0] < 0 || x[0] >= B_WIDTH || y[0] < 0 || y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (!inGame) timer.stop();
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = r * DOT_SIZE;
        r = (int) (Math.random() * RAND_POS);
        apple_y = r * DOT_SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame && !paused) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                leftDirection = false;
                rightDirection = false;
            }

            if (!inGame && key == KeyEvent.VK_ENTER) {
                initGame(); // Restart
            }

            if (key == KeyEvent.VK_P) {
                paused = !paused; // Pause toggle
            }
        }
    }
}
