// JAVA 프로그래밍 - https://codereading101.github.io/JAVA/
// 소스파일 - https://github.com/CodeReading101/JAVA/blob/main/Chapter13/CardMatchingPanel.java
package rpsGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 카드 맞추기 보드 클래스
public class CardMatchingPanel extends JPanel {
    
	private final int MAXSTAGE = 5;
    JButton[] stage;
    JLabel tutorial;
    Image background;
    
    private Container frame;
    private CardLayout cards;
	
    // 16장의 카드를 보드에 배치
	public CardMatchingPanel(Container frame,CardLayout cards,final String  imgPath ) {
		
		this.frame = frame;
		this.cards = cards;
		this.stage  = new JButton[MAXSTAGE];
		this.tutorial = new JLabel("<html><body>　스테이지가 높아질수록<br>　딜러의 지능이 상승합니다</body></html>");
		tutorial.setFont(new Font("굴림",Font.BOLD,25));
		tutorial.setForeground(Color.WHITE);
		this.background =  new ImageIcon(new ImageIcon(imgPath+"stageBackground.png").getImage().getScaledInstance(700,1000, Image.SCALE_SMOOTH)).getImage();
		
		setLayout( new GridLayout(3, 2 ) );
		//setFocusable( true );
		//requestFocus();

		
		ClickListener click = new ClickListener();
		for( int y=0; y < MAXSTAGE; y++ ){
			    stage[y] = new JButton("stage"+(y+1));
				stage[y].addActionListener( click );
				stage[y].setFont(new Font("Microsoft Himalaya",Font.BOLD,80));
				stage[y].setForeground(Color.white);
				stage[y].setContentAreaFilled(false);
				add( stage[y]);
			
		}
		
		add(tutorial);
	}

	// 카드 클릭
	private class ClickListener implements ActionListener {
		public void actionPerformed( ActionEvent event ) {
			for(int i =0; i<MAXSTAGE;i++) {
				if(event.getSource()==stage[i]) {
					GameManager.setStage(i+1);;
					cards.show(frame, "GAME");
				}
			}
			
		}
	}
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		g.drawImage( background, 0, 0, null );
		
	}

}


