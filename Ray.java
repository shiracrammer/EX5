package Primitives;

public class Ray
{
Point3D point;
Vector direction;

public Ray(Ray other)
{
	
	point=new Point3D(other.point);
	direction=new Vector(other.direction);
}

public Ray(Point3D point1, Vector direct)
{
	
	point=point1;
	direction=direct;
}

public Point3D getPoint() 
{
	return point;
}


public Vector getDirection() throws Exception {
	return direction.normalize();
}

@Override
public String toString() 
{
	return "Ray [point=" + point + ", direction=" + direction + "]";
}


public boolean equals(Object obj) 
{
   if (this == obj) return true;
   if (obj == null) return false;
   if (!(obj instanceof Ray)) return false;
   Ray oth = (Ray)obj;
   return point.equals(oth.point) && direction.equals(oth.direction);
}

public  Point3D getPoint(double t) throws Exception 
{
	if(util.isZero(t))
		return point;
	Point3D p=new Point3D(point.add(direction.scale(t)));
	return p;
}



}
