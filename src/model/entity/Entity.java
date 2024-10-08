package model.entity;

import controller.tool.UtilityTool;
import view.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Entity {
    //Panel
    GamePanel gp;

    //Image
    private static final int NUM_FRAMES = 4;
    public BufferedImage[] up = new BufferedImage[NUM_FRAMES];
    public BufferedImage[] down = new BufferedImage[NUM_FRAMES];
    public BufferedImage[] left = new BufferedImage[NUM_FRAMES];
    public BufferedImage[] right = new BufferedImage[NUM_FRAMES];
    public BufferedImage image, image2, image3;

    //STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    public boolean collision = false;

    //Counter
    public int spriteCounter = 0;

    //Character attribute
    public String name;
    public int speed;
    public int maxLife;
    public int life;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collidisionOn = false;


    public Entity (GamePanel gp) {

        this.gp = gp;

        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 5;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 48;
        solidArea.height = 22;

    }

    public BufferedImage setup(String imageName) {

        UtilityTool utilityTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageName + ".png")));
            image = utilityTool.scaleImage(image, gp.titleSize, gp.titleSize);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return image;
    }

    public void setAction() {}

    public void update() {

        setAction();

        collidisionOn = false;
        gp.collisionChecker.CheckTile(this);
        gp.collisionChecker.CheckObject(this, false);

        //IF COLLISION IS FALSE -> MOVE
        if (!collidisionOn) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        spriteCounter++;
        if (spriteCounter > 5) {
            if (spriteNum == 1) { spriteNum = 2;}
            if (spriteNum == 2) { spriteNum = 1;}
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image;

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        image = up[1];
        image = switch (direction) {
            case "up" -> switch (spriteNum) {
                case 1 -> up[1];
                case 2 -> up[2];
                default -> image;
            };
            case "down" -> switch (spriteNum) {
                case 1 -> down[1];
                case 2 -> down[2];
                default -> image;
            };
            case "left" -> switch (spriteNum) {
                case 1 -> left[1];
                case 2 -> left[2];
                default -> image;
            };
            case "right" -> switch (spriteNum) {
                case 1 -> right[1];
                case 2 -> right[2];
                default -> image;
            };
            default -> image;
        };
        g2.drawImage(image, screenX, screenY, gp.titleSize, gp.titleSize, null);
    }

}
