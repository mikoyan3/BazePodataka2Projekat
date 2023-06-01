import java.util.List;

public class main {

	public static void main(String[] args) {
        RTree rTree = new RTree(4, 2);
        //rTree.ucitajIzFajla("tacke.txt");
        Point point1 = new Point(1.0, 1.0);
        Point point2 = new Point(2.0, 2.0);
        Point point3 = new Point(3.0, 3.0);
        Point point4 = new Point(4.0, 4.0);
        Point point5 = new Point(5.0, 5.0);
        Point point6 = new Point(6.0, 6.0);
        Point point7 = new Point(7.0, 6.0);
        Point point8 = new Point(8.0, 6.0);
        Point point9 = new Point(9.0, 6.0);
        Point point10 = new Point(10.0, 6.0);
        Point point11 = new Point(11.0, 6.0);
        Point point12 = new Point(12.0, 6.0);
        Point point13 = new Point(13.0, 6.0);

        rTree.insert(point1);
        rTree.insert(point2);
        rTree.insert(point3);
        rTree.insert(point4);
        rTree.insert(point5);
        rTree.insert(point6);
        rTree.insert(point7);
        rTree.insert(point8);
        rTree.insert(point9);
        rTree.insert(point10);
        rTree.insert(point11);
        rTree.insert(point12);
        rTree.insert(point13);
        
        rTree.printTree();
        
        Point rangeTopLeft = new Point(3.0, 7.0);
        Point rangeBottomRight = new Point(7.0, 4.5);
        Region searchRegion = new Region(rangeTopLeft, rangeBottomRight, null);
        List<Region> results = rTree.rangeSearch(searchRegion);

        System.out.println("Range search:");
        for (Region result : results) {
            System.out.println("Tacka: (" + result.getTopLeft().getX() + ", " + result.getTopLeft().getY() + ")");
        }
	}
}
