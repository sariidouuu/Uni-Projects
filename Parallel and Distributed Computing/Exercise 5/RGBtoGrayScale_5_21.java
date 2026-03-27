import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class RGBtoGrayScale_5_21 {
	public static void main(String args[]) {
		
		String fileNameR = null;
		String fileNameW = null;
		
		//Input and Output files using command line arguments
		if (args.length != 3) {
			System.out.println("Usage: java RGBtoGrayScale <file to read > <file to write> <threads>");
			System.exit(1);
		}
		fileNameR = args[0];
		fileNameW = args[1];

		int numThreads = Integer.parseInt(args[2]);
        if (numThreads == 0)
            numThreads = Runtime.getRuntime().availableProcessors();

        RGBThread[] threads = new RGBThread[numThreads];
		
		//The same without command line arguments
		// fileNameR = "original.jpg";
		// fileNameW = "new.jpg";
				
		//Reading Input file to an image
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileNameR));
		} catch (IOException e) {
			System.out.println("Error: Unable to read the input file: " + fileNameR);
            System.exit(1);
		}

        //Start timing
		long start = System.currentTimeMillis(); 
		
		//Coefficinets of R G B to GrayScale
		double redCoefficient = 0.299;
		double greenCoefficient = 0.587;
		double blueCoefficient = 0.114;

		int block = img.getHeight() / numThreads;
        // Με την μέθοδο getHeight() παίρνουμε το ύψος της εικόνας, δηλαδή το πλήθος των γραμμών
        // Εφαρμόζουμε προσπέλαση κατά γραμμές 

		int startPixel, endPixel;
		for (int i = 0; i < numThreads; i++) {
            startPixel = i * block;
            endPixel = startPixel + block;
            if (i == (numThreads - 1))
                endPixel = img.getHeight();
            threads[i] = new RGBThread(startPixel, endPixel, img, redCoefficient, greenCoefficient, blueCoefficient);
            // Starting threads
            threads[i].start();
        }

		// Join threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }
        }
	
	    //Stop timing
		long elapsedTimeMillis = System.currentTimeMillis()-start;

		//Saving the modified image to Output file
		try {
			File file = new File(fileNameW);
			ImageIO.write(img, "jpg", file);
		} catch (IOException e) {}

		System.out.println("Done...");
		System.out.println("time in ms = "+ elapsedTimeMillis);
	}
}

class RGBThread extends Thread{
	private int start, end;
    private BufferedImage img;
    private double redCoefficient, greenCoefficient, blueCoefficient;

    public RGBThread(int start, int end, BufferedImage img, double redCoefficient, double greenCoefficient, double blueCoefficient) {
        this.start = start;
        this.end = end;
        this.img = img;
        this.redCoefficient = redCoefficient;
        this.greenCoefficient = greenCoefficient;
        this.blueCoefficient = blueCoefficient;
    }

	public void run(){
		for (int y = start; y < end; y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                // Retrieving contents of a pixel
                int pixel = img.getRGB(x, y);
                // Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                // Retrieving the R G B values, 8 bits per r,g,b
                // Calculating GrayScale
                int red = (int) (color.getRed() * redCoefficient);
                int green = (int) (color.getGreen() * greenCoefficient);
                int blue = (int) (color.getBlue() * blueCoefficient);

                int rgb = red + green + blue;

                // Creating new Color object
                color = new Color(rgb, rgb, rgb);
                // Setting new Color object to the image
                img.setRGB(x, y, color.getRGB());
            }
        }
	}
}

/*
RESULTS TABLE
Image Name | Size (ΚB) | 1 Thread | 2 Threads | 4 Threads | 8 Threads 
---------------------------------------------------------------------
1house        1.265       122ms       116ms       111ms      121ms
2aerial       1.729       250ms       271ms       223ms      222ms
3tiger        2.805       712ms       618ms       622ms      517ms
4food         2.645       715ms       496ms       456ms      475ms
5landscape    2.934       540ms       492ms       366ms      465ms
6berries      3.999       594ms       571ms       478ms      559ms
7lake         6.253       738ms       641ms       648ms      701ms
*/