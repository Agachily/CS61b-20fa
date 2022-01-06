package bearmaps.lab9;

import java.util.*;

/**
 * The Trie data structure implemented using the Hash Table
 * Created by Zetong
 *  */
public class MyTrieSet implements TrieSet61B {
    Node root;

    private class Node {
        private char content;
        private boolean isEnd;
        private Map<Character, Node> subNodes;
        private List<Node> allSubNodes;

        // Create a sentinel node as the root
        Node() {
            content = 'n';
            isEnd = false;
            subNodes = new HashMap<>();
        }

        Node(char content, boolean isEnd) {
            this.content = content;
            this.isEnd = isEnd;
            subNodes = new HashMap<>();
        }

        // Judges whether the given character is contained in the node's subNodes
        boolean isContained(char content) {
            return subNodes.containsKey(content);
        }

        void addSubChar(char content, boolean isEnd) {
            Node node = new Node(content, isEnd);
            subNodes.put(content, node);
        }

        Node getSubNode(char content) {
            return subNodes.get(content);
        }

        void changeIsEnd(boolean isEnd) {
            this.isEnd = isEnd;
        }

        boolean isEndNode() {
            return isEnd;
        }

        boolean hasSubNode() {
            return !subNodes.isEmpty();
        }

        char getContent() {
            return content;
        }

        List<Node> getAllSubNodes() {
            allSubNodes = new ArrayList<>();
            Set<Character> keySet = subNodes.keySet();
            for (Character key : keySet) {
                Node node = subNodes.get(key);
                allSubNodes.add(node);
            }
            return allSubNodes;
        }
    }

    public MyTrieSet() {
        root = new Node();
    }

    @Override
    public void clear() {
        root.subNodes.clear();
    }

    private boolean contains(String key, Node currentSuperNode) {
        char firstChar;
        int keyLength = key.length();
        if (keyLength == 0) {
            if (currentSuperNode.isEndNode()) {
                return true;
            } else {
                return false;
            }
        }
        firstChar = key.charAt(0);
        if (!currentSuperNode.isContained(firstChar)) {
            return false;
        } else {
            Node subNode = currentSuperNode.getSubNode(firstChar);
            String newKey = key.substring(1, keyLength);
            return contains(newKey, subNode);
        }
    }

    @Override
    public boolean contains(String key) {
        return contains(key, root);
    }

    // Add a character to a specific node
    private void add(String key, Node currentSuperNode) {
        char firstChar;
        boolean isEnd = false;
        int keyLength = key.length();
        if (keyLength == 0) {
            return;
        }
        if (keyLength == 1) {
            isEnd = true;
        }

        firstChar = key.charAt(0);

        if (!currentSuperNode.isContained(firstChar)) {
            currentSuperNode.addSubChar(firstChar, isEnd);
        }
        Node subNode = currentSuperNode.getSubNode(firstChar);
        if (!currentSuperNode.isContained(firstChar) || isEnd == true) {
            subNode.changeIsEnd(true);
        }
        String newKey = key.substring(1, keyLength);
        add(newKey, subNode);
    }

    @Override
    public void add(String key) {
        add(key, root);
    }

    private List<String> allStrings(Node currentSuperNode) {
        List<String> list = new ArrayList<>();
        String content = "";
        if (currentSuperNode != root) {
            content = Character.toString(currentSuperNode.getContent());
        }
        if (!currentSuperNode.hasSubNode()) {
            list.add(content);
            return list;
        }
        if (currentSuperNode.isEndNode()) {
            list.add(content);
        }
        List<Node> allSubNodes = currentSuperNode.getAllSubNodes();
        for (Node node : allSubNodes) {
            List<String> AllStrings = allStrings(node);
            for (String s : AllStrings) {
                list.add(content + s);
            }
        }
        return list;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        if (prefix.length() == 0) {
            return null;
        }
        List<String> matchStrings = new ArrayList<>();
        char[] pre = prefix.toCharArray();
        Node currentRoot = this.root;
        Node finalRoot = root; // The start point of searching the sub-trie
        for (int i = 0; i < pre.length; i++) {
            if (!currentRoot.isContained(pre[i])) {
                return null;
            }
            currentRoot = currentRoot.getSubNode((pre[i]));
            if (i == pre.length - 1) {
                finalRoot = currentRoot;
            }
        }
        List<String> subStrings = allStrings(finalRoot);
        for (String s : subStrings) {
            matchStrings.add(prefix.substring(0, prefix.length() - 1) + s);
        }
        return matchStrings;
    }

    @Override
    public String longestPrefixOf(String key) {
        String prefix = "";
        if (key.length() == 0) {
            return null;
        }
        Node currentRoot = this.root;
        char[] pre = key.toCharArray();
        for (int i = 0; i < pre.length; i++) {
            if (currentRoot.isContained(pre[i])) {
                prefix += Character.toString(pre[i]);
            } else {
                return prefix;
            }
            currentRoot = currentRoot.getSubNode((pre[i]));
        }
        return prefix;
    }
}
