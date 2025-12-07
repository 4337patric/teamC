package rpsGame;
import java.util.*;

//게임 매니저 클래스
class GameManager {
	private static int stagecount = 1;
    int targetNumber;
    int currentSum;
    int x2Active;
    Player player;
    Dealer dealer;
    Scanner sc = new Scanner(System.in);

    //스테이지 단계 수정 함수
    public static void setStage(int stage) {
		stagecount = stage;
	}
    //스테이지 단계 반환 함수
    public static int getStage() {
		return stagecount;
	}

    //게임 초기화 함수
    public void startGame() {
        dealer.bulletCount = 3;
        player.bulletCount = 3;
        startRound();
    }
    
    //라운드 시작 함수
    public void startRound() {
        //목표 숫자, 플레이어 딜러 카드 초기화
    	targetNumber = (int) (Math.random() * 21) + 20; // 20~40
        currentSum = 0;
        x2Active = 0;
        Arrays.fill(player.cardArray, -11);
        Arrays.fill(dealer.cardArray, -11);

        // 카드 분배
        for(int i = 0; i<5;i++) {
        giveCard(player.cardArray);
        giveCard(dealer.cardArray);
        }

    }

    //카드 분배 함수
    public void giveCard(int[] cardArray) {
        //빈 카드(-11)를 확률에 따라 숫자 또는 특수 카드로 분배
    	for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == -11) {
                double cardType = Math.random();
                if (cardType < 0.7) {
                    cardArray[i] = (int) (Math.random() * 20) + 1;
                } else {
                    int specialType = (int) (Math.random() * 5) + 1;
                    switch (specialType) {
                        case 1:
                            cardArray[i] = 0;
                            break;
                        case 2:
                            cardArray[i] = -((int) (Math.random() * 10) + 1);
                            break;
                        case 3:
                            cardArray[i] = -12;
                            break;
                        case 4:
                            cardArray[i] = -13;
                            break;
                        case 5:
                            cardArray[i] = targetNumber - 1;
                            break;
                    }
                }
            }
        }
    }


}
//플레이어 클래스
class Player {
    int bulletCount = 3;
    int[] cardArray = new int[5];
    GameManager gm;
    

    public Player(GameManager gm) {
        this.gm = gm;
    }

    //플레이어 선택 카드에 따른 연산, 카드 드로우
    public void playerTurn(int choice) {
    	   

        int selectedCard = cardArray[choice];

        if (selectedCard == -12) {
            gm.currentSum /= 2;
        } else if (selectedCard == -13) {
            gm.x2Active = 1;
        } else {
            if (gm.x2Active == 1) {
                gm.currentSum += selectedCard * 2;
                gm.x2Active = 0;
            } else {
                gm.currentSum += selectedCard;
            }
        }

        cardArray[choice] = -11; // used card
        gm.giveCard(cardArray);
        
    }

    //플레이어 탄환 30%확률로 발사, 발사 여부 반환
    public int fireBullet() {
        if (bulletCount <= 1) {
        	bulletCount -= 1;
            return 0;
        } else {
            double temp = Math.random();
            if (temp < 0.7) {
                bulletCount -= 1;
                return 1;
            } else {
            	bulletCount -= 1;
                return 0;
            }
        }
    }

 
}

//딜러 클래스
class Dealer {
    int bulletCount = 3;
    int[] cardArray = new int[5];
    GameManager gm;

    public Dealer(GameManager gm) {
        this.gm = gm;
    }

    //스테이지 난이도에 따라 딜러 난이도 변경, 딜러 선택 카드에 따른 연산, 카드 드로우
    public int dealerTurn() {
    	gm.giveCard(cardArray);
    	int idx = 0;
		int cardValue = 0;
    	if (GameManager.getStage() == 1) {
    		idx = chooseCard1();
    		cardValue = cardArray[idx];
    	}
    	else if(GameManager.getStage() == 2) {
    		idx = chooseCard2();
    		cardValue = cardArray[idx];
    	}
    	else if(GameManager.getStage() == 3) {
    		idx = chooseCard3();
    		cardValue = cardArray[idx];
    	}
    	else if(GameManager.getStage() == 4) {
    		idx = chooseCard4();
    		cardValue = cardArray[idx];
    	}
    	else if(GameManager.getStage() == 5) {
    		idx = chooseCard5();
    		cardValue = cardArray[idx];
    	}
        


        if (cardValue == -12) {
            gm.currentSum /= 2;
        } else if (cardValue == -13) {
            gm.x2Active = 1;
        } else {
            if (gm.x2Active == 1) {
                gm.currentSum += cardValue * 2;
                gm.x2Active = 0;
            } else {
                gm.currentSum += cardValue;
            }
        }
        cardArray[idx] = -11;
        

        return cardValue;
    }

    //스테이지에 따른 딜러의 카드 선택 알고리즘
    
    //1스테이지 : 그냥 랜덤 카드 선택 
    public int chooseCard1() {
        int[] temp = new int[5];
        int count = 0;
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] != -11) {
                temp[count] = i;
                count++;
            }
        }

        int r = (int)(Math.random() * count);
        return temp[r];
    }

    // 2스테이지 : 특수카드 있으면 그거부터, 없으면 랜덤
    public int chooseCard2() {
        for (int i = 0; i < cardArray.length; i++) {
            int c = cardArray[i];
            if (c == -12 || c == -13 || c == 0 || c < 0 || c == gm.targetNumber - 1) {
                if (c != -11) {
                    return i;
                }
            }
        }
        return chooseCard1();
    }

    // 3스테이지 : X2 → 0 → 음수 → /2 → 최소 양수
    public int chooseCard3() {

        // X2(-13)
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == -13) {
                return i;
            }
        }

        // 0
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == 0) {
                return i;
            }
        }

        // 음수(-1~-10 포함), target-1 포함
        for (int i = 0; i < cardArray.length; i++) {
            int c = cardArray[i];
            if (c < 0 && c != -12 && c != -13) {
                return i;
            }
            if (c == gm.targetNumber - 1) {
                return i;
            }
        }

        // /2(-12)
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == -12) {
                return i;
            }
        }

        // 가장 작은 양수
        int min = 9999;
        int idx = -1;
        for (int i = 0; i < cardArray.length; i++) {
            int c = cardArray[i];
            if (c > 0 && c < min) {
                min = c;
                idx = i;
            }
        }

        if (idx != -1) {
            return idx;
        }

        // 그래도 없으면 그냥 랜덤
        return chooseCard1();
    }

    // 4스테이지 : 현재 숫자/목표 숫자 비율에 따라 카드 선택
    public int chooseCard4() {
        
    	double ratio = (double) gm.currentSum / gm.targetNumber;

        int selectedIndex = -1;

        //비율이 80% 이상이면 특수카드 제출, 특수 카드 없으면 가장 작은 양수
        if (ratio > 0.8) {

            
            for (int i = 0; i < cardArray.length; i++) {
                if (cardArray[i] <= 0) {
                    selectedIndex = i;
                    break;
                }
            }

            
            if (selectedIndex == -1) {
                int min = Integer.MAX_VALUE;
                for (int i = 0; i < cardArray.length; i++) {
                    int c = cardArray[i];
                    if (c > 0 && c < min) {
                        min = c;
                        selectedIndex = i;
                    }
                }
            }
        }

        //비율이 50%이상이면 가장 작은 양수 카드
        else if (ratio > 0.5) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < cardArray.length; i++) {
                int c = cardArray[i];
                if (c > 0 && c < min) {
                    min = c;
                    selectedIndex = i;
                }
            }
        }

        // 그 이하 비율에서는 양수 카드 중에서 랜덤
        else {
            List<Integer> positiveIndexes = new ArrayList<>();
            for (int i = 0; i < cardArray.length; i++) {
                if (cardArray[i] > 0) {
                    positiveIndexes.add(i);
                }
            }

            if (!positiveIndexes.isEmpty()) {
                selectedIndex = positiveIndexes.get(
                    (int)(Math.random() * positiveIndexes.size())
                );
            }
        }

        
        if (selectedIndex == -1) {
            return 0; 
        }

        return selectedIndex;
    }
    
    // 5스테이지 : 목표 숫자를 넘지 않는 가장 큰 양수 카드 제출, 가진 모든 양수 카드가 목표 숫자를 넘기면 선호도에 따라 특수카드 제출
    public int chooseCard5() {
        int selectedIndex = -1;
        int maxValue = Integer.MIN_VALUE;

        // 1. 목표를 넘지 않는 가장 큰 양수 카드 선택
        for (int i = 0; i < cardArray.length; i++) {
            int c = cardArray[i];
            if (c > 0 && gm.currentSum + c < gm.targetNumber) {
                if (c > maxValue) {
                    maxValue = c;
                    selectedIndex = i;
                }
            }
        }
        if (selectedIndex != -1) {
            return selectedIndex;
        }

        // 2. 특수카드 선호도 순서대로 탐색

        // X2 카드 (-12)
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == -12) return i;
        }
        // 0 카드
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == 0) return i;
        }
        // 일반 음수 카드 (-10 ~ -1)
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] < 0 && cardArray[i] > -11) return i;
        }
        // /2 카드 (-13)
        for (int i = 0; i < cardArray.length; i++) {
            if (cardArray[i] == -13) return i;
        }

        // 둘 다 없으면 1번 카드 제출
        return 1;
    }


    //딜러 탄환 30%확률로 발사, 발사 여부 반환
    public int fireBullet() {
        if (bulletCount <= 1) {
        	bulletCount -= 1;
            return 0;
        } else {
            double temp = Math.random();
            if (temp < 0.7) {
                bulletCount -= 1;
               return 1;
            } else {
            	bulletCount -= 1;
                return 0;
            }
        }
    }

  
}

