package rpsGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 총 발사 이미지 패널 클래스
public class ShootingScene extends JPanel implements ActionListener {
	final int PLAYER = 0, DEALER = 1,DEFAULT = 2, SAVE = 1, DIE = 0,LOSE=3;
	protected ImageIcon[][] Scene;
	static JLabel anime;
	protected Timer timer,timer2;
	private static int name = 0, isNotFired = 0;
	JButton toStageButton;
	
	private Container frame;
	private CardLayout cards;
	
	//현재 총격 당하는 사람, 총알이 발사 되었는지 전달는 함수
	public void isFired(int userName, int fired) {
		name = userName;
		isNotFired = fired;
	}
	
	//ShootingScene 초기화
	public ShootingScene( Container frame, CardLayout cards, String imgPath ) {
		
		this.frame = frame;
		this.cards = cards;
		
		//이미지의 크기 위치를 직접 지정해서 배치
		this.setLayout(null);
		
		//각 사람별로 기본, 발사(죽음,생존), 승패 이미지 준비
		Scene = new ImageIcon[3][4];
		Scene[PLAYER][DEFAULT] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_default.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[PLAYER][SAVE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_save.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[PLAYER][DIE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_die.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[PLAYER][LOSE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_lose.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][DEFAULT] = new ImageIcon(new ImageIcon(imgPath + "dealerShoot_default.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][SAVE] = new ImageIcon(new ImageIcon(imgPath + "dealerShoot_save.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][DIE] = new ImageIcon(new ImageIcon(imgPath + "dealerShoot_die.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));
		Scene[DEALER][LOSE] = new ImageIcon(new ImageIcon(imgPath + "playerShoot_win.png").getImage().getScaledInstance(700, 1000, Image.SCALE_SMOOTH));

		//이미지담을 레이블, 스테이지 버튼 준비
		anime = new JLabel();
		toStageButton = new JButton("스테이지 목록으로");
		toStageButton.addActionListener(this);
		this.add(toStageButton);
		this.add(anime);
		
		//버튼에 텍스트만 남겨놓기
		toStageButton.setBorderPainted(false); 
		toStageButton.setContentAreaFilled(false);
		toStageButton.setFocusPainted(false);
		toStageButton.setFont(new Font("바탕",Font.BOLD,30));
		toStageButton.setForeground(Color.WHITE);
		toStageButton.setVisible(false);
		
		//구성요소 크기 위치 지정
		toStageButton.setBounds(200,800,300,100);
		anime.setBounds(0, 0, 700, 1000);
		
		// 기본->발사 이미지로 시간 간격두고 넘어가며 연출
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
				//총이 발사되었다면 승패와 스테이지 버튼 출력
				if(isNotFired == 0) {
					anime.setIcon(Scene[name][LOSE]);
					toStageButton.setVisible(true);
					timer2.stop();
					return;
				}
				//아니면 다시 게임 패널로
				timer2.stop();
				cards.show(frame, "GAME");
				
			}
		} );
	}
	
	//스테이지 버튼
	@Override
	public void actionPerformed( ActionEvent event ) {
		toStageButton.setVisible(false);
		cards.show(frame, "STAGE");
		
	}

	//게임 패널에서 호출해서 총 발사 이미지 출력 시작
	public void ShowShooting() {
		anime.setIcon(Scene[name][DEFAULT]);
		timer.start();
	}
}

