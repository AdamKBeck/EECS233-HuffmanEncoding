
public class HuffmanNode{
    private Character inChar; // The character denoted by the node
    private int frequency; // The frequency of occurrences of all chars in the subtree rooted at this node
    private HuffmanNode left; // Left child of a node in the tree
    private HuffmanNode right; // Right child of a node in the tree
    private String encoding; // The encoding of a node
    
    /**
     * Constructs a new HuffmanNode
     * 
     * @param inChar The character denoted by the node
     * @param frequency The frequency of all chars in the node's subtree
     * @param left The left child of the node
     * @param right The right child of the node
     */
    public HuffmanNode(Character inChar, int frequency, HuffmanNode left, HuffmanNode right){
        this.inChar = inChar;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
        encoding = "";
    }
    
    /**
     * Sets the right child of a node
     * @param right The right child to be set
     */
    public void setRight(HuffmanNode right){
        this.right = right;
    }
    
    /**
     * Gets the right child of a node
     * @return Right child
     */
    public HuffmanNode getRight(){
        return right;
    }
    
    /**
     * Sets the left child of a node
     * @param left The left child to be set
     */
    public void setLeft(HuffmanNode left){
        this.left = left;
    }
    
    /**
     * Gets the left child of a node
     * @return Left child
     */
    public HuffmanNode getLeft(){
        return left;
    }
    
    /**
     * Gets the character denoted by the node
     * @return The character denoted by the node
     */
    public Character getInChar(){
        return inChar;
    }
    
    /**
     * Sets the frequency of the node
     * @param frequency The frequency to set the node to
     */
    public void setFrequency(int frequency){
        this.frequency = frequency;
    }
    
    /**
     * Gets the frequency of the node
     * @return The frequency of the node
     */
    public int getFrequency(){
        return frequency;
    }
    
    /**
     * Sets the encoding of a node
     * @param encoding The encoding of a node
     */
    public void setEncoding(String encoding){
        this.encoding = encoding;
    }
    
    /**
     * Gets the encoding of a node
     * @return The encoding of the specified node
     */
    public String getEncoding(){
        return encoding;
    }
}
