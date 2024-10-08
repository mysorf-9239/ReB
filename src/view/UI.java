package view;

import controller.tool.UtilityTool;
import model.Object.Obj_Heart;
import model.entity.Entity;
import model.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;
    Color poison = new Color(52, 35, 122, 250);

    //Load images
    BufferedImage backgorundImage;
    BufferedImage[] characterImage = new BufferedImage[7];
    BufferedImage[] objectImage = new BufferedImage[10];
    BufferedImage[] mapImage = new BufferedImage[21];
    BufferedImage axeImage;

    //Obj image
    BufferedImage heart_full, heart_half, heart_blank;

    //Message
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public int commanNum = 0;

    //Other
    String currentDialogue = "";
    public static int selectMap = 1;

    public int titleScreenState = 0;
    public int subState = 0;
    public int guideState = 0;
    public int introState = 0;


    public UI (GamePanel gp) {

        this.gp = gp;


        //Define font
        try {
            InputStream inputStream = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            assert inputStream != null;
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            e.getStackTrace();
        }

        //Get character images for UI
        getCharacterImages();
        getObjectImages();
        getMapImages();

        //Create hub object
        Entity heart = new Obj_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    public void showMess(String text) {

        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);
        //Title state
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        //Play State
        if (gp.gameState == gp.playState) {
            if (gp.gameMode == 0) {
                drawTopBar();
                drawPlayerLife();
                drawScore();
                drawAxe();
            } else {
                drawTopBar();
                drawMap();
                drawAxe();
            }
            drawMessage();
        }
        //Option screen
        if (gp.gameState == gp.optionState) {
            drawOptionScreen();
        }
        //Game over screen
        if (gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        //Win map screen
        if (gp.gameState == gp.winState) {
            drawWinScreen();
        }
        //GuideScreen
        if (gp.gameState == gp.guideState) {
            drawGuideScreen();
        }
        //Intro
        if (gp.gameState == gp.introState) {
            drawIntro();
        }
    }

    public void drawMessage () {

        int messageX = gp.titleSize/2;
        int messageY = gp.screenHeight/2;
        g2.setFont(g2.getFont().deriveFont(32F));

        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {

                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;

                if (messageCounter.get(i) >= 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    public void drawTopBar() {

        g2.setColor(new Color(86, 85, 85, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.titleSize);

        float strokeWidth = 2.0f;
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.setColor(new Color(86, 85, 85, 200));
        g2.drawLine(0, gp.titleSize-4, gp.screenWidth, gp.titleSize-4);

        g2.setColor(new Color(86, 85, 85, 250));
        g2.drawLine(0, gp.titleSize-2, gp.screenWidth, gp.titleSize-2);
    }

    public void drawPlayerLife() {

        int x = gp.titleSize/2;
        int y = 0;

        int i = 0;

        //Draw max blank heart
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.titleSize;
        }

        //Reset x, y
        x = gp.titleSize/2;
        int life = gp.player.life;

        //Draw current life
        if (life == 1) {
            g2.drawImage(heart_half, x, y, null);
        }
        else if (life == 2) {
            g2.drawImage(heart_full, x, y, null);
        }
        else if (life == 3) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_half, x + gp.titleSize, y, null);
        }
        else if (life == 4) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_full, x + gp.titleSize, y, null);
        }
        else if (life == 5) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_full, x + gp.titleSize, y, null);
            g2.drawImage(heart_half, x + gp.titleSize*2, y, null);
        }
        else if (life > 5) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_full, x + gp.titleSize, y, null);
            g2.drawImage(heart_full, x + gp.titleSize*2, y, null);
        }
    }

    public void drawScore() {

        int disScore = (GamePanel.maxWorldRow - 11 - Player.furthestY /gp.titleSize)* GamePanel.DISTANCE_REWARD;
        int Score = GamePanel.totalScore + disScore;

        String text = "Score: " + Score;
        int scoreX = getXforCenterText(text);
        int scoreY = gp.titleSize;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        g2.setColor(Color.white);
        g2.drawString(text, scoreX - 32, scoreY*2/3);
    }

    public void drawMap() {

        String text = "Map: " + GamePanel.currentMap;
        int scoreX = getXforCenterText(text);
        int scoreY = gp.titleSize;

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        g2.setColor(Color.white);
        g2.drawString(text, scoreX - 32, scoreY*2/3);
    }

    public void drawAxe() {

        UtilityTool utilityTool = new UtilityTool();

        try {
            axeImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/axe.png")));
            axeImage = utilityTool.scaleImage(axeImage, 32, 32);
        } catch (IOException e) {
            e.getStackTrace();
        }

        int axeX = gp.screenWidth - gp.titleSize*2;
        int axeY = 0;

        g2.drawImage(axeImage, axeX, axeY, gp.titleSize-5, gp.titleSize-5, null);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        g2.drawString("x" + gp.player.hasAxe, axeX+gp.titleSize, gp.titleSize*2/3);
    }

    public void drawTitleScreen() {
        try {
            backgorundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Background
        g2.drawImage(backgorundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

        if (titleScreenState == 0) {
            //Title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Fugitive";
            int x = getXforCenterText(text);
            int y = gp.titleSize*5;

            //Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x+3, y+3);

            //Main Color
            g2.setColor(poison);
            g2.drawString(text, x, y);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
            g2.setColor(Color.white);

            text = "New Game";
            x = getXforCenterText(text) + gp.titleSize*4;
            y += gp.titleSize * 7;
            g2.drawString(text, x, y);
            if (commanNum == 0) {
                drawColection(text, x, y);
            }

            text = "Load Game";
            x = getXforCenterText(text) + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 1) {
                drawColection(text, x, y);
            }

            text = "Setting";
            x = getXforCenterText(text) + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 2) {
                drawColection(text, x, y);
            }

            text = "Guide";
            x = getXforCenterText(text) + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 3) {
                drawColection(text, x, y);
            }

            text = "Quit";
            x = getXforCenterText(text) + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 4) {
                drawColection(text, x, y);
            }

            //Create
            g2.setFont(g2.getFont().deriveFont(12F));
            g2.setColor(Color.darkGray);
            text = "Create by Teams17";
            x = 3;
            y = gp.screenHeight - gp.titleSize/6;
            g2.drawString(text, x, y);

            //Version
            text = "version: 1.0.0";
            x = gp.screenWidth - gp.titleSize*2;
            g2.drawString(text, x, y);

        }
        else if (titleScreenState == 1) {

            //Title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Setting";
            int x = getXforCenterText(text);
            int y = gp.titleSize*5;

            //Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x+3, y+3);
            //Main
            g2.setColor(poison);
            g2.drawString(text, x, y);


            //Menu
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
            g2.setColor(Color.white);

            text = "Mode";
            x = getXforCenterText(text)  + gp.titleSize*4;
            y += gp.titleSize*6 ;
            g2.drawString(text, x, y);
            if (commanNum == 0) {
                drawColection(text, x, y);
                //Triange1
                int[] xPoints = {x + gp.titleSize*3, x + gp.titleSize*5/2, x + gp.titleSize*3};
                int[] yPoints = {y-gp.titleSize*3/5, y-gp.titleSize*3/10, y};
                g2.setColor(Color.white);
                g2.fillPolygon(xPoints, yPoints, 3);
                //Triange2
                xPoints[0] = x + gp.titleSize*6;
                xPoints[1] = x + gp.titleSize*13/2;
                xPoints[2] = x + gp.titleSize*6;
                g2.setColor(Color.white);
                g2.fillPolygon(xPoints, yPoints, 3);

                //Mode name
                g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 36F));
                if (gp.gameMode == gp.endlessMode) {
                    g2.drawString("Endless", x + gp.titleSize*3+27, y-3);
                } else if (gp.gameMode == gp.overcomeMode) {
                    g2.drawString("Overcome", x + gp.titleSize*3+12, y-3);
                }
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
                g2.setColor(Color.white);
            }

            text = "Music";
            x = getXforCenterText(text)  + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 1) {
                drawColection(text, x, y);
                //Music bar
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(x + gp.titleSize*3+17, y-gp.titleSize*3/5, 120, 24);
                int volumeWidth = 24 * gp.music.volumeScale;
                g2.fillRect(x + gp.titleSize*3+17, y-gp.titleSize*3/5, volumeWidth, 24);
            }
            text = "SE";
            x = getXforCenterText(text)  + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 2) {
                drawColection(text, x, y);
                //SE bar
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(x + gp.titleSize*9/4+20, y-gp.titleSize*3/5, 120, 24);
                int volumeWidth = 24 * gp.se.volumeScale;
                g2.fillRect(x + gp.titleSize*9/4+20, y-gp.titleSize*3/5, volumeWidth, 24);
            }
            text = "Character";
            x = getXforCenterText(text)  + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 3) {
                drawColection(text, x, y);
            }
            text = "Back";
            x = getXforCenterText(text)  + gp.titleSize*4;
            y += gp.titleSize + 10;
            g2.drawString(text, x, y);
            if (commanNum == 4) {
                drawColection(text, x, y);
            }

            gp.config.saveConfig();
        }
        else if (titleScreenState == 2) {
            //Title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Select Your Character";
            int x = getXforCenterText(text);
            int y = gp.titleSize*4;

            //Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x+3, y+3);
            //Main
            g2.setColor(poison);
            g2.drawString(text, x, y);

            //Menu
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));

            //Character
            x =  gp.titleSize*9;
            y += gp.titleSize*3 ;
            int width = gp.titleSize*3;
            int height = gp.titleSize*3;

            g2.setColor(new Color(39, 47, 44, 200));
            g2.fillRect(x-gp.titleSize, y-gp.titleSize, gp.titleSize*5, gp.titleSize*6+10);
            g2.drawImage(characterImage[Player.characterNum], x, y, width, height, null);
            if (commanNum == 0) {
                g2.setColor(Color.white);
                //Triange1
                int[] xPoints = {x - gp.titleSize/2, x - gp.titleSize, x - gp.titleSize /2};
                int[] yPoints = {y+gp.titleSize*9/5, y+gp.titleSize*3/2, y+gp.titleSize*6/5};
                g2.fillPolygon(xPoints, yPoints, 3);
                //Triange2
                xPoints[0] = x + gp.titleSize*7/2;
                xPoints[1] = x + gp.titleSize*4;
                xPoints[2] = x + gp.titleSize*7/2;
                g2.fillPolygon(xPoints, yPoints, 3);
            }

            //Back
            text = "Back";
            x = getXforCenterText(text)+gp.titleSize/2;
            y += gp.titleSize*5;
            g2.drawString(text, x, y);
            if (commanNum == 1) {
                drawColection(text, x, y);
            }

            gp.config.saveConfig();
        }
        else if (titleScreenState == 3) {
            //Title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Select Your Level";
            int x = getXforCenterText(text);
            int y = gp.titleSize*4;

            //Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x+3, y+3);
            //Main
            g2.setColor(poison);
            g2.drawString(text, x, y);

            //Menu
            g2.setColor(new Color(39, 47, 44, 200));
            g2.fillRect(x+gp.titleSize*5/2, y+gp.titleSize*2, gp.titleSize*9, gp.titleSize*9);

            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));

            //Map
            x =  gp.titleSize*9;
            y += gp.titleSize*3 ;
            int width = gp.titleSize*5;
            int height = gp.titleSize*5;

            //Map image
            g2.drawImage(mapImage[selectMap], x - gp.titleSize+2, y - gp.titleSize+10, width, height, null);

            //Map num
            x = gp.titleSize*19/2 - 10;
            y += gp.titleSize * 6;
            g2.setColor(Color.white);
            if (selectMap < 10) {
                text = "Map 0" + selectMap;
            } else {
                text = "Map " + selectMap;
            }
            g2.drawString(text, x, y);
            //Arrows
            if (commanNum == 0) {
                g2.setColor(Color.white);
                //Triange1
                int[] xPoints = {x - gp.titleSize /2 - 6, x - gp.titleSize - 6, x - gp.titleSize /2 - 6};
                int[] yPoints = {y+gp.titleSize*3/10 - 16, y - 16, y-gp.titleSize*3/10 - 16};
                g2.fillPolygon(xPoints, yPoints, 3);
                //Triange2
                xPoints[0] = x + gp.titleSize*3 + 10;
                xPoints[1] = x + gp.titleSize*7/2 + 10;
                xPoints[2] = x + gp.titleSize*3 + 10;
                g2.fillPolygon(xPoints, yPoints, 3);
            }

            //Back
            text = "Back";
            x = getXforCenterText(text)+gp.titleSize/2;
            y += gp.titleSize*2-15;
            g2.drawString(text, x, y);
            if (commanNum == 1) {
                drawColection(text, x, y);
            }

            gp.config.saveConfig();
        }
        else if (titleScreenState == 4) {
            drawMapNotReady();
        }
    }


    //Option state
    private void drawOptionScreen() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.titleSize*6;
        int frameY = gp.titleSize*3;
        int frameWidth = gp.titleSize*8;
        int frameHeight = gp.titleSize*12;
        drawSubWindown(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0:
                option_tops(frameX,frameY);
                break;
            case 1:
                option_EndGame_Confirmation(frameX, frameY);
                break;
            case 2:
                options_control(frameX, frameY);
                break;
        }
    }

    public void option_tops(int frameX, int frameY) {

        int textX;
        int textY;

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));
        //Title
        String text = "Options";
        textX = getXforCenterText(text);
        textY = frameY + gp.titleSize;
        g2.drawString(text, textX, textY);


        //Music
        text = "Music";
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;
        g2.drawString(text, textX, textY);
        if (commanNum == 0) {
            drawColection(text, textX, textY);
        }

        //SE
        text = "SE";
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;
        g2.drawString(text, textX, textY);
        if (commanNum == 1) {
            drawColection(text, textX, textY);
        }

        //Control
        text = "Control";
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;
        g2.drawString(text, textX, textY);
        if (commanNum == 2) {
            drawColection(text, textX, textY);
        }

        //EndGame
        text = "EndGame";
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*5/2;
        g2.drawString(text, textX, textY);
        if (commanNum == 3) {
            drawColection(text, textX, textY);
        }

        //Retry
        text = "Retry";
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;
        g2.drawString(text, textX, textY);
        if (commanNum == 4) {
            drawColection(text, textX, textY);
        }

        //Back
        text = "Back";
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;
        g2.drawString(text, textX, textY);
        if (commanNum == 5) {
            drawColection(text, textX, textY);
        }

        //Music bar
        textX = frameX + gp.titleSize*4 + 5;
        textY = frameY + gp.titleSize*3/2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        //SE bar
        textY += gp.titleSize*3/2;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        gp.config.saveConfig();
    }

    public void options_control(int frameX, int frameY) {

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));

        int textX;
        int textY;

        //TITLE
        String text = "Control";
        textX = getXforCenterText(text);
        textY = frameY + gp.titleSize;
        g2.drawString(text, textX,textY);

        //Menu control
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;

        g2.drawString("Move", textX, textY); textY += gp.titleSize*3/2;
            textX += gp.titleSize;
        g2.drawString("Up", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("Left", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("Down", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("Right", textX, textY); textY += gp.titleSize*3/2;
            textX -= gp.titleSize;
        g2.drawString("Felling Tree", textX, textY);

        //Control
        textX = frameX + gp.titleSize*6;
        textY = frameY + gp.titleSize*4;

        g2.drawString("W", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("A", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("S", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("D", textX, textY); textY += gp.titleSize*3/2;
        g2.drawString("C", textX, textY);

        //Back
        textX = frameX + gp.titleSize;
        textY = frameY + gp.titleSize*23/2;
        g2.drawString("Back", textX, textY);
        if (commanNum == 0) {
            drawColection("Back", textX, textY);
        }

    }

    public void option_EndGame_Confirmation(int frameX, int frameY) {

        int textX = frameX + gp.titleSize + 40;
        int textY = gp.titleSize*6;

        currentDialogue = "Save the game and \nreturn to the title screen";

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));
        for (String line: currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textX -= 40;
            textY += gp.titleSize;
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD));
        //Yes
        String text = "Yes";
        textX = getXforCenterText(text);
        textY += gp.titleSize * 4;
        g2.drawString(text, textX, textY);
        if (commanNum == 0) {
            drawColection(text, textX, textY);
        }

        //No
        text = "No";
        textX = getXforCenterText(text);
        textY += gp.titleSize * 2;
        g2.drawString(text, textX, textY);
        if (commanNum == 1) {
            drawColection(text, textX, textY);
        }
    }

    public void drawGameOverScreen() {

        //Over
        g2.setColor(new Color(0, 0, 0, 125));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x, y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));

        text = "Game Over";
        //Shadow
        g2.setColor(Color.black);
        x = getXforCenterText(text);
        y = gp.titleSize*5;
        g2.drawString(text, x, y);
        //Main
        g2.setColor(Color.RED);
        g2.drawString(text, x-4, y-4);

        //BXH
        g2.setFont(g2.getFont().deriveFont(50F));
        g2.setColor(Color.white);

        int disScore = (GamePanel.maxWorldRow - 11 - Player.furthestY /gp.titleSize)* GamePanel.DISTANCE_REWARD;
        int Score = GamePanel.totalScore + disScore;

        if (Score < GamePanel.highestScore) {
            text = "Highest Socre: " + GamePanel.highestScore;
            x = getXforCenterText(text);
            y += gp.titleSize * 4;
            g2.drawString(text, x, y);

            text = "Your Score: " + Score;
            x = getXforCenterText(text);
            y += gp.titleSize * 2;
            g2.drawString(text, x, y);
        } else {
            text = "New Record";
            x = getXforCenterText(text);
            y += gp.titleSize * 4;
            g2.drawString(text, x, y);

            text = "Score: " + Score;
            x = getXforCenterText(text);
            y += gp.titleSize * 2;
            g2.drawString(text, x, y);
        }

        //Retry
        text = "Retry";
        x = getXforCenterText(text);
        y += gp.titleSize*4;
        g2.drawString(text, x, y);
        if (commanNum == 0) {
            drawColection(text, x, y);
        }

        //Return title screen
        text = "Quit";
        x = getXforCenterText(text);
        y += gp.titleSize*2;
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (commanNum == 1) {
            drawColection(text, x, y);
        }

        gp.config.saveConfig();
    }

    public void drawWinScreen() {

        //Over
        g2.setColor(new Color(0, 0, 0, 125));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x, y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110F));

        text = "You Win";
        //Shadow
        g2.setColor(Color.black);
        x = getXforCenterText(text);
        y = gp.titleSize*6;
        g2.drawString(text, x, y);
        //Main
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);

        //Next Map
        g2.setFont(g2.getFont().deriveFont(50F));
        g2.setColor(Color.white);
        text = "Next Map";
        x = getXforCenterText(text);
        y += gp.titleSize*6;
        g2.drawString(text, x, y);
        if (commanNum == 0) {
            drawColection(text, x, y);
        }

        //Retry
        g2.setFont(g2.getFont().deriveFont(50F));
        g2.setColor(Color.white);
        text = "Retry";
        x = getXforCenterText(text);
        y += gp.titleSize*2;
        g2.drawString(text, x, y);
        if (commanNum == 1) {
            drawColection(text, x, y);
        }

        //Return title screen
        text = "Quit";
        x = getXforCenterText(text);
        y += gp.titleSize*2;
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
        if (commanNum == 2) {
            drawColection(text, x, y);
        }
    }

    public void drawMapNotReady() {

        //Draw background
        int frameX = gp.titleSize*6;
        int frameY = gp.titleSize*6;
        int frameWidth = gp.titleSize*9;
        int frameHeight = gp.titleSize*7;
        drawSubWindown(frameX, frameY, frameWidth, frameHeight);

        //Draw Message
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));

        int textX;
        int textY = frameY + gp.titleSize + 15;

        currentDialogue = "You must complete the \nprevious maps to \nunlock this map. \nPlease select another map.";

        for (String line: currentDialogue.split("\n")) {
            textX = getXforCenterText(line) + 18;
            g2.drawString(line, textX, textY);
            textY += gp.titleSize;
        }

        //Back
        String text = "Back";
        textX = getXforCenterText(text) + 20;
        textY += gp.titleSize;
        g2.drawString(text, textX, textY);
    }

    public void drawGuideScreen() {

        try {
            backgorundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Background
        g2.drawImage(backgorundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.titleSize*6;
        int frameY = gp.titleSize*3;
        int frameWidth = gp.titleSize*8;
        int frameHeight = gp.titleSize*12;
        drawSubWindown(frameX, frameY, frameWidth, frameHeight);

        switch (guideState) {
            case 0:
                guide_selection(frameX, frameY);
                break;
            case 1:
                guide_top(frameX, frameY, 1);
                break;
            case 2:
                guide_object(frameX, frameY, 1);
                break;
            default:
                break;
        }

    }

    public void guide_selection(int frameX, int frameY) {

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));

        int textX;
        int textY;

        //TITLE
        String text = "Guide";
        textX = getXforCenterText(text);
        textY = frameY + gp.titleSize;
        g2.drawString(text, textX,textY);

        //Menu
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*2;
        g2.drawString("Controls", textX, textY);
        if (commanNum == 0) {
            drawColection("Controls", textX, textY);
        }

        textX = frameX + gp.titleSize;
        textY += gp.titleSize*2;
        g2.drawString("Objects", textX, textY);
        if (commanNum == 1) {
            drawColection("Objects", textX, textY);
        }

        //Back
        textX = frameX + gp.titleSize;
        textY = frameY + gp.titleSize*23/2;
        g2.drawString("Back", textX, textY);
        if (commanNum == 2) {
            drawColection("Back", textX, textY);
        }
    }

    public void guide_top(int frameX, int frameY, int num) {

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC));

        int textX;
        int textY;

        //TITLE
        String text = "Controls";
        textX = getXforCenterText(text);
        textY = frameY + gp.titleSize;
        g2.drawString(text, textX,textY);

        //Menu control
        textX = frameX + gp.titleSize;
        textY += gp.titleSize*3/2;

        g2.drawString("Move", textX, textY); textY += gp.titleSize*5/4;
        textX += gp.titleSize;
        g2.drawString("Up", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("Left", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("Down", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("Right", textX, textY); textY += gp.titleSize*5/4;
        textX -= gp.titleSize;
        g2.drawString("Felling Tree", textX, textY);  textY += gp.titleSize*5/4;
        g2.drawString("Overview", textX, textY);

        //Control
        textX = frameX + gp.titleSize*6;
        textY = frameY + gp.titleSize*15/4;

        g2.drawString("W", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("A", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("S", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("D", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("C", textX, textY); textY += gp.titleSize*5/4;
        g2.drawString("M", textX, textY);

        if (num == 1) {//Back
            textX = frameX + gp.titleSize;
            textY = frameY + gp.titleSize * 23 / 2;
            g2.drawString("Back", textX, textY);
            if (commanNum == 0) {
                drawColection("Back", textX, textY);
            }
        }

    }

    public void guide_object(int frameX, int frameY, int num) {

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 28F));

        int textX;
        int textY;

        //TITLE
        String text = "Object";
        textX = getXforCenterText(text);
        textY = frameY + gp.titleSize;
        g2.drawString(text, textX,textY);

        //Images
        int imageX = frameX + gp.titleSize;
        int imageY = frameY + gp.titleSize*2;
        text = "Axe: Cut tree";
        textX = frameX + gp.titleSize*5/2;
        textY += gp.titleSize*3/2;
        g2.drawImage(objectImage[0], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);

        imageY += gp.titleSize*5/4;
        text = "Boots: Speed up";
        textY += gp.titleSize*5/4;
        g2.drawImage(objectImage[1], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);

        imageY += gp.titleSize*5/4;
        text = "Spidernet: Speed down";
        textY += gp.titleSize*5/4;
        g2.drawImage(objectImage[5], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);

        imageY += gp.titleSize*5/4;
        text = "Hole: Bleed";
        textY += gp.titleSize*5/4;
        g2.drawImage(objectImage[6], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);

        imageY += gp.titleSize*5/4;
        text = "HP: Restore HP";
        textY += gp.titleSize*5/4;
        g2.drawImage(objectImage[7], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);

        imageY += gp.titleSize*5/4;
        text = "Chest: Score up";
        textY += gp.titleSize*5/4;
        g2.drawImage(objectImage[2], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);

        imageY += gp.titleSize*5/4;
        text = "Portal: Next map";
        textY += gp.titleSize*5/4;
        g2.drawImage(objectImage[3], imageX, imageY, gp.titleSize*2/3, gp.titleSize*2/3, null);
        g2.drawString(text, textX, textY);


        if (num == 1) {
            //Back
            textX = frameX + gp.titleSize;
            textY = frameY + gp.titleSize * 23 / 2;
            g2.drawString("Back", textX, textY);
            if (commanNum == 0) {
                drawColection("Back", textX, textY);
            }
        }

    }

    public void drawIntro() {

        try {
            backgorundImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Background.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Background
        g2.drawImage(backgorundImage, 0, 0, gp.screenWidth, gp.screenHeight, null);

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int frameX = gp.titleSize*6;
        int frameY = gp.titleSize*3;
        int frameWidth = gp.titleSize*8;
        int frameHeight = gp.titleSize*12;
        drawSubWindown(0, gp.screenHeight - gp.titleSize*3/2, gp.screenWidth, gp.titleSize*2);

        switch (introState) {
            case 0:
                intro_0(frameX, frameY);
                break;
            case 1:
                drawSubWindown(frameX, frameY, frameWidth, frameHeight);
                intro_1(frameX, frameY);
                break;
            case 2:
                drawSubWindown(frameX, frameY, frameWidth, frameHeight);
                intro_2(frameX, frameY);
                break;
            default:
                gp.gameState = gp.titleState;
                break;
        }
    }

    public void intro_0(int frameX, int frameY) {

        String text;
        int textX;
        int textY;

        g2.setColor(new Color(94, 89, 89, 76));
        g2.fillRect(0, gp.titleSize*11/2, gp.screenWidth, gp.titleSize*2);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 64F));
        g2.setColor(Color.white);
        text = "Welcome to Team17's Game";
        textX = getXforCenterText(text);
        textY = gp.titleSize*7;
        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 32F));
        text = "Press Enter to continue";
        textX = gp.screenWidth - gp.titleSize*7;
        textY = gp.screenHeight - gp.titleSize/2;
        g2.drawString(text, textX, textY);
    }

    public void intro_1(int frameX, int frameY) {

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 32F));
        g2.setColor(Color.white);

        String text;
        int textX;
        int textY;

        guide_top(frameX, frameY, 0);

        text = "Press Enter to continue";
        textX = gp.screenWidth - gp.titleSize*7;
        textY = gp.screenHeight - gp.titleSize/2;
        g2.drawString(text, textX, textY);

    }

    public void intro_2(int frameX, int frameY) {

        String text;
        int textX;
        int textY;

        guide_object(frameX, frameY, 0);

        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 32F));
        text = "Press Enter to continue";
        textX = gp.screenWidth - gp.titleSize*7;
        textY = gp.screenHeight - gp.titleSize/2;
        g2.drawString(text, textX, textY);

    }

    //Auxiliary Method
    public int getXforCenterText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return gp.screenWidth/2 - length/2;
    }

    private void drawSubWindown(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width - 10, height - 10, 25, 25);
    }

    public void drawColection(String text, int x, int y) {

        //Shadow
        g2.setColor(Color.black);
        g2.drawString(text, x, y);

        //Main Color
        g2.setColor(poison);
        if (gp.gameState == gp.optionState) { g2.setColor(Color.red);}
        g2.drawString(text, x-2, y-1);
        g2.setColor(Color.white);
    }

    public void getCharacterImages() {

        UtilityTool utilityTool = new UtilityTool();

        try {
            BufferedImage bigImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/player/manyCharacter01.png")));

            int x = 32;
            int y = 0;
            for (int i = 0; i < 7; i++) {
                // Cắt ảnh con từ ảnh gốc
                BufferedImage subImage = bigImage.getSubimage(x, y, 32, 32);
                subImage = utilityTool.scaleImage(subImage, 32, 32);

                characterImage[i] = subImage;

                x += 32*3;
                if (i == 2 || i == 5) {
                    x = 32;
                    y += 32*4;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getObjectImages() {


        UtilityTool utilityTool = new UtilityTool();

        try {
            objectImage[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/axe.png")));
            objectImage[0] = utilityTool.scaleImage(objectImage[0], 32, 32);

            objectImage[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/boots.png")));
            objectImage[1] = utilityTool.scaleImage(objectImage[1], 32, 32);

            objectImage[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/chest.png")));
            objectImage[2] = utilityTool.scaleImage(objectImage[2], 32, 32);

            objectImage[3] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/portal.png")));
            objectImage[3] = utilityTool.scaleImage(objectImage[3], 32, 32);

            objectImage[4] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/key.png")));
            objectImage[4] = utilityTool.scaleImage(objectImage[4], 32, 32);

            objectImage[5] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/spidernet.png")));
            objectImage[5] = utilityTool.scaleImage(objectImage[5], 32, 32);

            objectImage[6] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/hole.png")));
            objectImage[6] = utilityTool.scaleImage(objectImage[6], 32, 32);

            objectImage[7] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/HP.png")));
            objectImage[7] = utilityTool.scaleImage(objectImage[7], 32, 32);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMapImages() {


        UtilityTool utilityTool = new UtilityTool();

        try {
            mapImage[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map0.png")));
            mapImage[0] = utilityTool.scaleImage(mapImage[0], 240, 240);

            mapImage[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map1.png")));
            mapImage[1] = utilityTool.scaleImage(mapImage[1], 240, 240);

            mapImage[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map2.png")));
            mapImage[2] = utilityTool.scaleImage(mapImage[2], 240, 240);

            mapImage[3] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map3.png")));
            mapImage[3] = utilityTool.scaleImage(mapImage[3], 240, 240);

            mapImage[4] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map4.png")));
            mapImage[4] = utilityTool.scaleImage(mapImage[4], 240, 240);

            mapImage[5] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map5.png")));
            mapImage[5] = utilityTool.scaleImage(mapImage[5], 240, 240);

            mapImage[6] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map6.png")));
            mapImage[6] = utilityTool.scaleImage(mapImage[6], 240, 240);

            mapImage[7] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map7.png")));
            mapImage[7] = utilityTool.scaleImage(mapImage[7], 240, 240);

            mapImage[8] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map8.png")));
            mapImage[8] = utilityTool.scaleImage(mapImage[8], 240, 240);

            mapImage[9] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map9.png")));
            mapImage[9] = utilityTool.scaleImage(mapImage[9], 240, 240);

            mapImage[10] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map10.png")));
            mapImage[10] = utilityTool.scaleImage(mapImage[10], 240, 240);

            mapImage[11] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map11.png")));
            mapImage[11] = utilityTool.scaleImage(mapImage[11], 240, 240);

            mapImage[12] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map12.png")));
            mapImage[12] = utilityTool.scaleImage(mapImage[12], 240, 240);

            mapImage[13] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map13.png")));
            mapImage[13] = utilityTool.scaleImage(mapImage[13], 240, 240);

            mapImage[14] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map14.png")));
            mapImage[14] = utilityTool.scaleImage(mapImage[14], 240, 240);

            mapImage[15] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map15.png")));
            mapImage[15] = utilityTool.scaleImage(mapImage[15], 240, 240);

            mapImage[16] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map16.png")));
            mapImage[16] = utilityTool.scaleImage(mapImage[16], 240, 240);

            mapImage[17] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map17.png")));
            mapImage[17] = utilityTool.scaleImage(mapImage[17], 240, 240);

            mapImage[18] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map18.png")));
            mapImage[18] = utilityTool.scaleImage(mapImage[18], 240, 240);

            mapImage[19] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map19.png")));
            mapImage[19] = utilityTool.scaleImage(mapImage[19], 240, 240);

            mapImage[20] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/map/map20.png")));
            mapImage[20] = utilityTool.scaleImage(mapImage[20], 240, 240);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
