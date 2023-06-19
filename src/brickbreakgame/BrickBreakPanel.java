package brickbreakgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class BrickBreakPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private final static int GAME_HEIGHT = 300;
	private final static int GAME_WIDTH = 350;
	private final static int GAME_UNIT_SIZE = 10;
	private static final int DELAY = 1;
	private final static int BAR_SIZE = 5 * GAME_UNIT_SIZE;

	int barX = (GAME_WIDTH + BAR_SIZE) / 2;
	private final static int barY = GAME_HEIGHT - GAME_UNIT_SIZE;

	List<BricksCordinations> bricks; 
	Timer timer;
	Random random;
	int bricksBroken;

	int ballX;
	int ballY;

	int xVilocity = 1;
	int yVilocity = -1;
	
	int score;

	boolean running = false;

	public BrickBreakPanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
		this.setBackground(Color.black);
		this.addKeyListener(new MyKeyAdapter());
		this.setFocusable(true);
		
		gameStart();
	}

	public void gameStart() {
		score = 0;
		ballX = random.nextInt(GAME_WIDTH);
		ballY = GAME_HEIGHT - 4 * GAME_UNIT_SIZE;
		bricks = defBricksCordinationsList();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	private void draw(Graphics g) {
		// draw Units
/*		g.setColor(Color.GRAY);
 		for (int i = 0; i < GAME_WIDTH; i++)
 			g.drawLine(i * GAME_UNIT_SIZE, 0, i * GAME_UNIT_SIZE, GAME_HEIGHT);
 		for (int i = 0; i < GAME_HEIGHT; i++) 
  			g.drawLine(0, i * GAME_UNIT_SIZE, GAME_WIDTH, i * GAME_UNIT_SIZE);*/

		if(running) {
			g.setColor(Color.white);
			g.fillRect(barX, barY, BAR_SIZE, GAME_UNIT_SIZE);
			
			g.setColor(Color.red);
			g.fillOval(ballX, ballY, GAME_UNIT_SIZE, GAME_UNIT_SIZE);
			
			g.setColor(Color.white);
			bricks.stream().forEach(( brick ) -> g.fillRect(brick.getBrickX(), brick.getBrickY(), GAME_UNIT_SIZE, GAME_UNIT_SIZE));
			
			g.setColor(Color.black);
			bricks.stream().forEach(( brick ) -> g.drawRect(brick.getBrickX(), brick.getBrickY(), GAME_UNIT_SIZE, GAME_UNIT_SIZE));
		}else
			gameEnd(g);

	}

	public void ballMovment() {
		if (ballX < 0 || ballX > GAME_WIDTH - GAME_UNIT_SIZE)
			xVilocity = xVilocity * -1;
		if (isBallTouchedABrick(ballX, ballY) || ballY < 0)
			yVilocity = yVilocity * -1;
		if (ballY == GAME_HEIGHT - (2 * GAME_UNIT_SIZE))
			if (ballX >= barX && ballX <= (barX + BAR_SIZE)) 
				yVilocity = yVilocity * -1;
				
			 else 
				running = false;		
	}

	public void move() {
		ballMovment();
		ballY += yVilocity;
		ballX += xVilocity;
	}

	public void gameEnd(Graphics g) {
		g.setFont(new Font("ARIAL", Font.BOLD, 55));
		FontMetrics metrics = g.getFontMetrics();
		if(!bricks.isEmpty()) {
			g.setColor(Color.red);
			g.drawString("Game Over", (GAME_WIDTH - metrics.stringWidth("Game Over")) / 2, GAME_HEIGHT / 2);
		}else {
			
			g.setColor(Color.GREEN);
			g.drawString("YOU WIN", (GAME_WIDTH - metrics.stringWidth("YOU WIN")) / 2, GAME_HEIGHT / 4);
		
		}

		g.setColor(Color.gray);
		g.setFont(new Font("ROBOTO", Font.ITALIC, 30));
		metrics = g.getFontMetrics();
		g.drawString("Score: " + score, (GAME_WIDTH - metrics.stringWidth("Score: " + score)) / 2, GAME_HEIGHT * 3 / 4);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(bricks.isEmpty())
			running = false;
		if (running) {
			move();
			repaint();
		}
	}

	private boolean isBallTouchedABrick(int ballX, int ballY) {
		boolean isTouched = false;
		
		for (var brick : this.bricks)
			if (ballX >= brick.brickX && ballX < (brick.brickX + GAME_UNIT_SIZE) 
			&& ballY >= brick.brickY && ballY < (brick.brickY + GAME_UNIT_SIZE)) {
				isTouched = true;
				score++;
			}
		bricks.removeIf(
				(brick) -> 
					ballX >= brick.brickX && ballX < (brick.brickX + GAME_UNIT_SIZE) &&
					ballY >= brick.brickY && ballY < (brick.brickY + GAME_UNIT_SIZE));
		return isTouched;

	}
	
	public List<BricksCordinations> defBricksCordinationsList() {
		List<BricksCordinations> brickList = new ArrayList<>();
		for(int i = 0; i < GAME_WIDTH; i+= GAME_UNIT_SIZE) {
			
			for(int j = 0; j < 6 * GAME_UNIT_SIZE; j+= GAME_UNIT_SIZE) {
				
				brickList.add(new BricksCordinations(i, j));
			}
		}
		
		return brickList;
	}

	private class BricksCordinations {

		private int brickX;
		private int brickY;

		BricksCordinations(int brickX, int brickY) {
			this.brickX = brickX;
			this.brickY = brickY;
		}

		public int getBrickX() {
			return brickX;
		}
		public int getBrickY() {
			return brickY;
		}

	}


	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (barX > 0) {
					barX = barX - GAME_UNIT_SIZE;
				break;
			}
			case KeyEvent.VK_RIGHT: 
				if (barX < GAME_WIDTH - BAR_SIZE) {
					barX = barX + GAME_UNIT_SIZE;
				break;
			}
			}

		}
	}

}
