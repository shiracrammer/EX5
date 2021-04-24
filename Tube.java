package geometries;

import java.util.List;

import Primitives.Point3D;

import Primitives.Ray;
import Primitives.Vector;
import Primitives.util;
public class Tube extends RadialGeometry {
	Ray _axisRay;

	public Tube(double r,Ray ray) 
	{
		super(r);
		_axisRay=ray;
	}

	@Override
	public String toString() {
		return "Tube [_axisRay=" + _axisRay + ", _radius=" + _radius + "]";
	}

	public Ray get_axisRay() {
		return _axisRay;
	}

	@Override
	public Vector getNormal(Point3D point) throws Exception
	{
	Point3D o=_axisRay.getPoint();
	Vector v=_axisRay.getDirection();
	double t= point.substract(o).dotProduct(v);
	if(!util.isZero(t))
		o=o.add(v.scale(t));
	return point.substract(o).normalize();
	}

	@Override
	public List<Point3D> findIntsersections(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}
}
