package brickbreakgame;

import javax.swing.JFrame;

public class BrickBreakFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	public BrickBreakFrame() {
		this.setTitle("Brick Breaker Game *-^");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		BrickBreakPanel brickBreakPanel = new BrickBreakPanel();
		this.add(brickBreakPanel);
		this.pack();
		this.setResizable(false);
		this.setVisible(true);
		this.setFocusable(true);
		brickBreakPanel.requestFocusInWindow();
	}

}
