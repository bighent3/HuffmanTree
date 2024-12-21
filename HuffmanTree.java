//Henton Hailey-Marshall
//CSE 143 AA with Ido Avnon
//Homework A8: Huffman
import java.io.*;
import java.util.*;

//This is an encoder class. It reads in a text file. Analyzes the quantity of characters to
//organize them by the frequency of their occurence  and then efficiently codes them according
//to their frequency. Allowing parties with the code to encrypt and decrypt messages.
public class HuffmanTree {
    private Node root;

    //Class Constructor
    //This is the method that will construct your initial Huffman tree using the given array of 
    //frequencies where count[i] is the number of occurrences of the character with integer value 
    //i.
    //Parameters:
    //  -   Int[] Count: frequency of character occurrence
    //Exceptions: NA
    //Returns: Constructor NA
    public HuffmanTree(int[] count) {
        PriorityQueue<Node> pq = new PriorityQueue<>();

        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                pq.add(new Node(i, count[i], null, null));
            }
        }

        while (pq.size() > 1) {
            Node left = pq.remove();
            Node right = pq.remove();
            pq.add(new Node(-1, left.count + right.count, left, right));
        }

        if (pq.isEmpty()) {
            root = null;
        } else {
            root = pq.remove();
        }
    }

    //Constructor
    //Parameters:
    //  Scanner Input: text file of code
    //Exceptions: NA
    //Returns: Constructor NA
    public HuffmanTree(Scanner input) {
        root = new Node(-1, 0, null, null);
        while (input.hasNextLine()) {
            int character = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            addCode(character, code);
        }
    }


    //Post: Decode file in coded format
    //Parameters:
    //  BitInputStream input: bits encoded
    //  PrintStream output: object to write characters
    //  int eof: marks where file ends
    //Exceptions: NA
    //Returns:
    //  false: if decoded
    //  true: not decoded
    public boolean decode(BitInputStream input, PrintStream output, int eof) {
        Node current = root;
        while (true) {
            int bit = input.readBit();
            if (bit == -1) {
                return false;
            }

            if (bit == 0) {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                if (current.data == eof) {
                    return true;
                }
                output.write(current.data);
                current = root;
            }
        }
    }


    //Writes code based off of encodings
    //Parameters:
    //  PrintStream output: object to write code
    //Exceptions: NA
    //Returns: Public/Private call pair writing code
    public boolean write(PrintStream output) {
        return write(root, "", output);
    }


    //Post: Writes code of tree
    //Parameters:
    // NOde node: node being encoded/decoded
    // string text: text coded so far
    // printstream: object to write code
    //Exceptions: na
    //Returns: true if encoded/decoded
    private boolean write(Node node, String text, PrintStream output) {
        if (node.left == null && node.right == null) {
            output.println(node.data);
            output.println(text);
            return true;
        }
        boolean leftText = true;
        if (node.left != null) {
            leftText = write(node.left, text + "0", output);
        }
        boolean rightText = true;
        if (node.right != null) {
            rightText = write(node.right, text + "1", output);
        }
        return leftText && rightText;
    }


    //Adds letrer and code to tree
    //Parameters:
    // int letter: character added
    //string code: code corresponding to letter
    //Exceptions: na
    //Returns: true if added
    private boolean addCode(int letter, String code) {
        Node current = root;
        for (int i = 0; i < code.length(); i++) {
            char character = code.charAt(i);
            if (character == '0') {
                if (current.left == null) {
                    current.left = new Node(-1, 0, null, null);
                }
                current = current.left;
            } else if (character == '1') {
                if (current.right == null) {
                    current.right = new Node(-1, 0, null, null);
                }
                current = current.right;
            }
        }
        current.data = letter;
        return true;
    }

    //Post: Comparable interface for ordering by frequency
    //Parameters: NA
    //Exceptions: NA
    //Returns: NA
    private static class Node implements Comparable<Node>{
        public int data;
        public int count;
        public Node left;
        public Node right;

        //Node class 
        //Parameters:
        //int data: character vale
        //int count: letter frequency
        //node left: left child node
        //node right: right child node
        //Exceptions:
        //Returns:
        public Node(int data, int count, Node left, Node right) {
            this.data = data;
            this.count = count;
            this.left = left;
            this.right = right;
        }

        //Post: compares node to other node
        //Parameters:
        //Node other: the node being compared to
        //Exceptions: NA
        //Returns: 
        //negative value if this node less than other node
        //positive value is this node greater than other node
        //0 if equal
        public int compareTo(Node other) {
            return this.count - other.count;
        }
    }
}