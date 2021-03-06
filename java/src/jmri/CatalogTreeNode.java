package jmri;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import jmri.util.swing.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * Node of a CatalogTree.
 * <p>
 * Name for the node Path is info needed for leafs.
 *
 * @author Pete Cressman Copyright 2009
 */
public class CatalogTreeNode extends DefaultMutableTreeNode {

    // Sorted by height for ease of display in CatalogPanel
    private ArrayList<CatalogTreeLeaf> _leafs = new ArrayList<>();

    public CatalogTreeNode(String name) {
        super(name);
    }

    /**
     * Append leaf to the end of the leafs list.
     *
     * @param leaf the leaf to add
     */
    public void addLeaf(CatalogTreeLeaf leaf) {
        _leafs.add(leaf);
    }

    /**
     * Insert leaf according to height. Dan Boudreau 10/15/2018 eliminated the
     * check for valid icon and the sorting of the icons by height. Improves
     * load time at initialization by an order of magnitude.
     *
     * @param name name of the new leaf
     * @param path path to the new leaf
     */
    public void addLeaf(String name, String path) {
        int h = 0;
        _leafs.add(new CatalogTreeLeaf(name, path, h)); //  name is non-localized
    }

    /**
     * Leafs can be used for many-to-many relations.
     *
     * @param name the leafs to remove
     */
    public void deleteLeaves(String name) {
        for (Iterator<CatalogTreeLeaf> iterator = _leafs.iterator(); iterator.hasNext();) {
            CatalogTreeLeaf leaf = iterator.next();
            if (name.equals(leaf.getName())) {
                iterator.remove(); // Safely remove the current element from the iterator and the list
            }
        }
    }

    public void deleteLeaf(String name, String path) {
        for (int i = 0; i < _leafs.size(); i++) {
            CatalogTreeLeaf leaf = _leafs.get(i);
            if (name.equals(leaf.getName()) && path.equals(leaf.getPath())) {
                _leafs.remove(i);
                return;
            }
        }
    }

    public CatalogTreeLeaf getLeaf(String name, String path) {
        for (CatalogTreeLeaf leaf : _leafs) {
            if (name.equals(leaf.getName()) && path.equals(leaf.getPath())) {
                return leaf;
            }
        }
        return null;
    }

    /**
     * Leafs can be used for many-to-many relations.
     *
     * @param name name of the leafs to get
     * @return a list of matching leafs; an empty list if there are no matching
     *         leafs
     */
    public ArrayList<CatalogTreeLeaf> getLeaves(String name) {
        ArrayList<CatalogTreeLeaf> leafs = new ArrayList<>();
        for (CatalogTreeLeaf leaf : _leafs) {
            if (name.equals(leaf.getName())) {
                leafs.add(leaf);
            }
        }
        return leafs;
    }

    @Override
    public Enumeration<TreeNode> children() { // for JDK 9 typing
        return super.children();
    }

    public ArrayList<CatalogTreeLeaf> getLeaves() {
        return _leafs;
    }

    public int getNumLeaves() {
        return _leafs.size();
    }

    public void setLeaves(ArrayList<CatalogTreeLeaf> leafs) {
        _leafs = leafs;
    }

    //    private final static Logger log = LoggerFactory.getLogger(CatalogTreeNode.class);
}
