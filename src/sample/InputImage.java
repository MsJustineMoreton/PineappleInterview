package sample;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.util.HashMap;

public class InputImage {
    BufferedImage bufferedImage;

    HashMap<String,RGBMap> dividedInputImage =new HashMap<>();
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public BufferedImage transformInputImage(){
        BufferedImage workWithImage=bufferedImage;
        Graphics2D graphics2D=workWithImage.createGraphics();
        //get max width that is divisible by 20
        int xAxis=setAxis(bufferedImage.getWidth())/20;
        //get max height which is divisible by 20
        int yAxis=setAxis(bufferedImage.getHeight())/20;
        for(int i=0;i<setAxis(bufferedImage.getWidth());i+=xAxis){
            for(int j=0;j<setAxis(bufferedImage.getHeight());j+=yAxis){
                BufferedImage subImage=bufferedImage.getSubimage(i,j,xAxis,yAxis);

                try{


                    ProcessedTile processedTile = new ProcessedTile(xAxis,yAxis);
                    for(int w=0;w<xAxis;w++){
                        for(int h=0; h<yAxis;h++){
                            int RGB=subImage.getRGB(w,h);
                            //bitwise & operation and shifting 16 to the right gets the red color only
                            int red=((RGB&0x00ff0000) >> 16);
                            int green=(RGB& 0x0000ff00) >> 8;
                            int blue=RGB& 0x000000ff;
                            processedTile.redPixels[w][h]=red;
                            processedTile.greenPixels[w][h]=green;
                            processedTile.bluePixels[w][h]=blue;

                            // System.out.println("red:"+red+" green"+green+" blue:"+blue);
                        }
                    }

                    dividedInputImage.put(""+i+"-"+j,processedTile.setAverageRGB(processedTile,xAxis,yAxis));
                    String fileName=ImageDatabase.compareDistance(processedTile.setAverageRGB(processedTile,xAxis,yAxis),i,j);
                    BufferedImage image = (ImageIO.read(new File("DB/"+fileName)));
                    BufferedImage resized=resize(image,yAxis,xAxis);
                    graphics2D.drawImage(resized,i,j,null);





                }catch (Exception ioe){
                    System.out.println(ioe);
                }
            }
        }
        return workWithImage;
    }
    public int setAxis(int dimension){
        int remain=dimension%20;
        if(remain==0){
            return dimension;
        }
        return dimension-remain;
    }
    public BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}
