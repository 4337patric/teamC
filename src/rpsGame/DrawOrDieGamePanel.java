// JAVA 프로그래밍 - https://codereading101.github.io/JAVA/
// 소스파일 - https://github.com/CodeReading101/JAVA/blob/main/src/rpsGame/RockPaperScissorsPanel.java

package rpsGame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 가위바위보 클래스
public class DrawOrDieGamePanel extends JPanel implements ActionListener
{
	
	protected final int PLAYERGUN = 0, DEALERGUN = 1,CARD = 2, BACKGROUND = 3, DEALERCARD = 4, DEFAULT = 5,BULLETA =6,BULLETB = 7;
	//플레이어 제시 카드, 딜러 제시 카드, 목표,현재 숫자, 배경, 딜러 쪽 테이블에 뒷면 카드 그림
	protected JLabel imgPlayerA, imgPlayerB, strSumAndTarget,backgroung,dealerCard;
	//플레이어와 딜러의 남은 총알 수를 표시하기 위한 레이블
	protected JLabel[] playerBullet, dealerBullet;
	//플레이어가 선택 가능한 5장의 카드
	protected JButton[] button;
	//모든 이미지 배열
	protected ImageIcon[] image;
	protected Timer timer,timer2,timer3;
	
	private Container frame;
	private CardLayout cards;
	
	//게임 진행 클래스,총 발사 여부에 따라 isNotFired값 변경
	static GameManager gm = new GameManager();
    static Player player = new Player(gm);
    static Dealer dealer = new Dealer(gm);
    private static int isNotFired = 1;
    
    //총 쏘는 이미지 출력 클래스
    protected ShootingScene shoot;   
    int currentUser = PLAYERGUN;
	
	// DrawOrDieGamePanel 초기화
	public DrawOrDieGamePanel(Container frame, CardLayout cards, final String imgPath,ShootingScene shoot ) {
	
		
		this.frame = frame;
		this.cards= cards;
		this.shoot = shoot;
		
		gm.player = player;
	    gm.dealer = dealer;
	    gm.startGame();
	    
	    
		// 카드, 배경, 카드 뒷면(배경), 카드 제시 안할 시 출력 x, 플레이어 남은 탄환, 딜러 남은 탄환 이미지 준비
		image = new ImageIcon[8];
		image[CARD] = new ImageIcon( new ImageIcon( imgPath + "cardDesign.png"    ).getImage().getScaledInstance( 190, 280, Image.SCALE_SMOOTH ) );
		image[BACKGROUND] = new ImageIcon( new ImageIcon( imgPath + "background.png"     ).getImage().getScaledInstance( 700, 1000, Image.SCALE_SMOOTH ) );
		image[DEALERCARD] = new ImageIcon( new ImageIcon( imgPath + "dealerCard.png"     ).getImage().getScaledInstance( 500, 600, Image.SCALE_SMOOTH ) );
		image[DEFAULT] = null;
		image[BULLETA] = new ImageIcon( new ImageIcon( imgPath + "playerBullet.png"     ).getImage().getScaledInstance( 80, 100, Image.SCALE_SMOOTH ) );
		image[BULLETB] = new ImageIcon( new ImageIcon( imgPath + "dealerBullet.png"     ).getImage().getScaledInstance( 80, 100, Image.SCALE_SMOOTH ) );

		//플레이어,딜러 탄환 이미지 초기화
		this.playerBullet = new JLabel[3];
		this.dealerBullet = new JLabel[3];
		for(int i =0; i<3;i++) {
			playerBullet[i] = new JLabel(image[BULLETA]);
			dealerBullet[i] = new JLabel(image[BULLETB]);
			this.add(playerBullet[i]);
			this.add(dealerBullet[i]);
		}

   
		// 화면 중앙에 목표 숫자, 현재 숫자 초기화
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
		
		// 화면 아래쪽에 카드 선택 버튼 초기화
		button = new JButton[5];
		for(int i =0;i<5;i++) {
			button[i] = new JButton( new ImageIcon( image[CARD].getImage().getScaledInstance( 110, 170, Image.SCALE_SMOOTH ) ) );
			button[i].addActionListener( this );
			//버튼과 이미지 크기 일치, 이미지 위에 텍스트 출력하기(AI활용함)
			button[i].setBorderPainted(false); //버튼 테두리 제거
			button[i].setContentAreaFilled(false); //버튼 바탕 제거
			button[i].setFocusPainted(false); //버튼 선택되었을때 강조표시 비활성화
			button[i].setMargin(new Insets(0, 0, 0, 0)); //버튼 크기를 0으로 정해 이미지 끼워넣을 시 버튼의 크기가 이미지에 맞게 늘어나도록 함
			button[i].setFont(new Font("바탕", Font.BOLD, 20));			
			button[i].setHorizontalTextPosition(JButton.CENTER); //텍스트를 버튼 중앙에 출력하도록 해 이미지 위에 텍스트 출력
		}
		
		//플레이어 버튼 추가
		for(int i =0;i<5;i++) {
			button[i].setText(cardString(player.cardArray[i]));
			this.add(button[i]);
		}
		
		
		//구성요소의 크기, 좌표값을 직접 지정해서 이미지&버튼 배치
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
		
		//배경이미지 초기화
		this.dealerCard = new JLabel(image[DEALERCARD]);
		this.backgroung = new JLabel(image[BACKGROUND]);
		dealerCard.setBounds(100, 100, 500, 200);
		backgroung.setBounds(0,0,700,1000);
		this.add(dealerCard);
		this.add(backgroung);
		
		// 플레이어 카드 선택 시 1초 후 딜러 선택 or 플레이어가 목표 숫자를 넘겼을 경우 승패 판단으로
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
		
		//딜러 카드 선택 시 1초 후 다시 플레이어가 선택 가능하도록 준비 or 딜러가 목표 숫자를 넘겼을 경우 승패 판단으로
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
		
		//플레이어 승패 판단 후 살아 남았을 경우 다시 이 패널로 돌아와 컴퓨터가 선턴으로 다음라운드 진행되도록 2.7초의 대기시간 부여
		this.timer3 = new Timer( 2700, new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent event ) {
				
				computerTurn();
				timer3.stop();

			}
		});
		
	}
	
	//목표,현재 숫자 업데이트
	void displayNumber() {
		strSumAndTarget.setText("<html><body>"+" 목표: "+Integer.toString(gm.targetNumber)+"<br><br>"+" 현재: "+Integer.toString(gm.currentSum)+"</body></html>");
	}
	
	// 제시된 카드 초기화 후 플레이어 선택 버튼 활성화
	public void ready() {
		imgPlayerA.setIcon( image[DEFAULT] );
		imgPlayerB.setIcon( image[DEFAULT] );
		imgPlayerA.setText("");
		imgPlayerB.setText("");
		
		//플레이어와 딜러의 남은 탄환수에 따라 화면에 출력
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
	// 플레이어 선택한 카드 제시 후 버튼 비활성화
	@Override
	public void actionPerformed( ActionEvent event ) {
		
		
		for(int i =0;i<5;i++) {
			button[i].setEnabled(false);
		}
		currentUser = PLAYERGUN;
		//선택한 카드 번호 확인
		int playerA = select( event );
		imgPlayerA.setIcon(image[CARD]);
		//선택 카드에 맞는 문자 출력
		imgPlayerA.setText(cardString(player.cardArray[playerA]));
		//플레이어가 선택한 번호의 카드로 연산
		player.playerTurn(playerA);
		displayNumber();
		//잠시 대기 후 승패 출력 또는 딜러 차례 
		timer.start();
				
	}
	
	//딜러 카드 제시
	public void computerTurn() {
		
		currentUser = DEALERGUN;
		//딜러 선택 카드 연산, 선택 카드 반환
		int playerB = dealer.dealerTurn();
		imgPlayerB.setIcon(image[CARD]);
		//선택 카드에 맞는 문자 출력
		imgPlayerB.setText(cardString(playerB));
		displayNumber();
		//잠시 대기 후 승패 출력 또는 다시 플레이어 차례로 가기
		timer2.start();

	}
	
	//캐드 배열에 있는 숫자에 따라(특수카드가 있기 때문에) 알맞은 문자 출력
	public String cardString(int cardNumber) {
		if(cardNumber == -12) {
			return "÷";
		}
		else if(cardNumber == -13) {
			return "x";
		}
		else return Integer.toString(cardNumber);
	}
	
	//선택한 버튼을 해당 카드 번호로 반환
	public int select( ActionEvent event ) {
		
		for(int i = 0; i<5;i++) {
			if(event.getSource() == button[i]) {
				return i;
			}
		}
		return 0;
		
	}
	
	//승패 확인
	public void gameEnd() {
		//플레이어 승패 확인
		if (currentUser == PLAYERGUN) {
			
			//플레이어 총 발사 결과 반환
			isNotFired = player.fireBullet();
			//총 발사 이미지 패널에 출력할 값 전달, 출력
			shoot.isFired(PLAYERGUN, isNotFired);
			shoot.ShowShooting();	
			//제시한 카드 초기화
			imgPlayerA.setText("");
			imgPlayerB.setText("");
			imgPlayerA.setIcon( image[DEFAULT] );
			imgPlayerB.setIcon( image[DEFAULT] );
			//플레이어 남은 총알 업데이트
			for(int i = 0;i<3;i++) {
				playerBullet[i].setVisible(true);
				if(i<3-player.bulletCount) {
					playerBullet[i].setVisible(false);
				}
			}
			//총 발사 이미지 패널로 전환
			cards.show(frame, "SHOOT");
			//죽지 않았다면 컴퓨터 선턴으로 라운드 시작
			if(isNotFired == 1) {
				timer3.start();
			}
						
		}
		else {
			//딜러 총 발사 결과 반환
			isNotFired = dealer.fireBullet();
			//총 발사 이미지 패널에 출력할 값 전달, 출력
			shoot.isFired(DEALERGUN, isNotFired);
			//제시한 카드 초기화
			shoot.ShowShooting();
			imgPlayerA.setText("");
			imgPlayerB.setText("");
			imgPlayerA.setIcon( image[DEFAULT] );
			imgPlayerB.setIcon( image[DEFAULT] );
			//딜러 남은 총알 업데이트
			for(int i = 0;i<3;i++) {
				dealerBullet[i].setVisible(true);
				if(i>dealer.bulletCount-1) {
					dealerBullet[i].setVisible(false);
				}
			}
			//총 발사 이미지 패널로 전환
			cards.show(frame, "SHOOT");
			
		}
		
		//누군가 죽었다면 다음 게임을 위해 게임 초기화
		if(isNotFired == 0) {
			gm.startGame();
			ready();
			return;
		}
		//죽든 말든 목표 숫자 넘겼기 때문에 다음 라운드 진행
		gm.startRound();
		displayNumber();

	}
}

