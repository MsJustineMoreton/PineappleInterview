package sample;

import sun.misc.FloatingDecimal;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageDatabase {
    private static ImageDatabase imageDatabaseInstance = new ImageDatabase();
    private HashMap<String,RGBMap> tiles=new HashMap<>();
    public static ImageDatabase getInstance() {
            return imageDatabaseInstance;
    }
    public static HashMap<String,RGBMap> getTilesInstance(){
        return  imageDatabaseInstance.tiles;
    }


    protected ImageDatabase() {
        File file;
        BufferedImage image;
        String[] list=new File("DB/").list();

       try{

           for(int listItem=0;listItem<list.length;listItem++){
               file=new File("DB/"+list[listItem]);
               image = ImageIO.read(file);
               Raster raster=image.getData();
               int Height = raster.getHeight();
               int Width = raster.getWidth();
               ProcessedTile processedTile = new ProcessedTile(Width,Height);
               for(int i=0;i<Width;i++){
                   for(int j=0; j<Height;j++){
                       int RGB=image.getRGB(i,j);
                       //bitwise & operation and shifting 16 to the right gets the red color only
                       int red=((RGB&0x00ff0000) >> 16);
                       int green=(RGB& 0x0000ff00) >> 8;
                       int blue=RGB& 0x000000ff;
                       processedTile.redPixels[i][j]=red;
                       processedTile.greenPixels[i][j]=green;
                       processedTile.bluePixels[i][j]=blue;

                      // System.out.println("red:"+red+" green"+green+" blue:"+blue);
                   }
               }
               tiles.put(""+list[listItem],processedTile.setAverageRGB(processedTile,Width,Height));
           }


       }catch (IOException ioe){
            System.out.println(ioe);
       }
//System.out.println("x");
    }
    public static String compareDistance(RGBMap rgbMap,int xIndex,int yIndex){
        CIELab cieLab=CIELab.getInstance();
        HashMap<String,RGBMap> DBTiles=ImageDatabase.getTilesInstance();
        float minLab=0;
        String closestDBTile="";
        float[] inputSubImageLab=cieLab.fromRGB(new float[]{rgbMap.red,rgbMap.green,rgbMap.blue});

        for(Map.Entry<String,RGBMap> entry: DBTiles.entrySet()){
            entry.getKey();
            RGBMap rgbMap1=entry.getValue();
            float[] tileLab=cieLab.fromRGB(new float[]{rgbMap1.red,rgbMap1.green,rgbMap1.blue});
            /*Float inputLab=Math.abs(inputSubImageLab[0]*inputSubImageLab[1]*inputSubImageLab[2]);
            Float tile
            Float inputLSquared=(inputSubImageLab[0]*inputSubImageLab[0]);
            Float tileLSquared=(tileLab[0]*tileLab[0]);
            Float inputASquared=(inputSubImageLab[1]*inputSubImageLab[1]);
            Float tileASquared=(tileLab[1]*tileLab[1]);
            Float inputBSquared=(inputSubImageLab[2]*inputSubImageLab[2]);
            Float tileBSquared=(tileLab[2]*tileLab[2]);
            Float lDiff=(inputLSquared-tileLSquared);
            Float aDiff=(inputASquared-tileASquared);
            Float bDiff=(inputBSquared-tileBSquared);**/
            Float distance=(Math.abs((inputSubImageLab[0]*inputSubImageLab[1]*inputSubImageLab[2])-(tileLab[0]*tileLab[1]*tileLab[2])));
            if(minLab==0){
                minLab=distance;
            }
            if(distance<minLab){
                minLab=distance;
                closestDBTile=entry.getKey();
            }
        }
    return closestDBTile;
    }



}
