package geometries;
import java.util.List;

import Primitives.Point3D;
import Primitives.Ray;
import Primitives.Vector;
public class Triangle extends Polygon 
{

public Triangle(Point3D p1,Point3D p2,Point3D p3) throws Exception
{
	super(p1,p2,p3);
}
	
public Vector getNormal(Point3D point) throws Exception
{
	return super.getNormal(point);
}


public List<Point3D> findIntsersections(Ray ray) throws Exception
{
	return super.findIntsersections(ray);
}

}
