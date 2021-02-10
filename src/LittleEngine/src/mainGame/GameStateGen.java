package mainGame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

/**
 *
 * @author Noah Rachdi
 */
public class GameStateGen {
    
    // Set number of states to generate
    static int STATES = 50;
    
    // Set game parameters
    static int fieldsize = 7;            ;
    static int numKnights = 2;
    static int numGnomes = 3;
    
    // Parametrize piece values
    static int xGNOME = -1;
    static int xKNIGHT = -2;
    static int xKING = -3;
    static int GNOME = 1;
    static int KNIGHT = 2;
    static int KING = 3;
    
    // Probabilities
    static double gnomeMove = 0.7;
    static double createGnome = 0.7;
    static double createKnight = 0.8;
    
    /**
     * To be completed
     * @param numStates
     * @param startOpening
     * @return opening position game state (according to static class parameters)
     */
    public static int[][] Gen(int numStates, boolean startOpening) {
        int[][] gameState = new int[fieldsize][fieldsize];
        
        Random rand = new Random();
        
        // Initialize state array with opening position
        if (startOpening) gameState = initOpening();
        else gameState = simpleGen();
        
        // TODO: Choose random piece
        // TODO: Choose random amount of moves
        // TODO: Move piece randomly according to rules
        
        // Return gamestate array
        return gameState;
    }
    
    /**
     * Creates a game state of the opening position according to:
     * static class parameters fieldsize, numKnights, numGnomes and the piece values
     * @return opening position game state (according to static class parameters)
     */
    public static int[][] initOpening(){
        int[][] gameState = new int[fieldsize][fieldsize];
        
        // Placement of kings
        gameState[(fieldsize-1)/2][0] = xKING;
        gameState[(fieldsize-1)/2][fieldsize-1] = KING;

        // Placement of knights
        for (int i = 0; i <= numKnights; i++) {
            int field = ((fieldsize-1)/2) - (numKnights/2) + i;
            if (field != (fieldsize-1)/2){
                gameState[field][0] = xKNIGHT;
                gameState[field][fieldsize-1] = KNIGHT;
            }
        }

        // Placement of gnomes
        for (int i = 0; i < numGnomes; i++) {
            int field = ((fieldsize-1)/2 - (numGnomes-1)/2) + i;
            gameState[field][1] = xGNOME;
            gameState[field][fieldsize-2] = GNOME;
        }
        
        return gameState;
    }
    
    /**
     * Generates a game state according to static class parameters (using gauss dist)
     * Generates both kings, gaussian dist. close to their opening pos
     * Generates knights with likelyhood 'createKnight' (divided by the number of each further created knight)
     * Generates gnomes with likelyhood 'createGnome' (divided by the number of each further created knight)
     * Knights and gnomes are both placed gaussian dist. around their own king
     * @return randomly generated game state
     */
    public static int[][] simpleGen() {
        int[][] gameState = new int[fieldsize][fieldsize];
        Random rand = new Random();
        int xKINGx, xKINGy, KINGx, KINGy, fieldx, fieldy;
        
        // Generate xKing fields gaussian distributed around opening pos, bound to field size
        xKINGx = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize/3, 0), fieldsize - 1);
        xKINGy = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize/3 + (fieldsize - 1) / 2, 0), fieldsize - 1);
        // Place xKing
        gameState[xKINGy][xKINGx] = xKING;
        
        // Generate King fields gaussian distributed around opening pos, bound to field size
        do{
            KINGx = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize/3 + fieldsize - 1, 0), fieldsize - 1);
            KINGy = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize/3 + (fieldsize - 1) / 2, 0), fieldsize - 1);            
        }while(Math.abs(xKINGx - KINGx) + Math.abs(xKINGy - KINGy) < 3); // avoid king placed next to other
        // Place King
        gameState[KINGy][KINGx] = KING;
        
        // Create numKnights Knights for both teams
        for (int i = 1; i <= numKnights; i++) {
            // Create knight with the possibility createKnight, decreasing with each knight
            if (rand.nextDouble() < createKnight/i) {
                // Generate Knight fields gaussian distributed around King, bound to field size
                do {
                    fieldx = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + KINGx, 0), fieldsize - 1);
                    fieldy = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + KINGy, 0), fieldsize - 1);
                } while (gameState[fieldy][fieldx] != 0);
                // Place Knight
                gameState[fieldy][fieldx] = KNIGHT;
            }
            // Create knight with the possibility createKnight, decreasing with each knight
            if (rand.nextDouble() < createKnight/i) {
                // Generate xKnight fields gaussian distributed around xKing, bound to field size
                do {
                    fieldx = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + xKINGx, 0), fieldsize - 1);
                    fieldy = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + xKINGy, 0), fieldsize - 1);
                } while (gameState[fieldy][fieldx] != 0);
                // Place Knight
                gameState[fieldy][fieldx] = xKNIGHT;
            }
        }

        // Create numGnomes Gnomes for both teams
        for (int i = 1; i <= numGnomes; i++) {
            // Create Gnome with the possibility createGnome, decreasing with each Gnome
            if (rand.nextDouble() < createGnome/i) {
                // Generate Gnome fields gaussian distributed around King, bound to field size
                do {
                    fieldx = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + KINGx, 0), fieldsize - 1);
                    fieldy = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + KINGy, 0), fieldsize - 1);
                } while (gameState[fieldy][fieldx] != 0);
                // Place Gnome
                gameState[fieldy][fieldx] = GNOME;
            }
            // Create Gnome with the possibility createGnome, decreasing with each Gnome
            if (rand.nextDouble() < createGnome/i) {
                // Generate xGnome fields gaussian distributed around xKing, bound to field size
                do {
                    fieldx = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + xKINGx, 0), fieldsize - 1);
                    fieldy = (int) Math.min(Math.max(rand.nextGaussian() * (int) fieldsize / 3 + xKINGy, 0), fieldsize - 1);
                } while (gameState[fieldy][fieldx] != 0);
                // Place xGnome
                gameState[fieldy][fieldx] = xGNOME;
            }
        }

        return gameState;
    }
    
    public static String toCSVString(int[][][] array) { 
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int[][] x : array) {
                for (int[] i : x) {
                    for (int j : i) {
                        sb.append(j).append(",");
                    }
                }
            }
            result = result.concat(sb.deleteCharAt(sb.length() - 1).toString());
        } 
        return result;
    }
    
    public static void writeData(String data,String strFilePath)
        {
            try
            {
                File file = new File(strFilePath);
                if(!file.exists()){
                    file = new File(strFilePath);
                }
                PrintWriter csvWriter = new PrintWriter(new FileWriter(file,true));


                csvWriter.print(data);
                csvWriter.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    
    /**
     *
     * @param strFilePath File path of CSV to read
     * @param states Number of states
     * @param field Field size
     * @return Array containing randomly generated board states
     */
    public static int[][][] csvToIntArray(String strFilePath, int states, int field) {
        // Variable definitions
        String line, stringline = "";
        int[] line_array = new int[states*field*field];
        int[][][] output = new int[states][field][field];

        // Read file into single string line
        try (BufferedReader br = new BufferedReader(new FileReader(strFilePath))) {
            while ((line = br.readLine()) != null) {
                stringline = stringline.concat(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
              
        // Create one dimensional int array from string
        line_array = Stream.of(stringline.split(",")).mapToInt(Integer::parseInt).toArray();

        // Decode array according to field size and number of states
        for (int i=0; i< (states*field*field); i++)
            output[i/(field*field)][(i/field) - (field*(i/(field*field)))][i%field] = line_array[i];
        
        return output;
    }
    
    // Main method to (test) run
    public static void main(String[] args){
        int[][][] board_array = new int[STATES][fieldsize][fieldsize];
        int[][][] control_array = new int[STATES][fieldsize][fieldsize];
        
        // Generate random board positions
        for (int i=0; i< STATES ; i++)
            board_array[i] = simpleGen();
        
        // Write data to file
        writeData(toCSVString(board_array), "src/mainGame/50STATES_7FIELD.csv");
        
        // Read data (for testing purposes
        control_array = csvToIntArray("src/mainGame/50STATES_7FIELD.csv", STATES, fieldsize);
        
        // Test if input/output arrays are same
        System.out.printf(String.valueOf(Arrays.deepEquals(control_array, board_array))); 
        
        // Print gamestate array 
        for (int i=0; i< STATES ; i++) {
            for (int j=0; j< fieldsize ; j++) {
                for (int k=0; k < fieldsize ; k++) 
                    System.out.format("%2d ",control_array[i][j][k]); 
                System.out.println(); 
            }
            System.out.println(); 
        } 
    }
}
