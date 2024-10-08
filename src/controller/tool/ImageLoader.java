package controller.tool;

import model.entity.Player;
import model.tile.Tile;
import model.tile.TileManager;
import view.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageLoader {

    GamePanel gp;
    String imagePath;
    int numImages;
    int imageWidth;
    int imageHeight;

    int[] collisionTile = {5, 8, 13, 16, 17, 18, 19, 20, 21, 25, 27, 28, 29, 33, 34, 35, 37, 41, 42, 43, 49, 50, 51, 57, 58, 59, 60, 61, 65, 66, 67, 68, 69, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 95, 96, 97, 98, 99, 100, 101, 102, 103, 106, 108, 109 ,113, 114, 115, 116, 118 ,119, 120, 121, 122, 123 ,124, 125, 129, 130, 131, 132 ,133, 134 ,135, 136, 137, 138, 139};

    public ImageLoader(GamePanel gp, String imagePath, int numImages, int imageWidth, int imageHeight) {
        this.gp = gp;
        this.imagePath = imagePath;
        this.numImages = numImages;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public void PlayerImageLoader(Player player, int startX, int startY) {
        UtilityTool utilityTool = new UtilityTool();

        try {
            BufferedImage bigImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

            int index = 0;
            int x, y;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    x = startX + j * imageWidth;
                    y = startY + i * imageHeight;
                    BufferedImage subImage = bigImage.getSubimage(x, y, imageWidth, imageHeight);
                    subImage = utilityTool.scaleImage(subImage, gp.titleSize, gp.titleSize);

                    if (index >= 0 && index < 3) {
                        player.down[index] = subImage;
                    } else if (index >= 3 && index < 6) {
                        player.left[index - 3] = subImage;
                    } else if (index >= 6 && index < 9) {
                        player.right[index - 6] = subImage;
                    } else if (index >= 9 && index < 12) {
                        player.up[index - 9] = subImage;
                    }
                    index++;
                    if (index >= numImages) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void TileImageLoader(TileManager tileManagers) {
        UtilityTool utilityTool = new UtilityTool();
        int index = 1;

        try {
            BufferedImage bigImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));

            for (int y = 0; y < bigImage.getHeight(); y += imageHeight) {
                for (int x = 0; x < bigImage.getWidth(); x += imageWidth) {
                    if (index > numImages) {
                        break;
                    }

                    tileManagers.tile[index] = new Tile();
                    BufferedImage subImage = bigImage.getSubimage(x, y, imageWidth, imageHeight);
                    subImage = utilityTool.scaleImage(subImage, gp.titleSize, gp.titleSize);
                    tileManagers.tile[index].image = subImage;
                    tileManagers.tile[index].collision = false;
                    index++;
                }
            }

            for (int j : collisionTile) {
                if (j < tileManagers.tile.length && tileManagers.tile[j] != null) {
                    tileManagers.tile[j].collision = true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
