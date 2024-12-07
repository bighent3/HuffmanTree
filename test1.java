
    import java.io.PrintStream;
    import java.util.PriorityQueue;
    import java.util.Scanner;
    
    public class HuffmanTree {
        private HuffmanNode root;
    
        // Constructs a Huffman tree from character frequencies
        public HuffmanTree(int[] frequencies) {
            PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
    
            for (int i = 0; i < frequencies.length; i++) {
                if (frequencies[i] > 0) {
                    pq.add(new HuffmanNode(i, frequencies[i]));
                }
            }
    
            // Add pseudo-eof character
            pq.add(new HuffmanNode(frequencies.length, 1));
    
            while (pq.size() > 1) {
                HuffmanNode left = pq.poll();
                HuffmanNode right = pq.poll();
                HuffmanNode parent = new HuffmanNode(left.frequency + right.frequency, left, right);
                pq.add(parent);
            }
    
            root = pq.poll();
        }
    
        // Constructs a Huffman tree from a code file
        public HuffmanTree(Scanner input) {
            root = new HuffmanNode(-1, -1);
            while (input.hasNextLine()) {
                int character = Integer.parseInt(input.nextLine());
                String code = input.nextLine();
                addCode(root, character, code, 0);
            }
        }
    
        private void addCode(HuffmanNode node, int character, String code, int index) {
            if (index == code.length()) {
                node.character = character;
            } else {
                if (code.charAt(index) == '0') {
                    if (node.left == null) {
                        node.left = new HuffmanNode(-1, -1);
                    }
                    addCode(node.left, character, code, index + 1);
                } else {
                    if (node.right == null) {
                        node.right = new HuffmanNode(-1, -1);
                    }
                    addCode(node.right, character, code, index + 1);
                }
            }
        }
    
        // Writes the codes to the output stream
        public void write(PrintStream output) {
            writeHelper(root, "", output);
        }
    
        private void writeHelper(HuffmanNode node, String code, PrintStream output) {
            if (node.left == null && node.right == null) { // Leaf node
                output.println(node.character);
                output.println(code);
            } else {
                writeHelper(node.left, code + "0", output);
                writeHelper(node.right, code + "1", output);
            }
        }
    
        // Decodes the input stream and writes to the output
        public void decode(BitInputStream input, PrintStream output, int eof) {
            HuffmanNode current = root;
            int bit;
    
            while ((bit = input.readBit()) != -1) {
                current = (bit == 0) ? current.left : current.right;
    
                if (current.left == null && current.right == null) { // Leaf node
                    if (current.character == eof) {
                        break; // Stop decoding at pseudo-eof
                    }
                    output.write(current.character);
                    current = root; // Reset for next character
                }
            }
        }
    
        // Nested class representing nodes in the Huffman tree
        private static class HuffmanNode implements Comparable<HuffmanNode> {
            public int character;
            public int frequency;
            public HuffmanNode left;
            public HuffmanNode right;
    
            // Constructor for leaf nodes
            public HuffmanNode(int character, int frequency) {
                this.character = character;
                this.frequency = frequency;
                this.left = null;
                this.right = null;
            }
    
            // Constructor for non-leaf nodes
            public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
                this.character = -1; // Non-leaf node
                this.frequency = frequency;
                this.left = left;
                this.right = right;
            }
    
            @Override
            public int compareTo(HuffmanNode other) {
                return Integer.compare(this.frequency, other.frequency);
            }
        }
    }
