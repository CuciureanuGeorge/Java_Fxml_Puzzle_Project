package repository;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {

    public List<BufferedImage> buffImages;
    public List<Image> images;
    int currentImage;

    public void loadImages() throws IOException
    {
        this.buffImages.add(ImageIO.read(new FileInputStream( new File("D:\\Anul 2\\Semestrul 1\\Metode Avansate de programare\\IdeaProjects\\Puzzle(Lab5)\\src\\repository\\images\\eric.jpg"))));
        this.buffImages.add(ImageIO.read(new FileInputStream( new File("D:\\Anul 2\\Semestrul 1\\Metode Avansate de programare\\IdeaProjects\\Puzzle(Lab5)\\src\\repository\\images\\jess.jpg"))));

        this.images.add(new Image(getClass().getResource("/repository/images/jess.jpg").toExternalForm()));
        this.images.add(new Image(getClass().getResource("/repository/images/eric.jpg").toExternalForm()));

    }

    public List<BufferedImage> getBuffImages()
    {
        return this.buffImages;
    }

    public Image getCurrentImage()
    {
        Image ci = this.images.get(currentImage);
        currentImage+=1;
        if(currentImage==this.images.size())
            this.currentImage=0;
        return ci;
    }

    public List<Image> splitImage(int n) throws Exception {
        //File file = new File("D:\\Anul 2\\Semestrul 1\\Metode Avansate de programare\\Projects and Labs\\JavaFxFxmlExample\\src\\repository\\images\\eric.jpg"); // I have bear.jpg in my working directory
        //FileInputStream fis = new FileInputStream(file);

        BufferedImage image = buffImages.get(currentImage); //reading the image file

        int rows = n; //You should decide the values for rows and cols variables
        int cols = n;
        int chunks = rows * cols;

        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
        int chunkHeight = image.getHeight() / rows;
        int count = 0;
        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                //Initialize the image array with image chunks
                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

                // draws the image chunk
                Graphics2D gr = imgs[count++].createGraphics();
                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
                gr.dispose();
            }
        }

        List<Image> imgToRet = new ArrayList<>();

        //writing mini images into image files
        for (int i = 0; i < imgs.length; i++) {
            imgToRet.add(SwingFXUtils.toFXImage(imgs[i], null));
        }
        return imgToRet;
    }


    public Repository() throws IOException
    {
        this.currentImage=0;
        this.images = new ArrayList<>();
        this.buffImages = new ArrayList<>();
        loadImages();
    }

}
