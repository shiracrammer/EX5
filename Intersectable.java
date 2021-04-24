package geometries;
import java.util.List;

import Primitives.Point3D;
import Primitives.Ray;

public interface Intersectable
{
	List<Point3D> findIntsersections(Ray ray)throws Exception ;
}
