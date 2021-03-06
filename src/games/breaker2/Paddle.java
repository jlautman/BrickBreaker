package games.breaker2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**Class for a Panel in a BrickBreaker game.
 * */
class Paddle extends JPanel {
    /** Required static variable. */
    private static final long serialVersionUID = 275979669023012230L;

    /**Private parameters for a paddle.
     * */
    private static final int
            XSTART = 500,
            YOFFSET = 60,
            YSTART = BrickBreaker.GAMEHEIGHT - YOFFSET,
            PADDLEWIDTH    = 100,
            PADDLEHEIGHT   =  10,
            PADDLESPEED    =   5;

    /** Variable defining which way the paddle moves. */
    private int direction = 0;

    /**Constructor for a paddle.
     * @param xLoc The X location of the top left corner of the paddle.
     * @param yLoc the Y location of the top left corner of the paddle.
     * */
    public Paddle(final int xLoc, final int yLoc) {
        setLocation(xLoc, yLoc);
        setSize(PADDLEWIDTH, PADDLEHEIGHT);

        // Code necessary to use KeyBindings
        getInputMap().put(KeyStroke.getKeyStroke("a"), "Go Left");
        getInputMap().put(KeyStroke.getKeyStroke("A"), "Go Left");
        getInputMap().put(KeyStroke.getKeyStroke("d"), "Go Right");
        getInputMap().put(KeyStroke.getKeyStroke("D"), "Go Right");
        getInputMap().put(
                KeyStroke.getKeyStroke("released a"), "Clear Direction");
        getInputMap().put(
                KeyStroke.getKeyStroke("released A"), "Clear Direction");
        getInputMap().put(
                KeyStroke.getKeyStroke("released d"), "Clear Direction");
        getInputMap().put(
                KeyStroke.getKeyStroke("released D"), "Clear Direction");

        getActionMap().put("Go Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (direction == 0) direction = -1;
            }
        });
        getActionMap().put("Go Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (direction == 0) direction = 1;
            }
            
        });
        getActionMap().put("Clear Direction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                direction = 0;
            }
        });

//        // Code necessary to use KeyListeners. I should use keybindings since
//        // they're easier to test but I'll work on that with the next iteration.
//        setFocusable(true);
//        this.addKeyListener(new KeyListener() {
//            // Policy: A move key is only used if the paddle is not currently
//            // moving. A move key release always sets the paddle to not move.
//            // If both move keys were pressed at the same time, the first one
//            // to be registered wins. When the other key is released the paddle
//            // stops until keyTyped fires. This stoppage will likely not be
//            // noticeable.
//            @Override
//            public void keyPressed(final KeyEvent arg0) {
//                // Debugging
//                System.out.printf("%c pressed\n", arg0.getKeyChar());
//
//                // Ignore all key presses while the paddle is moving.
//                if (direction != 0) { return; }
//
//                char charEntered = Character.toLowerCase(arg0.getKeyChar());
//                if (charEntered == 'a') { direction = -1; }
//                if (charEntered == 'd') { direction =  1; }
//            }
//
//            @Override
//            public void keyReleased(final KeyEvent arg0) {
//                char charReleased = Character.toLowerCase(arg0.getKeyChar());
//                if (charReleased == 'a' || charReleased == 'd') {
//                    direction = 0;
//                }
//            }
//
//            @Override
//            public void keyTyped(final KeyEvent arg0) { return; }
//        });
//        // End of code for KeyListeners
    }

    /**Simplified constructor for a paddle that uses only default parameters.
     * */
    public Paddle() {
        this(XSTART, YSTART);
    }

    /**Additional tasks for painting a Paddle.
     * @param g The graphics object.
     */
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, PADDLEWIDTH, PADDLEHEIGHT);
    }

    /** Function called by the timer. The update function allows the paddle to
     *  move based on the current state of the keyboard.
     */
    public void update() {
        this.setLocation(getX() + (direction * Paddle.PADDLESPEED), getY());
        // Correct for off screen movement.
        if (getX() < 0) {
            // Too far left
            setLocation(0, getY());
        }
        if (getX() > BrickBreaker.GAMEWIDTH - PADDLEWIDTH) {
            // Too far right
            setLocation(BrickBreaker.GAMEWIDTH - PADDLEWIDTH, getY());
        }
    }

    /** Checks if the Ball in question impacted this paddle and if so, adjusts
     *  the Ball's location and heading.
     *  The ball's velocity gets more "left" if it hits the left side of the
     *  paddle and gets more "right" if it hits the right side of the paddle.
     *  @param ball The Ball to check.
     */
    protected void hitBall(final Ball ball) {
        // If the ball is intersecting the paddle
        if (ball.getY() + (2 * ball.getRadius()) > this.getY() //top
                && ball.getY() < this.getY() + PADDLEHEIGHT //bottom
                && ball.getX() + (2 * ball.getRadius()) > this.getX() //left
                && ball.getX() < this.getX() + PADDLEWIDTH //right
                && ball.getYVelocity() > 0 // going down
                ) {
            ball.setYVelocity(ball.getYVelocity() * -1);
            // This section increments or decrements the Ball's x velocity
            // based on which side of the paddle it hits. This is a weak way to
            // change the velocity of the ball but it works for a first pass
            // solution.
            if ((ball.getX() + ball.getRadius())
                    < (this.getX() + PADDLEWIDTH / 2)) {
                ball.setXVelocity(ball.getXVelocity() - 1);
            } else {
                ball.setXVelocity(ball.getXVelocity() + 1);
            }
        }
    }
}
