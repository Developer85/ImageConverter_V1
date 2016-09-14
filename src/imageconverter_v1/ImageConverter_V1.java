package imageconverter_v1;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author usuari
 */
public class ImageConverter_V1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<File> llistaFitxers = new ArrayList<>();
        
        cercarFitxers(new File("."), llistaFitxers);
        llistarFitxers(llistaFitxers);
        convertirFitxers(llistaFitxers);
    }
    
    public static void cercarFitxers(File ruta, ArrayList<File> llistaFitxers) {
        File[] elements = ruta.listFiles();
        String ext = ".jpg";
        
        for (File f : elements) {
            if (f.isDirectory()) {
                cercarFitxers(f, llistaFitxers);
            } else if (f.isFile() && f.getName().endsWith(ext)) {
                llistaFitxers.add(f);
            } 
        }
    }
    
    public static void llistarFitxers(ArrayList<File> llistaFitxers) {        
        for (File f : llistaFitxers) {
            System.out.println(f.getPath());
        }
        
        System.out.println("");
    }
    
    public static void convertirFitxers(ArrayList<File> llistaFitxers) {
        String ruta, nom, ext = ".jpg";
        int posicio;
        
        for (File f : llistaFitxers) {
            ruta = f.getParent() + "/";
            posicio = f.getName().lastIndexOf(".");
            nom = f.getName().substring(0, posicio);
            guardarFitxer(f, ruta, nom, ext);
        }
    }
    
    private static void guardarFitxer(File f, String ruta, String nom, String ext) {
        try {
            BufferedImage origImg = ImageIO.read(f);
            int tipus = origImg.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : origImg.getType();
            int origWidth = origImg.getWidth();
            int origHeight = origImg.getHeight();
            String orientacio = obtenirOrientacio(origWidth, origHeight);
            float ratioX, ratioY, ratio;
            
            if (orientacio.equals("horitzontal")) {
                ratioX = (float) 1600 / origWidth;
                ratioY = (float) 1200 / origHeight;
            } else {
                ratioX = (float) 1200 / origWidth;
                ratioY = (float) 1600 / origHeight;        
            }
            
            ratio = Math.min(ratioX, ratioY);
            
            int newWidth = (int)(origWidth * ratio);
            int newHeight = (int)(origHeight * ratio);
//            System.out.println(f.getPath() + " --> " + origWidth + "x" + origHeight +
//                    " --> " + newWidth + "x" + newHeight + " --> " + ratio);
            
            BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, tipus);
            Graphics2D g = resizedImg.createGraphics();
//            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(origImg, 0, 0, newWidth, newHeight, null);
            g.dispose();
            ImageIO.write(resizedImg, "JPEG", new File(ruta + nom + "_resized" + ext));
            
        } catch (IOException ex) {
            System.out.println("Error!!! S'ha produït un error d'E/S.");
        }
    }
    
    private static String obtenirOrientacio(int ample, int altura) {
        if (ample > altura) return "horitzontal";
        return "vertical";
    }
}
