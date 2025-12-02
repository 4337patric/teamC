package rpsGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 선물 배달 패널 클래스
public class ShootingScene extends JPanel implements ActionListener {
	final int PLAYER = 0, DEALER = 1,DEFAULT = 2, SAVE = 1, DIE = 0,LOSE=3;
	protected ImageIcon[][] Scene;
	static JLabel anime;
	protected Timer timer,timer2;
	private static int name = 0, isNotFired = 0;
	JButton toStageButton;
	
	private Container frame;
	private CardLayout cards;
	
	public void isFired(int userName, int fired) {
		name = userName;
		isNotFired = fired;
	}
	
	public ShootingScene( Container frame, CardLayout cards, String imgPath ) {
		
		this.frame = frame;
		this.cards = cards;
		
		this.setLayout(null);
		
		Scene = new ImageIcon[3][4];
		Scene[PLAYER][DEFAULT] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_default.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[PLAYER][SAVE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_save.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[PLAYER][DIE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_die.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[PLAYER][LOSE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_lose.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][DEFAULT] = new ImageIcon(new ImageIcon(imgPath + "dealerShoot_default.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][SAVE] = new ImageIcon(new ImageIcon(imgPath + "dealerShoot_save.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][DIE] = new ImageIcon(new ImageIcon(imgPath + "dealerShoot_die.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][LOSE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_win.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));

		anime = new JLabel();
		toStageButton = new JButton("스테이지 목록으로");
		toStageButton.addActionListener(this);
		this.add(toStageButton);
		this.add(anime);
		
		
		toStageButton.setBorderPainted(false); 
		toStageButton.setContentAreaFilled(false);
		toStageButton.setFocusPainted(false);
		toStageButton.setFont(new Font("바탕",Font.BOLD,30));
		toStageButton.setForeground(Color.WHITE);
		toStageButton.setVisible(false);
		
		toStageButton.setBounds(200,800,300,100);
		anime.setBounds(0, 0, 700, 1000);
		
		// 선물은 주기적으로 조금씩 낙하
		this.timer = new Timer( 1000, new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				anime.setIcon(Scene[name][isNotFired]);
				timer.stop();
				timer2.start();
			}
		} );
		this.timer2 = new Timer( 1000, new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				if(isNotFired == 0) {
					anime.setIcon(Scene[name][LOSE]);
					toStageButton.setVisible(true);
					timer2.stop();
					return;
				}
				timer2.stop();
				cards.show(frame, "GAME");
				
			}
		} );
	}
	
	@Override
	public void actionPerformed( ActionEvent event ) {
		toStageButton.setVisible(false);
		cards.show(frame, "STAGE");
		
	}


	public void ShowShooting() {
		anime.setIcon(Scene[name][DEFAULT]);
		timer.start();
	}
}

