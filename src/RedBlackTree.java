import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.WhileNode;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Created by Refael_Paz on 25/07/2015.
 */
public class RedBlackTree
{
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;

    private class BestNode
    {
        private Node best_side;
        private Node best_height;
        private int best_volume;
    }

    public boolean CheckBox(Integer side,Integer height)
    {
        boolean check_box;
        Node tmp = ceilingNode(side);
        if(tmp != null)
        {
            if (tmp.innerTree.ceiling(height) != null)
                check_box = true;
            else
                check_box = false;
        }
        else
        {
            check_box = false;
        }

        return check_box;
    }

    public Integer[] GetBox(Integer side,Integer height)
    {
        Integer[] bestBox = new Integer[2];
        BestNode best_node = new BestNode();
        best_node.best_side = ceilingNode(side);
        Node current_side_node = TreeSuccessor(best_node.best_side);
        if (best_node.best_side != null)
        {
            best_node.best_height = best_node.best_side.innerTree.ceilingNode(height);
            if (best_node.best_height != null)
                best_node.best_volume = best_node.best_side.key * 2 * best_node.best_height.key;
            else
                best_node.best_volume = 0;
        }

        while(current_side_node != null)
        {
            Node current_height_node = current_side_node.innerTree.ceilingNode(height);
            if(current_height_node != null)
            {
                int current_volume;
                current_volume = current_side_node.key * 2 * current_height_node.key;
                if (current_volume < best_node.best_volume || best_node.best_volume == 0)
                {
                    best_node.best_side = current_side_node;
                    best_node.best_height = current_height_node;
                    best_node.best_volume = current_volume;
                }
            }
            current_side_node = TreeSuccessor(current_side_node);
        }
        if (best_node.best_side != null && best_node.best_height != null)
        {
            best_node.best_side.best_height = best_node.best_height;
            bestBox[0] = best_node.best_side.key;
            bestBox[1] = best_node.best_height.key;
        }

        return bestBox;
    }

    public Node TreeSuccessor(Node node)
    {
        if (node != null)
            return ceilingNode(node.key+1);
        else
            return null;
    }

    public void InsertBox(Integer side,Integer height) {
        Node side_node = getNode(side);
        if (side_node != null) {
            Node height_node = side_node.innerTree.getNode(height);
            if (height_node != null)
                height_node.amount++;
            else
                side_node.innerTree.put(height, 1);
        }
        else
        {
            this.putBox(side,height);
        }
    }

    private void putBox(Integer side, Integer height)
    {
        this.put(side,1);
        Node side_node = getNode(side);
        side_node.innerTree = new RedBlackTree();
        side_node.innerTree.put(height, 1);
    }

    public boolean RemoveBox(Integer side, Integer height)
    {
        boolean box_exist = true;
        Node side_node = getNode(side);
        if (side_node == null)
        {
            System.out.print("No such side value exists");
            box_exist = false;
        }

        else
        {
            Node height_node = side_node.innerTree.getNode(height);
            if (height_node == null)
            {
                System.out.print("No such height value exists");
                box_exist = false;
            }
            else
            {
                if (height_node.amount != 1)
                    height_node.amount--;
                else
                {
                    side_node.innerTree.delete(height);
                    if (side_node.innerTree.size() == 0)
                        delete(side);
                }
            }
        }
        return box_exist;
    }


    public void printStructure()
    {
        if (root == null)
            System.out.println("null");
        else
        {
            System.out.println("*********************************");
            root.display(0);
            System.out.println("*********************************");
        }
        System.out.println();
    }



    public class Node
    {
        private Integer key;
        private Integer amount;
        private Node left, right;
        private boolean color;
        private int size;
        private RedBlackTree innerTree;
        private Node root;
        private Node best_height;

        public Node(Integer key, Integer amount, boolean color, int size)
        {
            this.key = key;
            this.amount = amount;
            this.color = color;
            this.size = size;
        }

        public Integer getKey()
        {
            return key;
        }

        public void display(int n)
        {
            String indent = "-";

            //Print the indents for this level
            for (int i = 1; i <= n; i++)
            {
                System.out.print(indent);
            }

            //Print the node content
            System.out.println("ROOT: " + key + ", color: " + color);

            //Indent
            for (int i = 1; i <= n; i++)
            {
                System.out.print(indent);
            }

            //Print the left child of the node
            System.out.println("LEFT");
            if (left == null)
            {
                for (int i = 1; i <= n+1; i++)
                {
                    System.out.print(indent);
                }
                System.out.println("null");
            }
            else
            {
                left.display(n+1);
            }

            //indent
            for (int i = 1; i <= n; i++)
            {
                System.out.print(indent);
            }

            //Print the right child of the node
            System.out.println("RIGHT");
            if (right == null)
            {
                for (int i = 1; i <= n; i++)
                {
                    System.out.print(indent);
                }
                System.out.println("null");
            }
            else
                right.display(n + 1);
        }

        public RedBlackTree getInnerTree(){return innerTree;}

    }

    private boolean isRed(Node node)
    {
        if (node == null)
            return false;
        else
            return (node.color == RED);
    }

    private int size(Node node)
    {
        if (node == null)
            return 0;
        else
            return node.size;
    }

    public int size()
    {
        return size(root);
    }

    public boolean isEmpty()
    {
        return root == null;
    }

    public Integer get(Integer key)
    {
        return get(root, key);
    }

    private Integer get(Node node, Integer key)
    {
        while (node != null)
        {
            int cmp = key.compareTo(node.key);
            if (cmp < 0)
                node = node.left;
            else if (cmp > 0)
                node = node.right;
            else
                return node.amount;
        }
        return null;
    }

    private Node getNode(Node node,Integer key)
    {
        while (node != null)
        {
            int cmp = key.compareTo(node.key);
            if (cmp < 0)
                node = node.left;
            else if (cmp > 0)
                node = node.right;
            else
                return node;
        }
        return null;
    }

    public Node getNode(Integer key)
    {
        return getNode(root,key);
    }

    public boolean contains(Integer key)
    {
        return get(key) != null;
    }

    /********
     * Insert Functions
     **********/
    public void put(Integer key, Integer amount)
    {
        root = put(root, key, amount);
        root.color = BLACK;
    }

    private Node put(Node sub_root, Integer key, Integer amount)
    {
        if (sub_root == null)
            return new Node(key, amount, RED, 1);

        int cmp = key.compareTo(sub_root.key);
        if (cmp < 0)
            sub_root.left = put(sub_root.left, key, amount);
        else if (cmp > 0)
            sub_root.right = put(sub_root.right, key, amount);
        else
            sub_root.amount = amount;

        if (isRed(sub_root.right) && !isRed(sub_root.left))
            sub_root = rotateLeft(sub_root);
        if (isRed(sub_root.left) && isRed(sub_root.left.left))
            sub_root = rotateRight(sub_root);
        if (isRed(sub_root.left) && isRed(sub_root.right))
            flipColors(sub_root);
        sub_root.size = size(sub_root.left) + size(sub_root.right) + 1;

        return sub_root;
    }


    /********
     * Delete Functions
     **********/

    public void deleteMin()
    {
        if (isEmpty())
            throw new NoSuchElementException("Tree underflow");
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        root = deleteMin(root);
        if (!isEmpty())
            root.color = BLACK;
    }

    private Node deleteMin(Node sub_root)
    {
        if (sub_root.left == null)
            return null;
        if (!isRed(sub_root) && !isRed(sub_root.left.left))
            sub_root = moveRedLeft(sub_root);

        sub_root.left = deleteMin(sub_root.left);
        return balance(sub_root);
    }

    public void deleteMax()
    {
        if (isEmpty())
            throw new NoSuchElementException("RedBlack Tree Underflow");

        if (!isRed(root.left) && !isRed(root.right))
            root.color = BLACK;

        root = deleteMax(root);
        if (!isEmpty())
            root.color = BLACK;
    }

    private Node deleteMax(Node sub_root)
    {
        if (isRed(sub_root.left))
            sub_root = rotateRight(sub_root);

        if (sub_root.right == null)
            return null;

        if (!isRed(sub_root.right) && !isRed(sub_root.right.left))
            sub_root = moveRedRight(sub_root);

        sub_root.right = deleteMax(sub_root.right);

        return balance(sub_root);
    }

    public void delete(Integer key)
    {
        if (!contains(key))
        {
            System.err.println("Symbol table does not contain " + key);
            return;
        }
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty())
            root.color = BLACK;
    }

    private Node delete(Node sub_root, Integer key)
    {
        if (key.compareTo(sub_root.key) < 0)
        {
            if (!isRed(sub_root.left) && !isRed(sub_root.left.left))
                sub_root = moveRedLeft(sub_root);

            sub_root.left = delete(sub_root.left, key);
        } else
        {
            if (isRed(sub_root.left))
                sub_root = rotateLeft(sub_root);
            if (key.compareTo(sub_root.key) == 0 && (sub_root.right == null))
                return null;
            if (!isRed(sub_root.right) && !isRed(sub_root.right.left))
                sub_root = moveRedRight(sub_root);
            if (key.compareTo(sub_root.key) == 0)
            {
                Node x = min(sub_root.right);
                sub_root.key = x.key;
                sub_root.amount = x.amount;
                sub_root.right = deleteMin(sub_root.right);
            } else
            {
                sub_root.right = delete(sub_root.right, key);
            }
        }
        return balance(sub_root);
    }

    private Node moveRedRight(Node sub_root)
    {
        flipColors(sub_root);
        if (isRed(sub_root.left.left))
        {
            sub_root = rotateRight(sub_root);
            flipColors(sub_root);
        }
        return sub_root;
    }

    private Node balance(Node sub_root)
    {
        if (isRed(sub_root.right))
            sub_root = rotateLeft(sub_root);
        if (isRed(sub_root.left) && isRed(sub_root.left.left))
            sub_root = rotateRight(sub_root);
        if (isRed(sub_root.left) && isRed(sub_root.right))
            flipColors(sub_root);
        sub_root.size = size(sub_root.left) + size(sub_root.right) + 1;
        return sub_root;
    }

    private Node moveRedLeft(Node sub_root)
    {
        flipColors(sub_root);
        if (isRed(sub_root.right.left))
        {
            sub_root.right = rotateRight(sub_root.right);
            sub_root = rotateLeft(sub_root);
            flipColors(sub_root);
        }
        return sub_root;
    }


    private void flipColors(Node sub_root)
    {
        sub_root.color = !sub_root.color;
        sub_root.left.color = !sub_root.left.color;
        sub_root.right.color = !sub_root.right.color;
    }

    private Node rotateRight(Node sub_root)
    {
        Node x = sub_root.left;
        sub_root.left = x.right;
        x.right = sub_root;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = sub_root.size;
        sub_root.size = size(sub_root.left) + size(sub_root.right) + 1;
        return x;
    }

    private Node rotateLeft(Node sub_root)
    {
        Node x = sub_root.right;
        sub_root.right = x.left;
        x.left = sub_root;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = sub_root.size;
        sub_root.size = size(sub_root.left) + size(sub_root.right) + 1;
        return x;
    }

    public int height()
    {
        return height(root);
    }

    public int height(Node node)
    {
        if (node == null)
            return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public Integer min()
    {
        if (isEmpty()) return null;
        return min(root).key;
    }

    private Node min(Node node)
    {
        if (node.left == null)
            return node;
        else
            return min(node.left);
    }

    public Integer max()
    {
        if (isEmpty())
            return null;
        return max(root).key;
    }

    private Node max(Node node)
    {
        if (node.right == null)
            return node;
        else
            return max(node.right);
    }

    public Integer floor(Integer key)
    {
        Node node = floor(root, key);
        if (node == null)
            return null;
        else
            return node.key;
    }

    private Node floor(Node node, Integer key)
    {
        if (node == null)
            return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0)
            return node;
        if (cmp < 0)
            return floor(node.left, key);
        Node t = floor(node.right, key);
        if (t != null)
            return t;
        else
            return node;
    }

    private Node ceilingNode(Integer key)
    {
        Node node = ceiling(root, key);
        if (node == null) return null;
        else              return node;
    }


    public Integer ceiling(Integer key)
    {
        Node node = ceiling(root, key);
        if (node == null) return null;
        else            return node.key;
    }

    private Node ceiling(Node node, Integer key)
    {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node;
        if (cmp > 0) return ceiling(node.right, key);
        Node t = ceiling(node.left, key);
        if (t != null) return t;
        else           return node;
    }


    public Integer select(int k)
    {
        if (k < 0 || k >= size()) return null;
        Node node = select(root, k);
        return node.key;
    }

    private Node select(Node node, int k)
    {
        int t = size(node.left);
        if (t > k)      return select(node.left,k);
        else if (t < k) return select(node.right, k - t - 1);
        else            return node;
    }

    public int rank(Integer key)
    {
        return rank(key,root);
    }

    private int rank(Integer key, Node node)
    {
        if (node == null) return 0;
        int cmp = key.compareTo(node.key);
        if (cmp < 0)     return rank(key, node.left);
        else if(cmp > 0) return 1 + size(node.left) + rank(key,node.right);
        else             return size(node.left);
    }

    //Check Integrity of red-black tree
    private boolean check()
    {
        if (!isBST())            System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!is23())             System.out.println("Not a 2-3 tree");
        if (!isBalanced())       System.out.println("Not balanced");
        return isBST() && isSizeConsistent() && is23() && isBalanced();
    }

    private boolean isBalanced()
    {
        int black = 0;
        Node node = root;
        while (node != null)
        {
            if (!isRed(node)) black++;
            node = node.left;
        }
        return isBalanced(root, black);
    }

    private boolean isBalanced(Node node, int black)
    {
        if (node == null) return black == 0;
        if (!isRed(node)) black--;
        return isBalanced(node.left,black) && isBalanced(node.right,black);
    }

    private boolean is23()
    {
        return is23(root);
    }

    private boolean is23(Node node)
    {
        if (node == null) return true;
        if (isRed(node.right)) return false;
        if (node != root && isRed(node) && isRed(node.left)) return false;
        return is23(node.left) && is23(node.right);
    }


    private boolean isSizeConsistent()
    {
        return isSizeConsistent(root);
    }

    private boolean isSizeConsistent(Node node)
    {
        if (node == null) return true;
        if (node.size != size(node.left) + size(node.right)+ 1 ) return false;
        return isSizeConsistent(node.left) && isSizeConsistent(node.right);
    }

    private boolean isBST()
    {
        return isBST(root,null,null);
    }

    private boolean isBST(Node node, Integer min, Integer max)
    {
        if (node == null) return true;
        if (min != null && node.key.compareTo(min) <= 0) return false;
        if (max != null && node.key.compareTo(max) >= 0) return false;
        return isBST(node.left,min,node.key) && isBST(node.right,node.key,max);
    }


}