import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;


public class CoordsToJpg{
    
    public static Color getColor(String col, int opacity) {
        if (col.charAt(0) == '7') col = col.substring(1);
        
        Color color = Color.WHITE;
        switch (col.toLowerCase()) {
            case "vlred":
                color = new Color(255,102,102,opacity);
                break;
            case "lred":
                color = new Color(255,51,51,opacity);
                break;
            case "red":
                color = new Color(255,0,0,opacity);
                break;
            case "dred":
                color = new Color(204,0,0,opacity);
                break;
            case "vlblue":
                color = new Color(51,204,255,opacity);
                break;
            case "lblue":
                color = new Color(51,153,255,opacity);
                break;
            case "blue":
                color = new Color(0,0,255,opacity);
                break;
            case "dblue":
                color = new Color(0,0,204,opacity);
                break;
            case "vlgreen":
                color = new Color(102,255,102,opacity);
                break;
            case "lgreen":
                color = new Color(0,255,51,opacity);
                break;
            case "green":
                color = new Color(0,255,0,opacity);
                break;
            case "dgreen":
                color = new Color(0,153,0,opacity);
                break;
            case "lyellow":
                color = new Color(255, 255, 153,opacity);
                break;
            case "yellow":
                color = new Color(255, 255, 0,opacity);
                break;
            case "dyellow":
                color = new Color(255, 204, 0,opacity);
                break;
            case "lorange":
                color = new Color(255, 153, 0,opacity);
                break;
            case "orange":
                color = new Color(255, 102, 0,opacity);
                break;
            case "gold":
                color = new Color(255, 204, 51,opacity);
                break;
            case "lgrey":
                color = new Color(204, 204, 204,opacity);
                break;
            case "dgrey":
                color = new Color(153, 153, 153,opacity);
                break;
            case "vdgrey":
                color = new Color(51, 51, 51,opacity);
                break;
            case "lbrown":
                color = new Color(153, 102, 0,opacity);
                break;
            case "dbrown":
                color = new Color(102, 0, 0,opacity);
                break;
            case "black":
                color = new Color(0, 0, 0,opacity);
                break;
            case "purple":
                color = new Color(102, 0, 153,opacity);
                break;
            case "maroon":
                color = new Color(153, 0,  0,opacity);
                break;
            case "salmon":
                color = new Color(255, 102, 102,opacity);
                break;
            case "magneta":
                color = new Color(255, 0, 255,opacity);
                break;
            case "magneta4":
                color = new Color(139, 0, 139,opacity);
                break;
            default:
                System.out.println("Color " + col.toLowerCase() + " not found!");
        }
        return color;
    }
    

    
   public static int[] buildArrowX(int l, char strand, int offset, String color){
       int [] polygon = new int[7];
       int x1 = offset;
       
       if(l < 10) l = 10;
       if(x1 < 50 && (x1 + l > 50)) x1 = x1 - ((x1 + l)-50);

       // make the head inclusive with the size DONE
       if (strand == '+'){
           polygon[0] = x1;
           polygon[1] = x1 + l -10;
           polygon[2] = x1 + l -10;
           polygon[3] = x1 + l; //arrow forward
           polygon[4] = x1 + l - 10;
           polygon[5] = x1 + l - 10;
           polygon[6] = x1;
       }
       else if (strand == '-' ) {
           x1 += 10;
           
           polygon[0] = x1;
           polygon[1] = x1;
           polygon[2] = x1 - 10;
           polygon[3] = x1;
           polygon[4] = x1;
           polygon[5] = x1 + l-10;
           polygon[6] = x1 + l-10;
       }
       
       return polygon;
   }
    
    public static int[] buildArrowY(char strand, int offset, String color){
        int [] polygon = new int[7];
        int y1 = offset * 15;
        
        if (strand == '+' ){
            polygon[0] = y1 +4;
            polygon[1] = y1 + 4;
            polygon[2] = y1;
            polygon[3] = y1 + 8; //arrow forward
            polygon[4] = y1 + 15;
            polygon[5] = y1 + 11;
            polygon[6] = y1 + 11;
        }
        else if (strand == '-') {
            polygon[0] = y1 + 4;
            polygon[1] = y1;
            polygon[2] = y1 + 8;
            polygon[3] = y1 + 15;
            polygon[4] = y1 + 11;
            polygon[5] = y1 + 11;
            polygon[6] = y1 + 4;
        }
        

        
        return polygon;
    }

   public static  void saveImage() throws IOException {

	BufferedImage bufferedImage = new BufferedImage(300,300, BufferedImage.TYPE_INT_RGB);
	Graphics2D g2d = bufferedImage.createGraphics();

	int [] polygonXs = new int[7];
    int [] polygonYs = new int[7];

    int [] squareXs = new int[4];
    int [] squareYs = {0, 0, 15, 15};
	Shape shape;
	File img_file;

	BufferedReader br = new BufferedReader(new FileReader("oc.txt"));
	String line;
	String img_name = "";
       String [] tokens;
       String [] names;
       int count = 0;
       int row_number = 0;
       g2d.setColor(Color.WHITE);
       g2d.fillRect(0, 0, 300, 300);
       int first = 0;
	while ((line = br.readLine()) != null) {
        count++;

        names = line.split("\t"); // names of focus genes
        
        if(names[0].equals("new")){
            // when we reach new figure:
            if(!img_name.equals("")){
                img_file = new File("images/"+ img_name + ".jpg");
                g2d.drawImage(bufferedImage, 0,0,null);
                ImageIO.write(bufferedImage, "jpg", img_file);
            }
            
            // for every new figure:
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, 300, 300);
            
            row_number = 0;
            
            first = 0;
            continue;
        }
        img_name = names[0]+ '_' + names[1];
        float opacity = Float.parseFloat(names[2]);
        
        line = br.readLine();
        tokens = line.split("\t");
        if(tokens.length <5){
            System.out.println(tokens.length);
            System.out.println(Arrays.toString(tokens));
            continue;
        }
        
        int num_arrows = tokens.length / 5; // verify input file structure

        int [] offsets = new int[num_arrows];
        int [] sizes = new int[num_arrows];
        char [] strands = new char[num_arrows];
        String current_name = "";
        int focus_index = -1;
        String [] colors =  new String[num_arrows];
        
        
        
        int k = 0;
        // read into ArrayList, maybe create an objece of 3-tuple
        for(int i = 0; i < tokens.length; i+=5) {
            if(tokens.length  <3 || tokens[i+2] == null){
                System.out.println(tokens.length);
                System.out.println(Arrays.toString(tokens));
            }
                
            strands[k] = tokens[i+2].charAt(0);
   
            offsets[k] = Integer.parseInt(tokens[i]);
            sizes[k] = Integer.parseInt(tokens[i+1]);

            colors[k] = tokens[i+3];
            current_name = tokens[i+4];
            k++;
        }
        
        for (int i = 0; i < num_arrows; i++){

            polygonXs = buildArrowX(sizes[i], strands[i], offsets[i], colors[i]);

            polygonYs = buildArrowY(strands[i], row_number, colors[i]);
            g2d.setColor(getColor(colors[i],(int)(opacity*255)));
            shape = new Polygon(polygonXs, polygonYs, polygonXs.length);

            g2d.fill(shape);
            
        }
        
        row_number++;
	}
       img_file = new File("images/"+ img_name + ".jpg");
       g2d.drawImage(bufferedImage, 0,0,null);
       ImageIO.write(bufferedImage, "jpg", img_file);
       System.out.println(count);
   } 
   public static void main(String[] args) {
	try {
		saveImage();
	}
	catch (IOException ex) {
		System.out.println("IO Exception Caught!");
	}
   }
}
