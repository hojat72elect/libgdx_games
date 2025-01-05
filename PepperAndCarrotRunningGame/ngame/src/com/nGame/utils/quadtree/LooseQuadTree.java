package com.nGame.utils.quadtree;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


/**
 * Implementation of first loose QuadTree in reference to T. Ulrich
 * QuadTree, optimized for moving objects.
 * <p/>
 * If you move an object you have to delete and reinsert the object.
 *
 * @author Oliver Eichner o.eichner{at}gmail.com
 */
public class LooseQuadTree {

    // parent node
    private static final int PN = -1;
    // top left, quadrant 1
    private static final int TL = 0;
    // top right, quadrant 2
    private static final int TR = 1;
    // bottom left, quadrant 3
    private static final int BL = 2;
    // bottom right, quadrant 4
    private static final int BR = 3;
    private static final String TAG = LooseQuadTree.class.getSimpleName();
    private final HashSet<LQTObject> _objects = new HashSet<LQTObject>();
    private final HashSet<LQTObject> _objects2 = new HashSet<LQTObject>();
    private final HashSet<LQTObject> _neighborObjects = new HashSet<LQTObject>();
    private final HashSet<LooseQuad> _neighbors = new HashSet<LooseQuad>();
    public HashSet<Rectangle> debugRectangles = new HashSet<Rectangle>();
    private LooseQuad rootNode;
    public boolean debugDrawBounds = false;
    float zValue = 0;
    private float maxDepth = 5;
    private float k = 2;
    private Vector2 tmpVec2 = new Vector2();

    public LooseQuadTree(float width, float height, int maxDepth) {
        Gdx.app.log(TAG, "w/h:" + width);
        rootNode = new LooseQuad(0, new Rectangle(0, 0,
                width, height), null, PN, k);
        this.maxDepth = maxDepth;

        split(rootNode);

        // find neighbors for every node nodes
        // findNeighbors(rootNode);
    }

    /**
     * Calculate the tree depth for first given {@link Rectangle}
     *
     * @param size the {@link Rectangle} to check
     * @return the tree depth for the {@link Rectangle}
     */
    private int getLevel(Rectangle size) {

        float maxSize = ((size.width > size.height) ? size.width : size.height);

        int lvl = (int) (Math.log(rootNode.locationBound.width / maxSize) / Math
                .log(2));

        return (int) (lvl > maxDepth ? maxDepth : lvl);
    }

    /**
     * Insert an {@link LQTObject}.
     * <p/>
     * The {@link LQTObject} needs first proper Rectangle
     *
     * @param widget the {@link LQTObject} to store
     */
    public boolean insert(LQTObject widget) {
        return insertOrRemove(widget, true);
    }

    /**
     * Delete first {@link LQTObject} from the tree
     *
     * @param object the {@link com.nGame.utils.quadtree.LQTObject} to delete
     */
    public boolean delete(LQTObject object) {
        return insertOrRemove(object, false);
    }

    /**
     * Insert or remove first {@link LQTObject}
     *
     * @param object the {@link LQTObject} to insert/remove
     * @param add    true for insert and false for remove
     * @return true on successful insert or remove, otherwise false
     */
    private boolean insertOrRemove(LQTObject object, boolean add) {
        Rectangle oldBounds = object.getBounds();
        // get node level for size
        int lvl = getLevel(object.getBounds());

        LooseQuad currentNode = rootNode;

        // find quad node
        while (currentNode.depth < lvl) {
            int index = findQuadIndex(oldBounds, currentNode);

            LooseQuad nodeFound = currentNode.nodes[index];

            //checkbounds to allow something outside of the root bounds
            //if (nodeFound.bound.contains(oldBounds)) {
            currentNode = nodeFound;
            //} else {
            //  break;
            //}

        }

        if (add) {
            // insert
            if (debugDrawBounds && !currentNode.hasObject()) {
                debugRectangles.add(currentNode.locationBound);
            }
            return currentNode.addObject(object);
        } else {
            // remove
            boolean ret = currentNode.delObject(object);
            if (debugDrawBounds && !currentNode.hasObject()) {
                debugRectangles.remove(currentNode.locationBound);
            }
            return ret;
        }
    }

    /**
     * Find the child node index for an {@link Rectangle} in first parent node.
     *
     * @param rect        the {@link Rectangle} from the {@link LQTObject}
     * @param currentNode the parent {@link LooseQuad}
     * @return the index of the childNode.
     */
    private int findQuadIndex(Rectangle rect, LooseQuad currentNode) {
        int retVal;
        float bWidth = currentNode.bound.width;
        float bHeight = currentNode.bound.height;
        float cX = currentNode.bound.x + bWidth / 2;
        float cY = currentNode.bound.y + bHeight / 2;

        rect.getCenter(tmpVec2);
        if (tmpVec2.x > cX) {
            // right
            if (tmpVec2.y > cY) {
                retVal = TR;
            } else {
                retVal = BR;
            }

        } else {
            // left
            if (tmpVec2.y > cY) {
                retVal = TL;
            } else {
                retVal = BL;

            }
        }
        return retVal;

    }


    /**
     * Split first {@link LooseQuad} into 4 child nodes
     *
     * @param quadNode the {@link LooseQuad} to split
     */
    private void split(LooseQuad quadNode) {

        if (quadNode != null) {
            // split node

            for (int i = 0; i < 4; i++) {
                quadNode.nodes[i] = new LooseQuad(quadNode.depth + 1,
                        getSplitSquare(quadNode, i), quadNode, i, k);
                if (quadNode.nodes[i].depth < this.maxDepth) {
                    split(quadNode.nodes[i]);
                }

            }

        }

    }


    /**
     * Generates child node {@link Rectangle}s.
     *
     * @param quad the parent {@link LooseQuad}
     * @param i    the child node index
     * @return calculated {@link Rectangle} for the new child node
     */
    private Rectangle getSplitSquare(LooseQuad quad, int i) {

        float width = quad.locationBound.width / 2;
        float height = quad.locationBound.height / 2;

        switch (i) {
            case BL:
                return new Rectangle(quad.locationBound.x, quad.locationBound.y,
                        width, height);
            case BR:
                return new Rectangle(quad.locationBound.x + width,
                        quad.locationBound.y, width, height);
            case TL:
                return new Rectangle(quad.locationBound.x, quad.locationBound.y
                        + height, width, height);
            case TR:
                return new Rectangle(quad.locationBound.x + width,
                        quad.locationBound.y + height, width, height);
            default:
                Gdx.app.log(TAG, "could not determine split quad [" + i + "] for quad ["
                        + quad.toString() + "] ");

                return null;
        }

    }

    /**
     * Collects all possible {@link LQTObject} - {@link LQTObject} collisions.
     *
     * @param collisions the List to store the collision pairs
     */
    public void findObjectObjectCollision(
            ArrayList<Pair<LQTObject, LQTObject>> collisions) {
        findObjectObjectCollision(rootNode, collisions);
    }

    /**
     * Collects all possible {@link LQTObject} - {@link LQTObject} collisions in first given {@link LooseQuad}.
     *
     * @param node       the {@link LooseQuad} to search in
     * @param collisions the List to store the collision pairs
     */
    private void findObjectObjectCollision(LooseQuad node,
                                           ArrayList<Pair<LQTObject, LQTObject>> collisions) {
        if (node != null) {

            if (node.hasObject()) {
                potentialObjectObjectCollision(node, collisions);

            } else {
                for (int i = 0; i < 4; i++) {
                    findObjectObjectCollision(node.nodes[i], collisions);
                }
            }

        }
    }


    /**
     * Acquire collisions for first {@link LooseQuad} which contains an {@link LQTObject}.
     *
     * @param quad       the {@link LooseQuad} containing at least one {@link LQTObject}
     * @param collisions the List to store the collision pairs
     */
    void potentialObjectObjectCollision(LooseQuad quad,
                                        ArrayList<Pair<LQTObject, LQTObject>> collisions) {
        _objects.clear();
        _neighbors.clear();
        _neighborObjects.clear();

        // get node/child objects
        if (quad.getObjectCount() > 1 || !quad.isLeaf()) {

            getObjects(quad, _objects);

        }
        // get neighbors
        getNeighbors(quad, _neighbors);

        for (Iterator<LooseQuad> i = _neighbors.iterator(); i.hasNext(); ) {
            LooseQuad neighborQuad = i.next();
            if (neighborQuad != null) {
                getObjects(neighborQuad, _neighborObjects);
            }
        }

        //int objsLen = _objects.size();
        //int nobjsLen = _neighborObjects.size();

        int cnt = 0, cnt2;
        for (LQTObject objA : _objects) {
            cnt2 = 0;
            for (LQTObject objB : _objects) {
                if (cnt2 < cnt + 1) {
                    cnt2++;
                    continue;
                }
                collisions.add(new Pair<LQTObject, LQTObject>(objA, objB));
            }

            cnt++;
            //neighbor objects

            for (LQTObject objB : _neighborObjects) {
                collisions.add(new Pair<LQTObject, LQTObject>(objA, objB));
            }
        }
    }

    /**
     * Calculate all neighbor {@link LooseQuad}s to first given {@link LooseQuad}
     *
     * @param quad           the {@link LooseQuad} to get the neighbors for
     * @param neighborsFound the {@link java.util.HashSet} to store the neighbors
     */
    private void getNeighbors(LooseQuad quad, HashSet<LooseQuad> neighborsFound) {
        int lvl = quad.depth;
        if (lvl > 0) {
            neighborsFound.add(getLeftNeighbor(quad));
            neighborsFound.add(getRightNeighbor(quad));
            neighborsFound.add(getTopNeighbor(quad));
            neighborsFound.add(getBottomNeighbor(quad));
            neighborsFound.add(getBottomLeftNeighbor(quad));
            neighborsFound.add(getBottomRightNeighbor(quad));
            neighborsFound.add(getTopLeftNeighbor(quad));
            neighborsFound.add(getTopRightNeighbor(quad));
        }

    }

    /**
     * Get all {@link LQTObject} from first {@link LooseQuad} and his child nodes
     *
     * @param quad         the {@link LooseQuad} to get the {@link LQTObject}s from
     * @param objectsFound the {@link java.util.HashSet} to store the {@link LQTObject} objects
     */
    private void getObjects(LooseQuad quad, HashSet objectsFound) {
        if (quad != null) {
            if (quad.hasObject()) {

                int len = quad.getObjectCount();

                for (int i = len - 1; i >= 0; i--) {
                    objectsFound.add(quad.getObject(i));
                }

            }
            for (int i = 0; i < 4; i++) {
                getObjects(quad.nodes[i], objectsFound);
            }
        }
    }

    /**
     * Get left neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getLeftNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == TR || node.childID == BR;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID - 1];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor
            LooseQuad parentNeighbor = getLeftNeighbor(node.parentNode);

            // Bail if we didn't find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID + 1];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get right neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getRightNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == TL || node.childID == BL;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID + 1];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor
            LooseQuad parentNeighbor = getRightNeighbor(node.parentNode);

            // Bail if we didnt find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID - 1];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get top neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getTopNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == BR || node.childID == BL;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID - 2];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor
            LooseQuad parentNeighbor = getTopNeighbor(node.parentNode);

            // Bail if we didnt find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID + 2];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get bottom neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getBottomNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == TR || node.childID == TL;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID + 2];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor
            LooseQuad parentNeighbor = getBottomNeighbor(node.parentNode);

            // Bail if we didnt find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID - 2];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get bottom left neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getBottomLeftNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == TR;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID + 1];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor

            LooseQuad parentNeighbor = null;
            int idOffset = 0;
            switch (node.childID) {
                case TL:
                    parentNeighbor = getLeftNeighbor(node.parentNode);
                    // 0->3
                    idOffset = 3;
                    break;
                case BL:
                    parentNeighbor = getBottomLeftNeighbor(node.parentNode);
                    // 2->1
                    idOffset = -1;
                    break;
                case BR:
                    parentNeighbor = getBottomNeighbor(node.parentNode);
                    // 3->0
                    idOffset = -3;
                    break;

            }

            // Bail if we didnt find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID + idOffset];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get bottom right neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getBottomRightNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == TL;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID + 3];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor

            LooseQuad parentNeighbor = null;
            int idOffset = 0;
            switch (node.childID) {
                case TR:
                    parentNeighbor = getRightNeighbor(node.parentNode);
                    // 1->2
                    idOffset = 1;
                    break;
                case BR:
                    parentNeighbor = getBottomRightNeighbor(node.parentNode);
                    // 3->0
                    idOffset = -3;
                    break;
                case BL:
                    parentNeighbor = getBottomNeighbor(node.parentNode);
                    // 2->1
                    idOffset = -1;
                    break;

            }

            // Bail if we didnt find first neighbor
            if (parentNeighbor == null)
                return null;

            // not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID + idOffset];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get top left neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getTopLeftNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == BR;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID - 3];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor

            LooseQuad parentNeighbor = null;
            int idOffset = 0;
            switch (node.childID) {
                case TR:
                    parentNeighbor = getTopNeighbor(node.parentNode);
                    // 1->2
                    idOffset = 1;
                    break;
                case TL:
                    parentNeighbor = getTopLeftNeighbor(node.parentNode);
                    // 0->3
                    idOffset = 3;
                    break;
                case BL:
                    parentNeighbor = getLeftNeighbor(node.parentNode);
                    // 2->1
                    idOffset = -1;
                    break;

            }

            // Bail if we didnt find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID + idOffset];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }

    /**
     * Get top right neighbor node for first given node
     *
     * @param node the {@link LooseQuad} to get the neighbor for
     * @return the neighbor {@link LooseQuad}
     */
    LooseQuad getTopRightNeighbor(LooseQuad node) {
        // check if root node
        if (node.parentNode == null)
            return null;

        // check if neighbor has same parent
        boolean sameParent = node.childID == BL;

        if (sameParent) {
            // get neighbor from parent
            return node.parentNode.nodes[node.childID - 1];
        } else {
            // We don't have first neighbor on this level, ask parent for its
            // neighbor

            LooseQuad parentNeighbor = null;
            int idOffset = 0;
            switch (node.childID) {
                case TR:
                    parentNeighbor = getTopRightNeighbor(node.parentNode);
                    // 1->2
                    idOffset = 1;
                    break;
                case BR:
                    parentNeighbor = getRightNeighbor(node.parentNode);
                    // 3->0
                    idOffset = -3;
                    break;
                case TL:
                    parentNeighbor = getRightNeighbor(node.parentNode);
                    // 0->3
                    idOffset = 3;
                    break;

            }

            // Bail if we didn't find first neighbor
            if (parentNeighbor == null)
                return null;

            // has Previous , has Children , not Leaf?
            if (!parentNeighbor.isLeaf()) {
                // yes, return found neighbors child
                return parentNeighbor.nodes[node.childID + idOffset];
            } else {
                // no return found neighbor
                return parentNeighbor;
            }
        }
    }


    /**
     * Remove all objects from tree
     */
    public void clear() {
        clear(rootNode);
    }

    private void clear(LooseQuad quad) {
        if (quad != null) {
            if (quad.hasObject()) {
                quad.clear();
            }
            for (int i = 0; i < 4; i++) {
                clear(quad.nodes[i]);
            }
        }
    }


    public ArrayList retrieve(ArrayList returnObjects, Rectangle pRect) {
        int lvl = getLevel(pRect);
        return retrieve(returnObjects, pRect, lvl, rootNode);
    }


    private ArrayList retrieve(ArrayList returnObjects, Rectangle pRect, int level, LooseQuad currentNode) {
        int index = findQuadIndex(pRect, currentNode);
        if (currentNode.depth == level) {
            _neighbors.clear();
            getNeighbors(currentNode, _neighbors);
            for (LooseQuad node : _neighbors) {
                if (node != null)
                    retrieve(returnObjects, pRect, level, node);
            }
        }
        if (index != -1 && !currentNode.isLeaf()) {

            retrieve(returnObjects, pRect, level, currentNode.nodes[index]);
        }

        if (currentNode.getObjectCount() > 0)
            returnObjects.addAll(currentNode.objects);

        return returnObjects;
    }


    /**
     * A loose Quad for the use in {@link LooseQuadTree}
     *
     * @author Oliver Eichner o.eichner{at}gmail.com
     */
    private class LooseQuad {

        public int depth = 0;
        public Rectangle bound;
        public Rectangle locationBound;
        public LooseQuad[] nodes;
        public ArrayList<LQTObject> objects = new ArrayList();
        public LooseQuad parentNode = null;
        public int childID = -1;
        public boolean hasNewObject = false;


        private Vector2 tmpVec2 = new Vector2();

        /**
         * Construct first loose Quad
         *
         * @param nodeDepth the depth of the node
         * @param boundary  the inner bounds, not the loose bound, it will be calculated from this
         * @param parent    the parent {@link LooseQuad} node
         * @param childID   the childID
         * @param k         the looseness value
         */
        public LooseQuad(int nodeDepth, Rectangle boundary, LooseQuad parent, int childID, float k) {
            this.childID = childID;
            this.depth = nodeDepth;
            parentNode = parent;
            locationBound = boundary;
            bound = new Rectangle(boundary);
            bound.getCenter(tmpVec2);
            bound.width = k * boundary.width;
            bound.height = k * boundary.width;
            bound.setCenter(tmpVec2);

            //bound.resize(k * boundary.width, k * boundary.height);

            nodes = new LooseQuad[]{null, null, null, null};
        }


        /**
         * Check if the node contains first {@link LQTObject}
         *
         * @return true if it contains first {@link LQTObject}
         */
        public boolean hasObject() {
            return !objects.isEmpty();
        }


        /**
         * add an {@link LQTObject} to this node
         *
         * @param obj the {@link LQTObject} to add
         * @return return true on success
         */
        public boolean addObject(LQTObject obj) {
            hasNewObject = true;
            return objects.add(obj);
        }

        /**
         * Delete first {@link LQTObject} from node
         *
         * @param obj the {@link LQTObject} to delete
         * @return return true on success
         */
        public boolean delObject(LQTObject obj) {

            return objects.remove(obj);

        }

        /**
         * Returns the amount of {@link LQTObject}s in this node
         *
         * @return the amount of {@link LQTObject}s
         */
        public int getObjectCount() {
            return objects.size();
        }

        /**
         * Get an {@link LQTObject} by index
         *
         * @param i the index for the {@link LQTObject}
         * @return the {@link LQTObject}. If it doesn't exist null will be returned
         */
        public LQTObject getObject(int i) {
            return objects.get(i);
        }

        /**
         * Delete all contained objects
         */
        public void clear() {
            objects.clear();
            hasNewObject = false;
        }

        public boolean isLeaf() {
            return nodes[0] == null && nodes[1] == null && nodes[2] == null && nodes[3] == null;
        }

    }
}
