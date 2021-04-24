package geometries;

import java.util.List;

import Primitives.Point3D;
import Primitives.Ray;
import Primitives.Vector;

public class Plane implements Geometry
{
Point3D _p;
Vector _normal;
@Override
public Vector getNormal(Point3D point) {
	return _normal;
}

public Plane(Point3D p1, Point3D p2, Point3D p3) throws Exception
{

   Vector U = new Vector (p2.substract(p1));
   Vector V = new Vector (p3.substract(p1));
   Vector N = U.crossProduct(V);
    _normal=N;
	_p=p1;
}
public Plane(Point3D p, Vector normal)
{
	try
	{
	_p=p;
	_normal=normal;
	}
	catch (Exception e) {}
}

public Point3D get_p() {
	return _p;
}

public Vector get_normal() {
	return _normal;
}

@Override
public String toString() {
	return "Plane [_p=" + _p + ", _normal=" + _normal + "]";
}
 
@Override
public List<Point3D> findIntsersections(Ray ray) throws Exception 
{
	Vector v;
	double mone;
	Point3D p1=new Point3D(ray.getPoint());
	try
	{
	    v=new Vector(_p.substract(p1));
	}
	catch(IllegalArgumentException e)
	{
		return null;
	}
    mone=_normal.dotProduct(v);
	double mechane=_normal.dotProduct(ray.getDirection());
	if(mechane==0)
		return null;
	if(mone==0)//the point is in the plane
		return List.of(p1);
	double t=mone/mechane;
	if(t<=0)
		return null;
	return List.of(ray.getPoint(t));
	
}

}
