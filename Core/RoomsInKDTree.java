package byow.Core;

public class RoomsInKDTree {
    private boolean HORIZONTAL;
    private Node root;

    private class Node {
        Room room;
        boolean split;
        Node left;
        Node right;

        Node(Room r, boolean s) {
            this.room = r;
            this.split = s;
            left = null;
            right = null;
        }
    }

    public RoomsInKDTree() {
        HORIZONTAL = false;
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Node insert(Room room) {
        root = insertHelper(room, root, HORIZONTAL);
        return root;
    }

    private Node insertHelper(Room room, Node node, boolean split) {
        if (node == null) {
            return new Node(room, split);
        }
        if (compareRooms(room, node.room, split) < 0) {
            node.left = insertHelper(room, node.left, !split);
        } else {
            node.right = insertHelper(room, node.right, !split);
        }
        return node;
    }

    public Room nearest(Room room) {
        return nearestHelper(root, room, root.room);
    }

    // return nearest room but not absolutely because of not comparing bad side.
    private Room nearestHelper(Node node, Room fixedRoom, Room nearestRoom) {
        if (node == null) {
            return nearestRoom;
        }
        double bestDist = Room.distanceBetweenTwoRooms(nearestRoom, fixedRoom);
        double currDist = Room.distanceBetweenTwoRooms(node.room, fixedRoom);
        if (currDist < bestDist) {
            nearestRoom = node.room;
        }
        Node goodSideNode = null;
        if (compareRooms(fixedRoom, node.room, node.split) < 0) {
            goodSideNode = node.left;
        } else {
            goodSideNode = node.right;
        }
        nearestRoom = nearestHelper(goodSideNode, fixedRoom, nearestRoom);
        return nearestRoom;
    }

    private int compareRooms(Room r1, Room r2, boolean splitDim) {
        if (splitDim == HORIZONTAL) {
            return r1.pos.x - r2.pos.x;
        } else {
            return r1.pos.y - r2.pos.y;
        }
    }
}
