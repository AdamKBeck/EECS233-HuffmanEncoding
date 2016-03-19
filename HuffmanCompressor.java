import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Reads and compresses an input text file and saves it to an
 * output file
 * 
 * @author Adam Beck
 */
public class HuffmanCompressor {
    
    
    /**
     * Scans the input file and generates a list of HuffmanNodes of the characters in the file
     * 
     * @param inputFileName
     * @throws IOException 
     * @return An ArrayList of HuffmanNode objects of each character in the text file.
     */
    public ArrayList<HuffmanNode> scanFile(String inputFileName) throws IOException{
       
        File fileName = new File(inputFileName);
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        
        /*
         * I used an ArrayList as opposed to  Linked List.
         * I did this because when removing elements from linked lists, you have to keep
         * track of the head pointer and change it accordingly. Also, to search for an element
         * in a Linked List, you have to traverse the whole list which is O(n). 
         * An ArrayList is O(1) for get() method calls. 
         * 
         * Most importantly, every time size() is called, it would be O(n) on a Linked List 
         * implementation if the size isn't stored as an instance variable. An arrayList 
         * is O(1) for calling size(). An arrayList can also have a predetermined size which
         * I used as 256, and you can't set a predetermined size for a LinkedList
         */
        ArrayList<HuffmanNode> charList = new ArrayList<HuffmanNode>(256);
        
        /* Add all possible characters to the list */
        for (int i = 0; i < 256; i++){
            charList.add(new HuffmanNode((char)i, 0, null, null));
        }
        
        int currentChar; // The current char is represented as an integer value
       
        /* Loop until the end of the file */
        while ( (currentChar =  br.read()) != -1){          
           
            /* Only add character codes up to 255 inclusive for this project */
            if (currentChar <= 255){
                /* Update frequency of the character scanned */
                charList.get(currentChar).setFrequency(charList.get(currentChar).getFrequency() + 1);
            }
        }
        
        br.close();     
        return charList;
    }
    
    /**
     * Runs a Huffman algorithm to create a Huffman tree
     * 
     * @param list The list of NuffmanNode objects to create into a tree
     * @return The HuffmanNode root of the Huffman Tree
     * @throws Exception List to generate tree is empty
     */
    public HuffmanNode generateTree(ArrayList<HuffmanNode> list) throws Exception{
        /* First, sort the list. I used a bubble sort. It's viable because
         * ASCII values only go up to 256, and a bubble sort is O(n^2)
         * and 256^2 is relatively very small for a sorting algorithm complexity.
         * Alternatively collections.sort() can be used */
        boolean switched = false;
        
        while (true){
            /* Sort from least to greatest (left to right in the list) */
            for (int i = 0; i < list.size()-1; i++){
                if (list.get(i).getFrequency() > list.get(i+1).getFrequency()){
                    HuffmanNode temp = list.get(i+1);
                    list.set(i+1, list.get(i));
                    list.set(i, temp);
                    switched = true;
                }
            }
            
            /* Break out of the loop once the list is sorted */
            if (switched){
                switched = false;
            }
            else{
                break;
            }
        }
        
        /* Now generate the Huffman Tree */
       if (list.size() == 1){
           return list.get(0);
       }
       
       else if (list.size() == 0){
           throw new Exception("List is empty");
       }
  
       else{
           while (list.size() > 1){
               
               if (list.get(0).getFrequency() == 0){
                   list.remove(0);
               }
               else{
                   /* Remove and merge the two least common nodes, add the merged node back to the list */
                   HuffmanNode leftNode = list.remove(0);
                   HuffmanNode rightNode = list.remove(0);
                   
                   /* Merge the two lowest nodes, adds them as children to the new merged node */
                   HuffmanNode mergedNode = new HuffmanNode(null, leftNode.getFrequency() + rightNode.getFrequency(), leftNode, rightNode);
                 
                   /* Put the merged node back in the list */
                   list.add(list.size(), mergedNode);     
               }
           }    
           
       }
     
       return list.remove(0); // The only remaining node must be the root.  
    }
    
    /**
     * Traverses the Huffman Tree and encodes the table.
     * @param root The root of the Huffman Tree
     */
    public void traverseTreeEncodeTable(HuffmanNode root){
        
        if (root.getLeft() == null && root.getRight() == null){
            return; // This is for the case with a parent node with no children.
        }
        
        /* If the parent has a left child, recursively encode the left child */
        if (root.getLeft() != null){
            root.getLeft().setEncoding(root.getEncoding() + "0");  
            traverseTreeEncodeTable(root.getLeft());
        }
        
        /* If the parent has a right child, recursively encode the right child */
        if (root.getRight() != null){
            root.getRight().setEncoding(root.getEncoding() + "1");
            traverseTreeEncodeTable(root.getRight());
        }     
    }
    
    /**
     * A method to scan the input text file (again!), produce the encoded output file, and compute the savings
     * 
     * @param inputFileName The file name for the input file
     * @param outputFileName The file name for the output file
     * @return A string containing tabulated information about the character and it's frequency,
     * each character's encoding, and information about the saved space from the algorithm.
     * @throws Exception File IO Exceptions
     */
    public static String huffmanCoder(String inputFileName, String outputFileName) throws Exception{
        HuffmanCompressor h = new HuffmanCompressor();
        ArrayList<HuffmanNode> list = h.scanFile(inputFileName);
        
        /* The original list will be deleted because of the generateTree() method. 
         * This local variable preserves the old list for future use in this method */
        ArrayList<HuffmanNode> preservedList = new ArrayList<HuffmanNode>(list);
        
        HuffmanNode root = h.generateTree(list);
        h.traverseTreeEncodeTable(root);
        
        int currentChar = 0; // char is represented as an integer value
        StringBuilder info = new StringBuilder(); // The info about the encoding table and saved space.
        StringBuilder inputEncoding = new StringBuilder();
        StringBuilder outputEncoding = new StringBuilder();
        
        File fileName = new File(inputFileName);
        PrintWriter writer = new PrintWriter(outputFileName);
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        
        /* Loops until the end of file. Creates a stringbuilder for the input file
         * characters and the output file characters */
        while ((currentChar = br.read()) != -1){
                int listIndex = -1;
                
                for (int i = 0; i < preservedList.size(); i++){
                    if (preservedList.get(i).getInChar() == currentChar){
                        listIndex = i;
                        break;
                    }
                }
                
                writer.print(preservedList.get(listIndex).getEncoding());
                inputEncoding.append(currentChar);
                outputEncoding.append(preservedList.get(listIndex).getEncoding());         
        }  
        
        /* Tabulate each character, it's frequency, and encoding value */
        for (int i = 0; i < preservedList.size(); i++){
            info.append("Character: ");
            info.append(preservedList.get(i).getInChar() + " Frequency: ");
            info.append(preservedList.get(i).getFrequency() + " Encoding: ");
            info.append(preservedList.get(i).getEncoding());
            info.append("\n");
        }
        
        /* Calculate input file bits and output file bits*/
        int inputFileBits = 0;
        int outputFileBits = 0;
        
        /* Each char is 16 bits in java but UTF-8 is 8 bits which is the large file size we are downloading to test */
        inputFileBits = inputEncoding.length() * 8;
        
        outputFileBits = outputEncoding.length(); // The length of the output file is the direct bit size.
        
        /* Calculate storage saved from by the Huffman encoding */ 
        double savedStoragePercent = (double)outputFileBits / (double)inputFileBits;
        // savedStoragePercent is the percent of the outputFileBits compared to the original.
        // e.g. output is 5 bits, original is 10 bits, the file is now 50% the size of original.
        
        info.append("\n");
        info.append("The following calculates saved space by file size");
        info.append("The original input file size was " + inputFileBits + " bits.\n");
        info.append("The new output file size was " + outputFileBits + " bits.\n");
        info.append("The new output file is " + savedStoragePercent * 100 + "% the size of the original file\n");
        info.append("Therefore this is " + (double)inputFileBits / (double)outputFileBits + " times smaller.\n");
       
        /* Calculate average length of the new characters after the encoding */
        int sumLength = 0;
        int totalCharacters = 0;
        for (int i = 0; i < preservedList.size(); i++){
            sumLength += (preservedList.get(i).getFrequency() * preservedList.get(i).getEncoding().toString().length());
            totalCharacters += preservedList.get(i).getFrequency();
        }
        
        double averageLengthEncoded = (double)sumLength / (double)totalCharacters;
              
        info.append("\nThis is not the true compression ratio. The total length of the binary digits in the output file is: " + sumLength);
        info.append("\nThe following will calculate the compression using average character bit size");
        info.append("\nThe total characters represented in the encoded file is: " + totalCharacters);
        info.append("\nThe average length of the new character is now: " + averageLengthEncoded);
        info.append("\nSince UTF-8 is 8 bits, the new compression rate is 1-"+averageLengthEncoded+"/8 which is: " +(1-(averageLengthEncoded/8))*100 + "% compression.");
        info.append("\nSavings is therefore: " + (1-(averageLengthEncoded/8))*100 + "%");

        /* Write the table and savings to a separate file */
        FileWriter writer2 = new FileWriter("table.txt");
        writer2.write(info.toString());
        
        writer.close();
        writer2.close();
        br.close();
        return info.toString();   
     }
    
    /* The file names for the input and output files are stored in
     * args[0] and args[1] respectively */
    public static void main(String args[]) throws Exception{  
        if (args.length != 2){
            System.out.println("Error: There must be only two arguments for this program.");
            System.out.println("You inputted " + args.length + " arguments.");
        }
        else{
            System.out.println(huffmanCoder(args[0], args[1]));
        }
    }
    
}
