package model.entity;

import controller.Config;
import controller.tool.ImageLoader;
import controller.KeyHandler;
import view.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

    KeyHandler keyHandler;
    ImageLoader imageLoader;
    Config config = new Config(gp);

    public final int screenX;
    public final int screenY;

    public static int characterNum;

    public final int numImage = 12;
    public final int imageWidth = 32;
    public final int imageHeight = 32;
    public static int imageStartX;
    public static int imageStartY = 500;

    public static int furthestY;

    public int hasKey = 0;
    public int hasAxe = 1;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        super(gp);

        //Main default andres
        screenX = gp.screenWidth/2;
        screenY = gp.screenHeight/2;

        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 21;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 24;
        solidArea.height = 27;

        this.keyHandler = keyHandler;
        config.getCharacter();
    }

    public void setDefaultValues() {
        //Define the start screen's andres
        switch (GamePanel.currentMap) {

            case 0:
                worldX = gp.titleSize * 20;
                worldY = gp.titleSize * 489;
                break;
            case 1:
                worldX = gp.titleSize * 19;
                worldY = gp.titleSize * 29;
                break;
            case 6:
                worldX = gp.titleSize * 19;
                worldY = gp.titleSize * 29;
                break;
            case 9:
                worldX = gp.titleSize * 20;
                worldY = gp.titleSize * 29;
                break;
            case 10:
                worldX = gp.titleSize * 2;
                worldY = gp.titleSize * 2;
                break;
            case 11:
                worldX = gp.titleSize * 25;
                worldY = gp.titleSize * 2;
                break;
            case 12:
            case 13:
            case 14:
            case 15:
                worldX = gp.titleSize * 3;
                worldY = gp.titleSize * 3;
                break;
            case 20:
                worldX = gp.titleSize * 2;
                worldY = gp.titleSize * 2;
                break;
            default:
                worldX = gp.titleSize * 21;
                worldY = gp.titleSize * 29;
                break;

        }

        speed = gp.titleSize/5;
        direction = "up";

        //Player life
        maxLife= 6;
        life = maxLife;
    }

    public void setDefaultPositions() {

        worldX = gp.titleSize * 20;
        worldY = gp.titleSize * (GamePanel.maxWorldRow - 11);
        speed = gp.titleSize/5;
        direction = "up";
        life = maxLife;

        KeyHandler.upPressed = false;
        KeyHandler.downPressed = false;
        KeyHandler.leftPressed = false;
        KeyHandler.rightPressed = false;
        KeyHandler.movingKeyPressed = false;
    }

    public void getPlayerImage() {

        switch (characterNum) {
            case 0:
                imageStartX = 0;
                imageStartY = 0;
                break;
            case 1:
                imageStartX = 3*imageWidth;
                imageStartY = 0;
                break;
            case 2:
                imageStartX = 6*imageWidth;
                imageStartY = 0;
                break;
            case 3:
                imageStartX = 0;
                imageStartY = 4*imageHeight;
                break;
            case 4:
                imageStartX = 3*imageWidth;
                imageStartY = 4*imageHeight;
                break;
            case 5:
                imageStartX = 6*imageWidth;
                imageStartY = 4*imageHeight;
                break;
            case 6:
                imageStartX = 0;
                imageStartY = 8*imageHeight;
                break;
        }

        imageLoader = new ImageLoader(gp, "/player/manyCharacter01.png", numImage, imageWidth, imageHeight);
        imageLoader.PlayerImageLoader(this, imageStartX, imageStartY);
    }

    public void update() {

        gp.eventHandler.poisonMistCheck();

        //Move
        if(KeyHandler.upPressed || KeyHandler.downPressed || KeyHandler.leftPressed || KeyHandler.rightPressed)
        {
            //GamePanel.isMove = true;
            if (KeyHandler.upPressed) {
                direction = "up";
            }
            if (KeyHandler.downPressed) {
                direction = "down";
            }
            if (KeyHandler.leftPressed) {
                direction = "left";
            }
            if (KeyHandler.rightPressed) {
                direction = "right";
            }

            //CHECK TILE COLLISION
            collidisionOn = false;
            gp.collisionChecker.CheckTile(this);

            //CHECK OBJ COLLISION
            int objIndex = gp.collisionChecker.CheckObject(this, true);
            pickUpObject(objIndex);

            //CHECK EVENT
            gp.eventHandler.checkEvent();

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
            } else {
                switch (direction) {
                    case "up":
                        KeyHandler.upPressed = false;
                        break;
                    case "down":
                        KeyHandler.downPressed = false;
                        break;
                    case "left":
                        KeyHandler.leftPressed = false;
                        break;
                    case "right":
                        KeyHandler.rightPressed = false;
                        break;
                }
                KeyHandler.movingKeyPressed = false;
            }
            spriteCounter++;
            if (spriteCounter > 4) {
                if (spriteNum < 3) {
                    spriteNum++;
                } else {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

            if (worldY < furthestY) {
                furthestY = worldY;
            }
        }

        //Life and Die
        if (life > maxLife) {
            life = maxLife;
        }
        if (life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commanNum = -1;
            gp.stopMusic();
            //Play end game music (index = i)
            gp.playSE(3);
        }
    }

    public void pickUpObject(int i) {

        if (i != 999) {
            String objectName = gp.obj[GamePanel.currentMap][i].name;

            switch (objectName) {
                case "Key":
                    hasKey++;
                    gp.obj[GamePanel.currentMap][i] = null;
                    gp.ui.showMess("You got a key");
                    gp.playSE(1);
                    break;
                case "Portal":
                    gp.ui.commanNum = -1;
                    gp.gameState = gp.winState;
                    gp.stopMusic();
                    gp.playSE(6);
                    break;
                case "Boots":
                    speed += 2;
                    gp.obj[GamePanel.currentMap][i] = null;
                    gp.ui.showMess("Speed up");
                    gp.playSE(1);
                    break;
                case "Chest":
                    if (hasKey > 0) {
                        GamePanel.totalScore += 500;
                        gp.obj[GamePanel.currentMap][i].collision = false;
                        hasKey--;
                        gp.obj[GamePanel.currentMap][i] = null;
                        gp.ui.showMess("You received some score");
                        gp.playSE(1);
                    }
                    break;
                case "Axe":
                    hasAxe++;
                    gp.obj[GamePanel.currentMap][i] = null;
                    gp.ui.showMess("You got an axe");
                    gp.playSE(1);
                    break;
                case "Spidernet":
                    if (speed > 2) {
                        speed -= 2;
                    } else {
                        speed = 1;
                    }
                    gp.obj[GamePanel.currentMap][i] = null;
                    gp.ui.showMess("Speed down");
                    gp.playSE(1);
                    break;
                case "Hole":
                    if (life > 0) {
                        life -= 1;
                    }
                    gp.obj[GamePanel.currentMap][i] = null;
                    gp.ui.showMess("You are falling");
                    gp.playSE(1);
                    break;
                case "HP":
                    if (life < maxLife) {
                        life += 1;
                    }
                    gp.obj[GamePanel.currentMap][i] = null;
                    gp.ui.showMess("HP up");
                    gp.playSE(1);
                    break;
            }
        }
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = up[0];

        image = switch (direction) {
            case "up" -> switch (spriteNum) {
                case 1 -> up[0];
                case 2 -> up[1];
                case 3 -> up[2];
                default -> image;
            };
            case "down" -> switch (spriteNum) {
                case 1 -> down[0];
                case 2 -> down[1];
                case 3 -> down[2];
                default -> image;
            };
            case "left" -> switch (spriteNum) {
                case 1 -> left[0];
                case 2 -> left[1];
                case 3 -> left[2];
                default -> image;
            };
            case "right" -> switch (spriteNum) {
                case 1 -> right[0];
                case 2 -> right[1];
                case 3 -> right[2];
                default -> image;
            };
            default -> null;
        };
        g2.drawImage(image, screenX, screenY, null);

    }
}
