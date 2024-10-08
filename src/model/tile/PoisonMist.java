package model.tile;

import view.GamePanel;

import java.awt.*;

public class PoisonMist {

    GamePanel gp;

    public int PoisonMistY;
    public int PoisonMistSpeed;
    private int defaultPoisonMistY;

    private static long poisonSpeedTime = System.currentTimeMillis();
    private static final long SPEED_INTERVAL = 10000;


    public PoisonMist(GamePanel gp) {

        this.gp = gp;
    }

    public void setDefaultPoisonMist() {

        defaultPoisonMistY = (GamePanel.maxWorldRow + 20)*gp.titleSize + gp.titleSize;
        int defaultPoisonMistSpeed = 1;
        PoisonMistY = defaultPoisonMistY;
        PoisonMistSpeed = defaultPoisonMistSpeed;
    }

    public void changeSpeed(){
        long currentTime = System.currentTimeMillis();

        if (PoisonMistSpeed < 6 && currentTime - poisonSpeedTime >= SPEED_INTERVAL) {
            PoisonMistSpeed += 1;
            poisonSpeedTime = currentTime;
        }
    }

    public void update() {

        PoisonMistY -= PoisonMistSpeed;
        changeSpeed();
    }

    public void stop() {

        PoisonMistSpeed = 0;
    }

    public void draw(Graphics2D g2) {

        int drawX = 0;
        int drawY = gp.screenHeight/2 - (gp.player.worldY - PoisonMistY);
        int PoisonMistWidth = gp.maxScreenCol*gp.titleSize;
        int PoisonMistHeight = defaultPoisonMistY - PoisonMistY;

        g2.setColor(Color.BLACK);
        g2.setColor(new Color(120, 120, 200, 150));

        g2.fillRect(drawX, drawY, PoisonMistWidth, PoisonMistHeight);
        g2.setFont(g2.getFont().deriveFont(26F));
        g2.setColor(Color.white);
    }
}
