package com.crosslink.battleprism.client.world.nodes;

import com.crosslink.battleprism.core.containers.AxialDirection;
import com.crosslink.battleprism.core.containers.Color;
import com.crosslink.battleprism.core.containers.Ray;
import com.crosslink.battleprism.core.math.Octant;
import com.crosslink.battleprism.core.math.Vec3;
import com.crosslink.battleprism.core.math.Vec3I;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 12/27/13
 * Time: 8:32 PM
 * Represents either a Chunk or Voxel used in an Octree configuration.
 */
public class Node {


    //region Public Variables
    public NodePosition position;
    public Chunk parent;            // The chunk that owns this node
    public Octant octant;           // The octant of the parent which this node occupies

    public int level;               // The octree level that this node stands in
    public int diameter;            // The width of this node in all dimensions
    //endregion

    //region Private Variables
    private boolean colorValid = false;
    private Color color = Color.red;
    private int glow = 0;
    //endregion

    //region Constructors
    public Node(Chunk parent, Octant octant, int level) {
        this.parent = parent;
        this.octant = octant;
        this.level = level;
        this.diameter = calcDiameter();

        // Update the relative position of this node to the root level chunk.
        // Based on relative position of parent.
        if (parent == null) {
            position = new NodePosition();
        } else {
            position = new NodePosition(parent.position);
            if (octant.getXSign() > 0)
                position.relPosition.x += diameter;
            if (octant.getYSign() > 0)
                position.relPosition.y += diameter;
            if (octant.getZSign() > 0)
                position.relPosition.z += diameter;
        }
    }
    //endregion

    //region Private Methods

    //-------------------------------------------------------------------------------
    //  Returns the diameter of this node based on tree level.
    //-------------------------------------------------------------------------------
    private int calcDiameter() {
        return (2 << level) / 2; // power(2, level)
    }

    //endregion

    //region Public Methods

    public Color getColor() {
        return color;
    }


    //-------------------------------------------------------------------------------
    //  Determines the absolute location of a node adjacent to this node in an
    //  arbitrary direction.
    //-------------------------------------------------------------------------------
    public AbstractNodePosition getAdjacentNodeLoc(AxialDirection direction) {

        Vec3I adjRootIndex = new Vec3I(position.root.worldIndex);

        long adjRelX = position.relPosition.x;    // Uses long type, because the relative position of the adjacent
        long adjRelY = position.relPosition.y;    // node may exceed the standard bounds of relative position.
        long adjRelZ = position.relPosition.z;    // This will be rebased to use a root index in addition to rel pos.

        switch (direction) {            // Since we are looking for a node of equal size to this one, the node
            case PosX:                  // should be positioned exactly size units away in an arbitrary
                adjRelX += diameter;    // direction.
                break;
            case NegX:
                adjRelX -= diameter;
                break;
            case PosY:
                adjRelY += diameter;
                break;
            case NegY:
                adjRelY -= diameter;
                break;
            case PosZ:
                adjRelZ += diameter;
                break;
            case NegZ:
                adjRelZ -= diameter;
                break;
        }

        if (adjRelX >= ChunkMap.OT_ROOT_DIAMETER) {
            adjRelX -= ChunkMap.OT_ROOT_DIAMETER;
            adjRootIndex.x += 1;
        }
        if (adjRelX < 0) {
            adjRelX += ChunkMap.OT_ROOT_DIAMETER;
            adjRootIndex.x -= 1;
        }
        if (adjRelY >= ChunkMap.OT_ROOT_DIAMETER) {
            adjRelY -= ChunkMap.OT_ROOT_DIAMETER;
            adjRootIndex.y += 1;
        }
        if (adjRelY < 0) {
            adjRelY += ChunkMap.OT_ROOT_DIAMETER;
            adjRootIndex.y -= 1;
        }
        if (adjRelZ >= ChunkMap.OT_ROOT_DIAMETER) {
            adjRelZ -= ChunkMap.OT_ROOT_DIAMETER;
            adjRootIndex.z += 1;
        }
        if (adjRelZ < 0) {
            adjRelZ += ChunkMap.OT_ROOT_DIAMETER;
            adjRootIndex.z -= 1;
        }

        return new AbstractNodePosition(adjRootIndex, new Vec3I((int)adjRelX, (int)adjRelY, (int)adjRelZ));
    }

    /**
     * Begins the tree search for the node at a specific node position.
     * This function should be used in place of {@link #searchForNode(NodePosition, int, boolean) searchForNode()}
     * as that function is not conscious of differing root chunks.
     *
     * @param searchPosition
     *      The position of the node that the function is looking for.
     * @param minLevel
     *      The lowest level node that the function can return.
     * @return
     *      Returns a node of minLevel if the function found the exact node requested.
     *      Returns a node of minLevel + n if the function could not find the exact node requested, but
     *      could find a node that contains the point.
     *      Returns null if no existing nodes contain the requested searchPosition.
     */
    public Node startNodeSearch(AbstractNodePosition searchPosition, int minLevel) {
        if (!position.root.worldIndex.equals(searchPosition.rootIndex)) { // If target chunk is in a different root
            RootChunk root = position.root.parentMap.getRootChunk(searchPosition.rootIndex);
            if (root == null)
                return null;
            return root.searchForNode(new NodePosition(root, searchPosition.relPosition), minLevel, false);
        }
        return searchForNode(new NodePosition(position.root, searchPosition.relPosition), minLevel, true);
    }

    //-------------------------------------------------------------------------------
    //  Recursively searches for the node at a specific node position. This method
    //  should generally be called only by startNodeSearch as this method does not
    //  natively search for nodes in different roots.
    //-------------------------------------------------------------------------------
    public Node searchForNode(NodePosition searchPosition, int minLevel, boolean maySearchUp) {

        // Check if this is the node that the function is searching for.
        // If it is, we're done!
        if (searchPosition.relPosition.equals(position.relPosition)) {
            System.out.println("a");
            return this;
        }

        // Check if the bounding box of this node contains the target point.
        Vec3I minBound = position.relPosition;
        Vec3I maxBound = Vec3I.add(position.relPosition, diameter);

        System.out.println("b");

        if (searchPosition.relPosition.isInBounds(minBound, maxBound)) {
            System.out.println("c");
            return this; // We couldn't find the exact node, so we're going to return the closest voxel.
        } else {
            // Search position is not within this chunk, so we move up a level
            System.out.println("d");
            if (maySearchUp)
                return parent.searchForNode(searchPosition, minLevel, true);
            else
                return null;
        }
    }

    public boolean deleteVoxel(AbstractNodePosition targetPosition, int level, boolean alsoDeleteChunks) {
        System.out.println("deleting at level " + this.level);
        Node parent = startNodeSearch(targetPosition, level);
        System.out.println(parent.level);
        if (parent == null)
        {
            RootChunk newRoot = new RootChunk(targetPosition.rootIndex);
            position.root.parentMap.putRootChunk(newRoot);
            parent = newRoot;
            System.out.println("0");
        }
        if (parent instanceof Chunk) {
            System.out.println("1");
            return false;
        }

        Voxel v = (Voxel) parent;
        if (v.level >= level) {
            while (v.level > level) {
                Chunk containingChunk = v.split();
                Octant octant = containingChunk.getChildPositionAsOctant(targetPosition.relPosition);
                v = (Voxel) containingChunk.children[octant.getValue()];
                System.out.println("2");
            }
            if (v.level == level) {
                v.parent.deleteChild(v.octant);
                System.out.println("3");
                return true;
            }
        } else if (alsoDeleteChunks) {
            while (parent.level < level)
                parent = parent.parent;
            parent.parent.deleteChild(parent.octant);
            System.out.println("4");
        }
        System.out.println("5");
        return false;

    }

    public boolean isIntersectingRay(Ray ray, Ray.RayResult outputs)
    {
        Vec3 worldPos = position.getRenderPosition();
        Vec3 inverseDir = Vec3.div(new Vec3(1), ray.direction);
        float t1 = (worldPos.x - ray.startPoint.x) * inverseDir.x;
        float t2 = (worldPos.x + diameter - ray.startPoint.x) * inverseDir.x;

        float tminX = Math.min(t1, t2);
        outputs.facet = tminX == t1 ? AxialDirection.NegX : AxialDirection.PosX;
        float tmaxX = Math.max(t1, t2);

        t1 = (worldPos.y - ray.startPoint.y) * inverseDir.y;
        t2 = (worldPos.y + diameter - ray.startPoint.y) * inverseDir.y;

        float tminY = Math.min(t1, t2);
        float tmin = Math.max(tminX, tminY);
        if ( tmin == tminY)
        {
            outputs.facet = tminY == t1 ? AxialDirection.NegY : AxialDirection.PosY;
        }

        float tmaxY = Math.min(tmaxX, Math.max(t1, t2));

        t1 = (worldPos.z - ray.startPoint.z) * inverseDir.z;
        t2 = (worldPos.z + diameter - ray.startPoint.z) * inverseDir.z;

        float tminZ = Math.min(t1, t2);
        tmin = Math.max(tmin, tminZ);
        if (tmin == tminZ)
        {
            outputs.facet = tminZ == t1 ? AxialDirection.NegZ : AxialDirection.PosZ;
        }

        float tmaxZ = Math.min(tmaxY, Math.max(t1, t2));

        if (tmaxZ >= tmin /*&& tmin >= 0*/) // TODO: Voxels behind camera? CHECK THIS SHIT!
        {
            outputs.distance = tmin;
            return true;
        }
        return false;
    }

    public Octant getChildPositionAsOctant(Vec3I childRelPosition)
    {
        Vec3I difference = Vec3I.subtract(childRelPosition, Vec3I.add(this.position.relPosition, diameter/2));
        return Octant.fromSign(difference.x, difference.y, difference.z);
    }

    public void invalidateBuffer() {
        parent.invalidateBuffer();
    }
}
