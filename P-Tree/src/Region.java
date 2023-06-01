import java.util.List;
import java.util.ArrayList;

public class Region {
	private Point topLeft;
	private Point bottomRight;
	private Node potomak;
	
	public Region(Point tl, Point br, Node potomak) {
		topLeft = tl;
		bottomRight = br;
		this.potomak = potomak;
	}

	
	public Region(List<Region> regioni, Node potomak) {
		this.potomak = potomak;
		double minX = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		
		for(Region region : regioni) {
			minX = Math.min(minX, region.getTopLeft().getX());
            minY = Math.min(minY, region.getBottomRight().getY());
            maxX = Math.max(maxX, region.getBottomRight().getX());
            maxY = Math.max(maxY, region.getTopLeft().getY());
		}
		
		Point newTopLeft = new Point(minX, maxY);
		Point newBottomRight = new Point(maxX, minY);
		
		this.bottomRight = newBottomRight;
		this.topLeft = newTopLeft;
	}
	
	public void setPotomak(Node potomak) {
		this.potomak = potomak;
	}
	
	public Point getTopLeft() {
		return topLeft;
	}
	
	public Point getBottomRight() {
		return bottomRight;
	}
	
	public Node getPotomak() {
		return potomak;
	}
	
	public double getArea() {
		double width = Math.abs(bottomRight.getX() - topLeft.getX());
        double height = Math.abs(bottomRight.getY() - topLeft.getY());
        return width * height;
	}
	
	public boolean contains(Point point) {
		double xTacke = point.getX();
		double yTacke = point.getY();
		double xGoreLevo = topLeft.getX();
		double xDoleDesno = bottomRight.getX();
		double yGoreLevo = topLeft.getY();
		double yDoleDesno = bottomRight.getY();
		
		boolean xInRange = (xTacke >= xGoreLevo) && (xTacke <= xDoleDesno);
		boolean yInRange = (yTacke <= yGoreLevo) && (yTacke >= yDoleDesno);

        return xInRange && yInRange;
	}
	
	public boolean intersects(Region reg) {
		Point otherTopLeft = reg.getTopLeft();
        Point otherBottomRight = reg.getBottomRight();
      
        if (topLeft.getX() > otherBottomRight.getX() || bottomRight.getX() < otherTopLeft.getX()) {
            return false; 
        }

        if (topLeft.getY() < otherBottomRight.getY() || bottomRight.getY() > otherTopLeft.getY()) {
            return false; 
        }
        return true; 
	}
	
	public boolean contains(Region reg) {
		Point otherTopLeft = reg.getTopLeft();
        Point otherBottomRight = reg.getBottomRight();

        if (topLeft.getX() >= otherTopLeft.getX() &&
            topLeft.getY() <= otherTopLeft.getY() &&
            bottomRight.getX() <= otherBottomRight.getX() &&
            bottomRight.getY() >= otherBottomRight.getY()) {
            return true; 
        }
        return false; 
	}
	
	public void updateMBR(Region reg) {
		double minX = Math.min(topLeft.getX(), reg.getTopLeft().getX());
		double maxY = Math.max(topLeft.getY(), reg.getTopLeft().getY());
		double maxX = Math.max(bottomRight.getX(), reg.getBottomRight().getX());
		double minY = Math.min(bottomRight.getY(), reg.getBottomRight().getY());
		
		Point newTopLeft = new Point(minX, maxY);
		Point newBottomRight = new Point(maxX, minY);
		
		this.bottomRight = newBottomRight;
		this.topLeft = newTopLeft;
	}
	
	public double distance(Region reg) {
		double x = Math.max(0, Math.max(reg.getTopLeft().getX() - bottomRight.getX(), topLeft.getX() - reg.getBottomRight().getX()));
		double y = Math.max(0, Math.max(reg.getBottomRight().getY() - topLeft.getY(), bottomRight.getY() - reg.getTopLeft().getY()));
		
		return Math.sqrt(x*x + y*y);
	}
	
	public double izracunajUnionArea(Region reg) {
		double minX = Math.min(topLeft.getX(), reg.getTopLeft().getX());
		double minY = Math.min(bottomRight.getY(), reg.getBottomRight().getY());
		double maxX = Math.max(bottomRight.getX(), reg.getBottomRight().getX());
		double maxY = Math.max(topLeft.getY(), reg.getTopLeft().getY());
		
		return (maxX - minX) * (maxY - minY);
	}
}
