/*this file contains the definitions of a Red Black tree with location values*/
/*------------------------------------------------------------------------------------------------------------*/

// Class Definitions
class RedBlackNode {

    /*color for this node*/
    public static final int BLACK = 0;
    public static final int RED = 1;

    // the key of each node
    public int key;

    RedBlackNode parent;

    RedBlackNode left;
    RedBlackNode right;

    // the number of elements to the left of each node
    public int numLeft = 0;
    // the number of elements to the right of each node
    public int numRight = 0;

    // the color of a node
    public int color;

    RedBlackNode() {
        color = BLACK;
        numLeft = 0;
        numRight = 0;
        parent = null;
        left = null;
        right = null;
    }

    // Constructor for key.
    RedBlackNode(int key) {
        this();
        this.key = key;
    }
}

@SuppressWarnings("ALL")
class RedBlackTree {

    // Root initialized to nil.
    private RedBlackNode nil = new RedBlackNode();
    private RedBlackNode root = nil;

    public RedBlackTree() {
        root = null;
    }

    // @param: x, The node that is rotated
    // Performs a left rotation.
    private void leftRotate(RedBlackNode x) {

        // Update to the numLeft and numRight values.
        leftRotateFixup(x);

        //left rotation
        RedBlackNode y;
        y = x.right;
        x.right = y.left;

        //  pointer changes
        if (!isNil(y.left)) {
            y.left.parent = x;
        }
        y.parent = x.parent;

        // null parent
        if (isNil(x.parent)) {
            root = y;
        } else if (x.parent.left == x) {
            x.parent.left = y;
        }

        // x is right child
        else {
            x.parent.right = y;
        }

        //end
        y.left = x;
        x.parent = y;
    }

    // @param: x, the node that got a left rotation
    // Fixes the numLeft & numRight values affected by leftRotate.
    private void leftRotateFixup(RedBlackNode x) {

        // Only x, x.right and x.right.right always are not nil.
        if (isNil(x.left) && isNil(x.right.left)) {
            x.numLeft = 0;
            x.numRight = 0;
            x.right.numLeft = 1;
        }

        //x.right.left also exists
        else if (isNil(x.left) && !isNil(x.right.left)) {
            x.numLeft = 0;
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 2 + x.right.left.numLeft +
                    x.right.left.numRight;
        }

        //x.left too
        else if (!isNil(x.left) && isNil(x.right.left)) {
            x.numRight = 0;
            x.right.numLeft = 2 + x.left.numLeft + x.left.numRight;

        }
        //x.left and x.right.left both exist
        else {
            x.numRight = 1 + x.right.left.numLeft +
                    x.right.left.numRight;
            x.right.numLeft = 3 + x.left.numLeft + x.left.numRight +
                    x.right.left.numLeft + x.right.left.numRight;
        }

    }

    // @param: x, the node that is rotated right
    // Fixes numLeft and numRight values affected by the rotation.
    private void rightRotate(RedBlackNode y) {

        // Fix numRight and numLeft values
        rightRotateFixup(y);

        // Rotation
        RedBlackNode x = y.left;
        y.left = x.right;

        // Check if not null
        if (!isNil(x.right)) {
            x.right.parent = y;
        }
        x.parent = y.parent;

        // y's parent is nil
        if (isNil(y.parent)) {
            root = x;
        }

        // y is a right child
        else if (y.parent.right == y) {
            y.parent.right = x;
        }

        // y is a left child
        else {
            y.parent.left = x;
        }
        x.right = y;

        y.parent = x;

    }

    // @param: y, the node that got rotated right
    // Fixes numLeft and numRight values affected by the rotation
    private void rightRotateFixup(RedBlackNode y) {

        //Only y, y.left and y.left.left exist
        if (isNil(y.right) && isNil(y.left.right)) {
            y.numRight = 0;
            y.numLeft = 0;
            y.left.numRight = 1;
        }

        //y's left.right also exists
        else if (isNil(y.right) && !isNil(y.left.right)) {
            y.numRight = 0;
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 2 + y.left.right.numRight +
                    y.left.right.numLeft;
        }

        //y's right also exists
        else if (!isNil(y.right) && isNil(y.left.right)) {
            y.numLeft = 0;
            y.left.numRight = 2 + y.right.numRight + y.right.numLeft;

        }

        //y.right & y.left.right exist
        else {
            y.numLeft = 1 + y.left.right.numRight +
                    y.left.right.numLeft;
            y.left.numRight = 3 + y.right.numRight +
                    y.right.numLeft +
                    y.left.right.numRight + y.left.right.numLeft;
        }

    }

    // @param: key, the integer to be inserted into the Tree rooted at root
    // Inserts the into a node in the RedBlackTree
    private void insert(int key) {
        insert(new RedBlackNode(key));
    }

    // @param: z, the node to be inserted into the Tree rooted at root
    // inserts z into the tree.
    private void insert(RedBlackNode z) {

        // Create a reference to root & initialize a node to nil
        RedBlackNode y = nil;
        RedBlackNode x = root;

        while (!isNil(x)) {
            y = x;

            // go left is smaller
            if (z.key < x.key) {

                // update x's left
                x.numLeft++;
                x = x.left;
            }

            // go right
            else {

                // update x's right
                x.numRight++;
                x = x.right;
            }
        }
        // parent
        z.parent = y;

        // place z in the proper place
        if (isNil(y)) {
            root = z;
        } else if (z.key < y.key) {
            y.left = z;
        } else {
            y.right = z;
        }

        // set z's children left and right to nil and change z's color to red
        z.left = nil;
        z.right = nil;
        z.color = RedBlackNode.RED;

        // fix
        insertFixup(z);

    }

    // @param: z, the node which was inserted
    // Fixes up the violation of the RedBlackTree traits
    private void insertFixup(RedBlackNode z) {

        RedBlackNode y;
        // While there is a violation of the RedBlackTree traits
        while (z.parent.color == RedBlackNode.RED) {

            // If z's parent is the the left child of its parent.
            if (z.parent == z.parent.parent.left) {

                // Initialize y to be z 's cousin
                y = z.parent.parent.right;

                //if y is red...recolor
                if (y.color == RedBlackNode.RED) {
                    z.parent.color = RedBlackNode.BLACK;
                    y.color = RedBlackNode.BLACK;
                    z.parent.parent.color = RedBlackNode.RED;
                    z = z.parent.parent;
                }
                //if y is black & z is a right child
                else if (z == z.parent.right) {

                    // rotate to the left around z's parent
                    z = z.parent;
                    leftRotate(z);
                }

                //y is black & z is a left child
                else {
                    // fix
                    z.parent.color = RedBlackNode.BLACK;
                    z.parent.parent.color = RedBlackNode.RED;
                    rightRotate(z.parent.parent);
                }
            }

            // If z's parent is the right child
            else {

                y = z.parent.parent.left;

                // recolor y
                if (y.color == RedBlackNode.RED) {
                    z.parent.color = RedBlackNode.BLACK;
                    y.color = RedBlackNode.BLACK;
                    z.parent.parent.color = RedBlackNode.RED;
                    z = z.parent.parent;
                }

                // y is black and z is left
                else if (z == z.parent.left) {
                    // rightRotate around z's parent
                    z = z.parent;
                    rightRotate(z);
                }
                // same but z is right
                else {
                    // recolor and rotate around z's grandfather node
                    z.parent.color = RedBlackNode.BLACK;
                    z.parent.parent.color = RedBlackNode.RED;
                    leftRotate(z.parent.parent);
                }
            }
        }
        // make sure root is black
        root.color = RedBlackNode.BLACK;

    }

    // @param: node, a Red Black Node
    // @return: node, the node with the smallest key in the tree
    private RedBlackNode treeMinimum(RedBlackNode node) {

        // while there is a smaller key, keep going left
        while (!isNil(node.left)) {
            node = node.left;
        }
        return node;
    }

    // @param: node, a Red Black Node
    // @return: node, the node with the largest key in the tree
    private RedBlackNode treeMaximum(RedBlackNode node) {

        // while there is a larger key, keep going right
        while (node != null && !isNil(node.right)) {
            node = node.right;
        }
        return node;
    }

    // @param: x, a node that has a successor we need to find
    // @return:y, the key next largest node after x
    private RedBlackNode treeSuccessor(RedBlackNode x) {

        // if x.left is not nil, call treeMinimum(x.right) and return value
        if (!isNil(x.left)) {
            return treeMinimum(x.right);
        }

        RedBlackNode y = x.parent;

        // x is parent
        while (!isNil(y) && x == y.right) {
            // Keep moving up in the tree
            x = y;
            y = y.parent;
        }
        // Return successor
        return y;
    }

    // @param: z, the a node that will be removed
    // removes z from the tree
    private void remove(RedBlackNode v) {

        RedBlackNode z = search(v.key);

        RedBlackNode x;
        RedBlackNode y;

        // if either one of z's children is nil, remove it
        if (isNil(z.left) || isNil(z.right)) {
            y = z;
        }

        // remove z's successor
        else {
            y = treeSuccessor(z);
        }

        // Let x be the left or right child of y (y can only have one child)
        if (!isNil(y.left)) {
            x = y.left;
        } else {
            x = y.right;
        }

        // x's parent is y's parent
        x.parent = y.parent;

        // if y is null x is root
        if (isNil(y.parent)) {
            root = x;
        }

        // else if y is a left child, set x to be y's left sibling
        else if (!isNil(y.parent.left) && y.parent.left == y) {
            y.parent.left = x;
        }

        // else if y is a right child, set x to be y's right sibling
        else if (!isNil(y.parent.right) && y.parent.right == y) {
            y.parent.right = x;
        }

        // if y != z, will move y's key data into z.
        if (y != z) {
            z.key = y.key;
        }

        // Update the numLeft and numRight numbers which might need updating due to the deletion of z.key.
        fixNodeData(x, y);

        // If y's color is black, we will fix the tree
        if (y.color == RedBlackNode.BLACK) {
            removeFixup(x);
        }
    }

    // @param: y, the node which was actually deleted from the tree
    // @param: key, the value of the key that used to be in y
    private void fixNodeData(RedBlackNode x, RedBlackNode y) {

        // Initialize two variables for travel
        RedBlackNode current = nil;
        RedBlackNode track = nil;

        // if x is nil, then we will start updating at y.parent
        // Set track to y, y.parent's child
        if (isNil(x)) {
            current = y.parent;
            track = y;
        }

        // if x is not nil, then we start updating at x.parent
        // Set track to x, x.parent's child
        else {
            current = x.parent;
            track = x;
        }

        while (!isNil(current)) {
            // if the node we deleted has a different key than the current node
            if (y.key != current.key) {

                // if the node we deleted is greater than current.key then decrease current.numRight
                if (y.key > current.key) {
                    current.numRight--;
                }

                // if the node we deleted is less than current.key then decrease current.numLeft
                if (y.key < current.key) {
                    current.numLeft--;
                }
            }

            // if the node we deleted has the same key as the
            // current node
            else {
                // check and update
                if (isNil(current.left)) {
                    current.numLeft--;
                } else if (isNil(current.right)) {
                    current.numRight--;
                }

                // check the two children and set track
                else if (track == current.right) {
                    current.numRight--;
                } else if (track == current.left) {
                    current.numLeft--;
                }
            }

            // update track and current
            track = current;
            current = current.parent;

        }

    }

    // @param: x, the child of the deleted node from remove(RedBlackNode v)
    // fixes the Red Black traits after removing a node
    private void removeFixup(RedBlackNode x) {

        RedBlackNode w;

        while (x != root && x.color == RedBlackNode.BLACK) {

            // if x is it's parent's left child
            if (x == x.parent.left) {

                // set w = x's sibling
                w = x.parent.right;

                //w's color is red.
                if (w.color == RedBlackNode.RED) {
                    w.color = RedBlackNode.BLACK;
                    x.parent.color = RedBlackNode.RED;
                    leftRotate(x.parent);
                    w = x.parent.right;
                }

                //both of w's children are black
                if (w.left.color == RedBlackNode.BLACK &&
                        w.right.color == RedBlackNode.BLACK) {
                    w.color = RedBlackNode.RED;
                    x = x.parent;
                } else {
                    //w's right child is black
                    if (w.right.color == RedBlackNode.BLACK) {
                        w.left.color = RedBlackNode.BLACK;
                        w.color = RedBlackNode.RED;
                        rightRotate(w);
                        w = x.parent.right;
                    }
                    //w = black, w.right = red
                    w.color = x.parent.color;
                    x.parent.color = RedBlackNode.BLACK;
                    w.right.color = RedBlackNode.BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {

                // if x is it's parent's right child set w to x's sibling
                w = x.parent.left;

                //w's color is red
                if (w.color == RedBlackNode.RED) {
                    w.color = RedBlackNode.BLACK;
                    x.parent.color = RedBlackNode.RED;
                    rightRotate(x.parent);
                    w = x.parent.left;
                }

                //both of w's children are black
                if (w.right.color == RedBlackNode.BLACK &&
                        w.left.color == RedBlackNode.BLACK) {
                    w.color = RedBlackNode.RED;
                    x = x.parent;
                } else {
                    //w's left child is black
                    if (w.left.color == RedBlackNode.BLACK) {
                        w.right.color = RedBlackNode.BLACK;
                        w.color = RedBlackNode.RED;
                        leftRotate(w);
                        w = x.parent.left;
                    }

                    // w = black, and w.left = red
                    w.color = x.parent.color;
                    x.parent.color = RedBlackNode.BLACK;
                    w.left.color = RedBlackNode.BLACK;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }

        // set x to black to ensure there is no violation of
        // RedBlack tree traits
        x.color = RedBlackNode.BLACK;
    }

    // @param: key, the key whose node we want to search for
    // @return: returns a node with the key, key, if not found, returns null
    private RedBlackNode search(int key) {

        // Initialize a pointer to travel
        RedBlackNode current = root;

        // travel until end
        while (!isNil(current)) {

            // found
            if (current.key == key)

            // return that node and exit search(int)
            {
                return current;
            }

            // go left or right
            else if (current.key < key) {
                current = current.right;
            } else {
                current = current.left;
            }
        }

        // we have not found a node whose key is "key"
        return null;

    }

    // @param: node, the node we must check to see whether it's nil
    // @return: true if node is nil or not;
    private boolean isNil(RedBlackNode node) {
        return node == null || node == nil;
    }

    // @return: tree size
    // number of nodes
    public int size() {

        if (root == null) {
            return 0;
        }
        // Return the number of nodes to the root's left + the number of
        // nodes on the root's right + the root itself.
        return root.numLeft + root.numRight + 1;
    }


    //@param: k, the amount of smallest numbers from the array we want to print
    //@param: element, the integer we want to insert to the tree
    // the function will insert nodes to the tree but will keep the tree filled only with k smallest nodes at all times
    private void kInsert(int k, int element) {
        RedBlackNode max = treeMaximum(root); //largest key in the tree
        if (size() < k) {
            insert(element); //insert is there are less than k elements
        } else {
            if (element < max.key) { //if there are k elements, remove the largest and insert the new one
                remove(max);
                insert(element);
            }
        }
    }

    //@param: arr, the array we want to print it's k-smallest numbers
    //@param: k, the required user input
    //@param: n1, the first  check stop in the tree
    //@param:n2, the second check stop in the tree
    //@param:n3, the third and final check stop in the tree
    //prints the K-smallest numbers in the array
    public void printKMin(int arr[], int k, int n1, int n2, int n3) {
        for (int i = 0; i < arr.length; i++) {
            kInsert(k, arr[i]); //insert the array's k-smallest numbers
            if ((i == n1)) { //print the saved tree at every check stop
                System.out.print("first check point:\n ");
                printTree();
            }
            if ((i == n2)) {
                System.out.print("Second check point:\n ");
                printTree();
            }
            if ((i == n3)) {
                System.out.print("Third check point:\n ");
                printTree();
            }
        } //print the final K-smallest numbers
        System.out.println("K-Smallest keys: ");
        printTree();
    }

    //this program the tree's keys in an "in order" fashion
    private void printTree() {

        System.out.print("\nIn order : ");
        this.inorder(root);
        System.out.println("\n");
    }

    //@param receives a node from which to travel
    //prints the K-smallest numbers in the array
    private void inorder(RedBlackNode r) {

        if (r != nil) {

            inorder(r.left);

            System.out.print(r.key + " ");
            inorder(r.right);
        }
    }
}