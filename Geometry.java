package geometries;

import java.util.List;

import Primitives.Point3D;
import Primitives.Ray;
import Primitives.Vector;

public interface Geometry extends Intersectable
{
public Vector getNormal(Point3D point) throws Exception;

public List<Point3D> findIntsersections(Ray ray) throws Exception ;

}
