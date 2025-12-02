// JAVA 프로그래밍 - https://codereading101.github.io/JAVA/
// 소스파일 - https://github.com/CodeReading101/JAVA/blob/main/src/rpsGame/RockPaperScissorsPanel.java

package rpsGame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 가위바위보 클래스
public class RockPaperScissorsPanel extends JPanel implements ActionListener
{
	protected final int PLAYERGUN = 0, DEALERGUN = 1,CARD = 2, BACKGROUND = 3, DEALERCARD = 4, DEFAULT = 5,BULLETA =6,BULLETB = 7;
	protected JLabel imgPlayerA, imgPlayerB, strSumAndTarget,backgroung,dealerCard;
	protected JLabel[] playerBullet, dealerBullet;
	protected JButton[] button;
	protected ImageIcon[] image,imageCard,imageBig;
	protected Timer timer,timer2,timer3;
	
	private Container frame;
	private CardLayout cards;
	
	static GameManager gm = new GameManager();
    static Player player = new Player(gm);
    static Dealer dealer = new Dealer(gm);
    protected ShootingScene shoot;
    private static int isNotFired = 1;
    int weitTime = 1500,currentUser = PLAYERGUN;
	
	// 가위바위보 초기화
	public RockPaperScissorsPanel(Container frame, CardLayout cards, final String imgPath,ShootingScene shoot ) {
	
		
		this.frame = frame;
		this.cards= cards;
		this.shoot = shoot;
		
		gm.player = player;
	    gm.dealer = dealer;
	    gm.startGame();
	    
	    
		// 가위바위보 3가지, 미선택 1가지, 승패 2가지를 고려하여 이미지를 준비
		image = new ImageIcon[8];
		image[CARD] = new ImageIcon( new ImageIcon( imgPath + "cardDesign.png"    ).getImage().getScaledInstance( 190, 280, Image.SCALE_SMOOTH ) );
		image[BACKGROUND] = new ImageIcon( new ImageIcon( imgPath + "background.png"     ).getImage().getScaledInstance( 700, 1000, Image.SCALE_SMOOTH ) );
		image[DEALERCARD] = new ImageIcon( new ImageIcon( imgPath + "dealerCard.png"     ).getImage().getScaledInstance( 500, 600, Image.SCALE_SMOOTH ) );
		image[DEFAULT] = null;
		image[BULLETA] = new ImageIcon( new ImageIcon( imgPath + "playerBullet.png"     ).getImage().getScaledInstance( 80, 100, Image.SCALE_SMOOTH ) );
		image[BULLETB] = new ImageIcon( new ImageIcon( imgPath + "dealerBullet.png"     ).getImage().getScaledInstance( 80, 100, Image.SCALE_SMOOTH ) );

		
		this.playerBullet = new JLabel[3];
		this.dealerBullet = new JLabel[3];
		for(int i =0; i<3;i++) {
			playerBullet[i] = new JLabel(image[BULLETA]);
			dealerBullet[i] = new JLabel(image[BULLETB]);
			this.add(playerBullet[i]);
			this.add(dealerBullet[i]);
		}

   
		// 화면 위쪽에 가위바위보 이미지 초기화
		this.imgPlayerA = new JLabel( image[DEFAULT]);
		this.imgPlayerB = new JLabel( image[DEFAULT]);
		this.strSumAndTarget = new JLabel("<html><body>"+" 목표: "+Integer.toString(gm.targetNumber)+"<br><br>"+" 현재: "+Integer.toString(gm.currentSum)+"</body></html>", JLabel.CENTER );
		this.strSumAndTarget.setFont(new Font("고딕",Font.BOLD,20));
		this.strSumAndTarget.setForeground(Color.WHITE);
		this.add( this.imgPlayerB );
		this.add( this.strSumAndTarget );
		this.add( this.imgPlayerA );
		imgPlayerA.setFont(new Font("바탕", Font.BOLD, 40));			
		imgPlayerA.setHorizontalTextPosition(JButton.CENTER);
		imgPlayerB.setFont(new Font("바탕", Font.BOLD, 40));			
		imgPlayerB.setHorizontalTextPosition(JButton.CENTER);
		// 화면 아래쪽에 가위바위보 입력 버튼 초기화
		button = new JButton[5];
		for(int i =0;i<5;i++) {
			button[i] = new JButton( new ImageIcon( image[CARD].getImage().getScaledInstance( 110, 170, Image.SCALE_SMOOTH ) ) );
			button[i].addActionListener( this );
			//버튼과 이미지 크기 일치, 이미지 위에 텍스트 출력하기(AI활용함)
			button[i].setBorderPainted(false); 
			button[i].setContentAreaFilled(false);
			button[i].setFocusPainted(false);
			button[i].setMargin(new Insets(0, 0, 0, 0));
			button[i].setFont(new Font("바탕", Font.BOLD, 20));			
			button[i].setHorizontalTextPosition(JButton.CENTER);
		}
		
		for(int i =0;i<5;i++) {
			button[i].setText(cardString(player.cardArray[i]));
			this.add(button[i]);
		}
		
		
		//이미지 위치,크기 조정
		this.setLayout(null);
		this.setBackground( Color.white );
		this.setPreferredSize( new Dimension(700, 1000) );
		imgPlayerA.setBounds(150, 400, 150, 200);
		imgPlayerB.setBounds(402, 400, 150, 200);
		strSumAndTarget.setBounds(300, 445, 100, 80);
		for(int i =0;i<5;i++) {
			button[i].setBounds(105+100*i, 750, 90, 120);;
		}
		for(int i = 0;i<3;i++) {
			playerBullet[i].setBounds(450,600,90+45*i,120);
			dealerBullet[i].setBounds(120,290,90+45*i,120);
		}
		
		//배경이미지
		this.dealerCard = new JLabel(image[DEALERCARD]);
		this.backgroung = new JLabel(image[BACKGROUND]);
		dealerCard.setBounds(100, 100, 500, 200);
		backgroung.setBounds(0,0,700,1000);
		this.add(dealerCard);
		this.add(backgroung);
		
		// 가위바위보 준비
		this.timer = new Timer( 1000, new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				
				if(gm.currentSum>=gm.targetNumber) {
					gameEnd();
					timer.stop();
					return;
				}
				computerTurn();
				timer.stop();

			}
		});
		this.timer2 = new Timer( 1000, new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				
				if(gm.currentSum>=gm.targetNumber) {
					gameEnd();
				}
				ready();
				timer2.stop();

			}
		});
		
		this.timer3 = new Timer( 2700, new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				
				computerTurn();
				timer3.stop();

			}
		});
		
	}
	
	void displayNumber() {
		strSumAndTarget.setText("<html><body>"+" 목표: "+Integer.toString(gm.targetNumber)+"<br><br>"+" 현재: "+Integer.toString(gm.currentSum)+"</body></html>");
	}
	
	// 물음표 제시하고 버튼 활성화
	public void ready() {
		imgPlayerA.setIcon( image[DEFAULT] );
		imgPlayerB.setIcon( image[DEFAULT] );
		imgPlayerA.setText("");
		imgPlayerB.setText("");
		for(int i = 0;i<3;i++) {
			playerBullet[i].setVisible(true);
			dealerBullet[i].setVisible(true);
			if(i<3-player.bulletCount) {
				playerBullet[i].setVisible(false);
			}
			if(i>dealer.bulletCount-1) {
				dealerBullet[i].setVisible(false);
			}
		}
		
		for(int i =0;i<5;i++) {			
			button[i].setEnabled(true);
			button[i].setText(cardString(player.cardArray[i]));			
		}
		displayNumber();
		
	}
	// 가위바위보 내기
	@Override
	public void actionPerformed( ActionEvent event ) {
		// 승패 결과 제시
		
		for(int i =0;i<5;i++) {
			button[i].setEnabled(false);
		}
		currentUser = PLAYERGUN;
		int playerA = select( event );
		imgPlayerA.setIcon(image[CARD]);			
		imgPlayerA.setText(cardString(player.cardArray[playerA]));
		player.playerTurn(playerA);
		displayNumber();
		//show( playerA,"player",imgPlayerA);
		timer.start();
		
		// 다음 판은 잠시 대기
		
	}
	
	public void computerTurn() {
		
		currentUser = DEALERGUN;
		int playerB = dealer.dealerTurn();
		//show(playerB,"dealer",imgPlayerB);
		imgPlayerB.setIcon(image[CARD]);
		imgPlayerB.setText(cardString(playerB));
		displayNumber();
		timer2.start();

	}
	
	public String cardString(int cardNumber) {
		if(cardNumber == -12) {
			return "÷";
		}
		else if(cardNumber == -13) {
			return "x";
		}
		else return Integer.toString(cardNumber);
	}
	
	public int select( ActionEvent event ) {
		
		for(int i = 0; i<5;i++) {
			if(event.getSource() == button[i]) {
				return i;
			}
		}
		return 0;
		
	}
	
	public void gameEnd() {
		if (currentUser == PLAYERGUN) {
			
			isNotFired = player.fireBullet();
			
			shoot.isFired(PLAYERGUN, isNotFired);
			shoot.ShowShooting();			
			imgPlayerA.setText("");
			imgPlayerB.setText("");
			imgPlayerA.setIcon( image[DEFAULT] );
			imgPlayerB.setIcon( image[DEFAULT] );
			for(int i = 0;i<3;i++) {
				playerBullet[i].setVisible(true);
				if(i<3-player.bulletCount) {
					playerBullet[i].setVisible(false);
				}
			}
			cards.show(frame, "SHOOT");
			
			if(isNotFired == 1) {
				timer3.start();
			}
						
		}
		else {
			
			isNotFired = dealer.fireBullet();
			
			shoot.isFired(DEALERGUN, isNotFired);
			shoot.ShowShooting();
			imgPlayerA.setText("");
			imgPlayerB.setText("");
			imgPlayerA.setIcon( image[DEFAULT] );
			imgPlayerB.setIcon( image[DEFAULT] );
			for(int i = 0;i<3;i++) {
				dealerBullet[i].setVisible(true);
				if(i>dealer.bulletCount-1) {
					dealerBullet[i].setVisible(false);
				}
			}
			cards.show(frame, "SHOOT");
			
		}
		
		if(isNotFired == 0) {
			gm.startGame();
			ready();
			return;
		}
		
		gm.startRound();
		displayNumber();

	}
}

