import java.io.PrintStream;
import java.util.Scanner;

public class HuffmanTree {
    private Node root;

    /**
     * Constructs a HuffmanTree based on the codes from a Scanner input.
     * Each line in the input contains a character code followed by its Huffman encoding.
     *
     * @param input the Scanner containing character codes and encodings.
     */
    public HuffmanTree(Scanner input) {
        root = new Node(0, null, null);
        while (input.hasNextLine()) {
            int character = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            addCode(root, character, code);
        }
    }

    /**
     * Decodes the input bits and writes the corresponding characters to the output.
     *
     * @param input the BitInputStream containing encoded bits.
     * @param output the PrintStream to write the decoded characters.
     * @param eof the end-of-file marker character.
     */
    public void decode(BitInputStream input, PrintStream output, int eof) {
        Node current = root;
        int bit;

        while ((bit = input.readBit()) != -1) {
            if (bit == 0) {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) { // Leaf node
                if (current.data == eof) {
                    return; // Stop decoding at EOF
                }
                output.write(current.data);
                current = root; // Reset to start decoding the next character
            }
        }
    }

    /**
     * Writes the character codes and their encodings to the output in preorder traversal.
     *
     * @param output the PrintStream to write the character codes and encodings.
     */
    public void write(PrintStream output) {
        write(root, "", output);
    }

    /**
     * Constructs a HuffmanTree based on character frequencies.
     * The character frequencies are provided as an array where the index represents the character.
     *
     * @param frequencies an array containing the frequencies of characters.
     */
    public HuffmanTree(int[] frequencies) {
        root = buildTree(frequencies);
    }

    // Helper method to build the Huffman tree based on character frequencies
    private Node buildTree(int[] frequencies) {
        Node[] nodes = new Node[frequencies.length];
        int size = 0;

        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                nodes[size++] = new Node(i, frequencies[i], null, null);
            }
        }

        while (size > 1) {
            Node min1 = removeMin(nodes, size);
            size--;
            Node min2 = removeMin(nodes, size);
            size--;

            Node combined = new Node(0, min1.frequency + min2.frequency, min1, min2);
            nodes[size++] = combined;
        }

        return nodes[0];
    }

    // Helper method to remove and return the minimum node from an array of nodes
    private Node removeMin(Node[] nodes, int size) {
        int minIndex = 0;
        for (int i = 1; i < size; i++) {
            if (nodes[i].frequency < nodes[minIndex].frequency) {
                minIndex = i;
            }
        }
        Node min = nodes[minIndex];
        nodes[minIndex] = nodes[size - 1];
        return min;
    }

    // Recursive helper to write character codes and their encodings
    private void write(Node node, String path, PrintStream output) {
        if (node.left == null && node.right == null) { // Leaf node
            output.println(node.data);
            output.println(path);
        } else {
            if (node.left != null) {
                write(node.left, path + "0", output);
            }
            if (node.right != null) {
                write(node.right, path + "1", output);
            }
        }
    }

    // Helper method to add a character code to the Huffman tree
    private void addCode(Node node, int character, String code) {
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (c == '0') {
                if (node.left == null) {
                    node.left = new Node(0, null, null);
                }
                node = node.left;
            } else { // '1'
                if (node.right == null) {
                    node.right = new Node(0, null, null);
                }
                node = node.right;
            }
        }
        node.data = character;
    }

    // Node class for the Huffman tree
    private static class Node {
        public int data;
        public int frequency;
        public Node left;
        public Node right;

        public Node(int data, int frequency, Node left, Node right) {
            this.data = data;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        public Node(int data, Node left, Node right) {
            this(data, 0, left, right);
        }
    }
}
