// JAVA 프로그래밍 - https://codereading101.github.io/JAVA/
// 소스파일 - https://github.com/CodeReading101/JAVA/blob/main/Chapter13/QuestionPanel.java
package rpsGame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
// 질문 패널 클래스
public class QuestionPanel extends JPanel
{
	private JRadioButton option1, option2;
	private String nextOption2;
	private Container frame;
	private CardLayout cards;
	private Image background;
	// 질문 패널 초기화
	public QuestionPanel( Container frame, CardLayout cards, final String imgPath ) {
		// 질문 및 선택항목 제시 / 선택시 반응할 리스너 등록
		setLayout( null );
		JLabel label = new JLabel( "DRAW OR DIE" );
		label.setFont( new Font( "Microsoft Himalaya", Font.BOLD, 125) );
		label.setForeground(Color.WHITE);
		add( label );
		ClickListener click = new ClickListener();
		
		option1 = new JRadioButton( "게임시작" );
		option1.setFont( new Font( "굴림", Font.BOLD, 50) );
		option1.setForeground(Color.WHITE);
		option1.addActionListener( click );
		option1.setOpaque(false);
		option1.setFocusPainted(false);
		add( option1 );
		
		option2 = new JRadioButton( "게임종료" );
		option2.setFont( new Font( "굴림", Font.BOLD, 50) );
		option2.setForeground(Color.WHITE);
		option2.addActionListener( click );
		option2.setOpaque(false);
		option2.setFocusPainted(false);
		add( option2 );

		// 다음 질문으로 이동할 수 있도록 준비
		this.frame = frame;
		this.cards= cards;
		
		
		
		background = new ImageIcon(new ImageIcon(imgPath+"main.png").getImage().getScaledInstance(700,1000, Image.SCALE_SMOOTH)).getImage();
		label.setBounds(50,200,600,100);
		option1.setBounds(230,580,240,100);
		option2.setBounds(230,680,240,100);
		
	}
	// 항목 선택시 다음 질문으로 이동
	private class ClickListener implements ActionListener {
		public void actionPerformed( ActionEvent event ) {
			if ( ( event.getSource() == option1 ) && ( option1.getText().equals( "처음으로" ) ) ) {
				option1.setSelected( false );
				cards.first( frame );
			}
			else if ( ( event.getSource() == option1 ) && ( option1.getText().equals( "이전으로" ) ) ) {
				option1.setSelected( false );
				cards.previous( frame );
			}
			else if ( event.getSource() == option1 ) {
				option1.setSelected( false );
				cards.show(frame, "STAGE");
			}
			else if ( event.getSource() == option2 ) {
				option2.setSelected( false );
				System.exit(0);
				
			}
		}
	}
	
	@Override
	public void paintComponent( Graphics g ) {
		super.paintComponent( g );
		g.drawImage( background, 0, 0, null );
		
	}
}

