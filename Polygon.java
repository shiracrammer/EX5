package geometries;
import Primitives.Color;
import java.util.ArrayList;
import java.util.List;
import Primitives.*;
import static Primitives.util.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 * 
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> _vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane _plane;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     * 
     * @param vertices list of vertices according to their order by edge path
     * @throws Exception 
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex></li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) throws Exception {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        _vertices = List.of(vertices);
        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        _plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3) return; // no need for more tests for a Triangle

        Vector n = _plane.get_normal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].substract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].substract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].substract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].substract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
          
        }
    }
    /*
     * constructor with color and list of vertices
     */
public Polygon(Color color,Point3D... vertices) throws Exception
{
	this(vertices);
	_emmission=new Color(color.getColor());
}
/*
 * constructor with material, color and list of vertices
 */
public Polygon(Material material, Color color,Point3D... vertices) throws Exception
{
	this(color, vertices);
	_material=material;
}
    @Override
    public Vector getNormal(Point3D point) throws Exception {
        return _plane.get_normal().normalize();
    }

	@Override
	public List<GeoPoint> findIntsersections(Ray ray) throws Exception
	{
		
		List<GeoPoint> result= _plane.findIntsersections(ray);
		   if (result == null)
	            return null;
		List<Vector> v =new ArrayList<>() ;
		List<Vector> n = new ArrayList<>() ;
        boolean flag;
		for (int i=0;i<_vertices.size();i++)
		{
			v.add(_vertices.get(i).substract(ray.getPoint()));
		}
		
		for (int i=0;i<v.size()-1;i++)
		{
			n.add((v.get(i).crossProduct(v.get(i+1))).normalize());
		}
		n.add((v.get(v.size()-1).crossProduct(v.get(0))).normalize());
		
		flag=ray.getDirection().dotProduct(n.get(0))>0;	
		int i=1;
		while(i<n.size()&&ray.getDirection().dotProduct(n.get(i))>0==flag)
		{
			  i++;
		}
		if(i!=n.size())
			return null;
		 List<GeoPoint> result1 = new ArrayList<>();
        for (GeoPoint geo : result) 
            result1.add(new GeoPoint(this, geo.point));
        return result1;
	}
}

