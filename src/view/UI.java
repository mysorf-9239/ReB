package view;

import model.Object.Obj_Heart;
import model.Object.Obj_Key;
import model.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica;

    //Obj image
    BufferedImage heart_full, heart_half, heart_blank;


    //Message
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;

    //Other
    public boolean gameFnished = false;
    double playTime;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    public int titleScreenState = 0;
    public int commanNum = 0;


    public UI (GamePanel gp) {

        this.gp = gp;


        //Define font
        try {
            InputStream inputStream = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException e) {
            e.getStackTrace();
        } catch (FontFormatException e) {
            e.getStackTrace();
        }


        //Create hub object
        Entity heart = new Obj_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

    }

    public void showMess(String text) {

        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        g2.setColor(Color.white);
        //Title state
        if (gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        //Play State
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
        }
        //Pause state
        if (gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }
    }

    public void drawPlayerLife() {

        int x = gp.titleSize/2;
        int y = gp.titleSize/2;

        int i = 0;

        //Draw max blank heart
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.titleSize;
        }

        //Reset x, y
        x = gp.titleSize/2;
        y = gp.titleSize/2;
        int life = gp.player.life;

        //Draw current life
        if (life > 0 && life <= 1) {
            g2.drawImage(heart_half, x, y, null);
        }
        else if (life > 1 && life <= 2) {
            g2.drawImage(heart_full, x, y, null);
        }
        else if (life > 2 && life <= 3) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_half, x + gp.titleSize, y, null);
        }
        else if (life > 3 && life <= 4) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_full, x + gp.titleSize, y, null);
        }
        else if (life > 4 && life <= 5) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_full, x + gp.titleSize, y, null);
            g2.drawImage(heart_half, x + gp.titleSize*2, y, null);
        }else if (life > 5) {
            g2.drawImage(heart_full, x, y, null);
            g2.drawImage(heart_full, x + gp.titleSize, y, null);
            g2.drawImage(heart_full, x + gp.titleSize*2, y, null);
        }


    }

    public void drawTitleScreen() {
        //Background
        g2.setColor(new Color(70, 120, 80));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);



        if (titleScreenState == 0) {

            //Title name
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
            String text = "Fugitive";
            int x = getXforCenterText(text);
            int y = gp.titleSize*3;

            //Shadow
            g2.setColor(Color.black);
            g2.drawString(text, x+5, y+5);

            //Main Color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //Character image
            x = gp.screenWidth/2 - gp.titleSize;
            y += gp.titleSize*2;
            g2.drawImage(gp.player.down[1], x, y, gp.titleSize*2, gp.titleSize*2, null);

            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

            text = "New Game";
            x = getXforCenterText(text);
            y += gp.titleSize * 8;
            g2.drawString(text, x, y);
            if (commanNum == 0) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }

            text = "Load Game";
            x = getXforCenterText(text);
            y += gp.titleSize;
            g2.drawString(text, x, y);
            if (commanNum == 1) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }

            text = "Mode";
            x = getXforCenterText(text);
            y += gp.titleSize;
            g2.drawString(text, x, y);
            if (commanNum == 2) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }

            text = "Setting";
            x = getXforCenterText(text);
            y += gp.titleSize;
            g2.drawString(text, x, y);
            if (commanNum == 3) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }

            text = "Quit";
            x = getXforCenterText(text);
            y += gp.titleSize;
            g2.drawString(text, x, y);
            if (commanNum == 4) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }
        }
        else if (titleScreenState == 1) {

            g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

            String text = "Select your mode";
            int x = getXforCenterText(text);
            int y = gp.titleSize*3;
            g2.drawString(text, x, y);

            text = "Endless";
            x = getXforCenterText(text);
            y += gp.titleSize*9;
            g2.drawString(text, x, y);
            if (commanNum == 0) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }

            text = "Overcome";
            x = getXforCenterText(text);
            y += gp.titleSize;
            g2.drawString(text, x, y);
            if (commanNum == 1) {
                g2.drawString(">", x - gp.titleSize * 2 / 3, y);
            }
        }

    }

    public void drawPauseScreen() {

        String text = "Pause";
        int x = getXforCenterText(text);

        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public int getXforCenterText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;

        return x;
    }

//    public void draw(Graphics2D g2) {
//
//        if (gameFnished == true) {
//
//            g2.setColor(Color.red);
//            g2.setFont(arial_40);
//
//            String text;
//            int textLength;
//            int x, y;
//
//            text = "You WIN";
//            textLength = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
//
//            x = gp.screenWidth/2 - textLength/2;
//            y = gp.screenHeight/2 - gp.titleSize/3;
//            g2.drawString(text, x, y);
//        }
//        else {
//            g2.setFont(arial_40);
//            g2.setColor(Color.WHITE);
//            g2.drawImage(keyImage, gp.titleSize / 2, gp.titleSize / 2, gp.titleSize, gp.titleSize, null);
//            g2.drawString("x: " + gp.player.hasKey, 74, 65);
//
//            playTime+= (double) 1/60;
//            g2.drawString("Time: " + decimalFormat.format(playTime), gp.titleSize*16, 65);
//
//            if (messageOn == true) {
//
//                g2.drawString(message, 100, 100);
//
//                messageCounter++;
//                if (messageCounter > 100) {
//                    messageCounter = 0;
//                    messageOn = false;
//                }
//            }
//        }
//    }

}
