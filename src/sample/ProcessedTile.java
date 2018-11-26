package sample;

public class ProcessedTile {

    public int[][] redPixels;
    public int[][] greenPixels;
    public int[][] bluePixels;
    public ProcessedTile(int width, int height){
        redPixels=new int[width][height];
        greenPixels=new int[width][height];
        bluePixels=new int[width][height];
    }
    public RGBMap setAverageRGB(ProcessedTile processedTile, int Width, int Height){
        RGBMap rgbMap=new RGBMap();
        int red=0;
        int green=0;
        int blue=0;
        for(int i=0;i<Width;i++){
            for (int j=0;j<Height;j++){
                red+=(processedTile.redPixels[i][j]);
                green+=(processedTile.greenPixels[i][j]);
                blue+=(processedTile.bluePixels[i][j]);
            }
        }
        rgbMap.setRed(red/(Width*Height));
        rgbMap.setGreen(green/(Width*Height));
        rgbMap.setBlue(blue/(Width*Height));
        return rgbMap;
    }

}
