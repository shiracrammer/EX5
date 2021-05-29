package renderer;
import geometries.Intersectable.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import Primitives.Color;
import Primitives.Material;
import Primitives.Ray;
import Primitives.Vector;
import Primitives.util;
import Primitives.Point3D;

import elements.Camera;
import elements.LightSource;
import geometries.Intersectable;
import scene.Scene;

public class Render {
	public ImageWriter _imagewriter;
	public Scene _scene;
	
	
	private static final int MAX_CALC_COLOR_LEVEL = 10;
	private static final double MIN_CALC_COLOR_K = 0.001;
	public Render(ImageWriter iw,Scene sc) 
	{
		_imagewriter= iw;
		_scene=sc;
	}

	/*
	 * construct ray throw each pixel and calculate its color
	 */
	public void renderImage() throws Exception
	{
		Camera camera = _scene.get_camera();
		Intersectable geometries = _scene.get_geometries();
		Color background = _scene.get_background();
		int nX = _imagewriter.getNx();
        int nY = _imagewriter.getNy();
        double distance=_scene.get_distance();
        double width= _imagewriter.getWidth();
        double height= _imagewriter.getHeight();
        GeoPoint closestPoint=new GeoPoint (null,new Point3D(0,0,1) );
        for (int j = 0; j < nY; j++) 
        {
            for (int i = 0; i < nX; i++) 
            {
            	Ray ray = camera.constructRayThroughPixel(nX, nY, i, j, distance, width, height);
            	List<GeoPoint> intersectionPoints = geometries.findIntsersections(ray);
                if(intersectionPoints.isEmpty())
                	_imagewriter.writePixel(i, j, background.getColor());
                else
                {
                	 //closestPoint =  getClosestPoint(intersectionPoints);
                	  closestPoint = findCLosestIntersection(ray);
                     _imagewriter.writePixel(i, j, closestPoint == null ? _scene.get_background().getColor(): calcColor(closestPoint, ray).getColor());
                }
            }
		}
	}
	/*
	 * return the closest intersection point
	 */
	
	 public GeoPoint getClosestPoint(List<GeoPoint> intersectionPoints) 
	{
	    double minimum=_scene.get_camera().get_location().distance(intersectionPoints.get(0).point);
	    double minDistance;
	    GeoPoint Pmin=new GeoPoint(intersectionPoints.get(0).geometry,intersectionPoints.get(0).point);
		for (int i = 1; i < intersectionPoints.size(); i++)
		{
			minDistance=_scene.get_camera().get_location().distance(intersectionPoints.get(i).point);
			if(minimum>minDistance)
			{
				minimum=minDistance;
			    Pmin=intersectionPoints.get(i);
			}
		}
		return Pmin; 
	}
	 
	 /*
		 * return the closest intersection point from the ray
		 */
		private GeoPoint findCLosestIntersection(Ray ray) throws Exception
		{
			if(ray==null)
				return null;
			List<GeoPoint> intersectionPoints=_scene.get_geometries().findIntsersections(ray);
			  if (intersectionPoints.isEmpty())
		            return null;
		    double minimum=Double.MAX_VALUE;;//=ray.getPoint().distance(intersectionPoints.get(0).point);
		    double minDistance;
		    GeoPoint Pmin=null;
		    //Pmin=new GeoPoint(intersectionPoints.get(0).geometry,intersectionPoints.get(0).point);
			for (int i = 0; i < intersectionPoints.size(); i++)
			{
				minDistance=ray.getPoint().distance(intersectionPoints.get(i).point);
				if(minimum>minDistance)
				{
					minimum=minDistance;
				    Pmin=intersectionPoints.get(i);
				}
			}
			return Pmin; 	
		}

/*
 * return the color with the landing factor
*/
	 private Color calcColor(GeoPoint point,Ray inRay, int level, double k) throws Exception
	 {
		// Color color= _scene.get_ambientLight().get_intensity();
		 Color color = point.geometry.get_emmission();
		 Vector v = point.point.substract(_scene.get_camera().get_location()).normalize();
		 Vector n = point.geometry.getNormal(point.point).normalize();
		 Material material =point.geometry.get_material();
		 int nShiness = material.get_nShiness();
		 double kd = material.get_kd();
		 double ks = material.get_ks();
		 List<LightSource> lights=_scene.get_lights();
		 if(lights!=null)
		 {
			// System.out.println(lights.size());
		 for (LightSource lightSource : lights)
		 {
			 Vector l = lightSource.getL(point.point);
			 double nl = util.alignZero(n.dotProduct(l));
             double nv =util.alignZero(n.dotProduct(v));
			 if (((n.dotProduct(l))>0 && (n.dotProduct(v))>0)  || ((n.dotProduct(l))<0 && (n.dotProduct(v))<0) )
			 {
				 double ktr = transparency(lightSource, l, n, point);
				 if (ktr * k > MIN_CALC_COLOR_K) {	 
			 Color lightIntensity = lightSource.getIntensity(point.point).scale(ktr);
			 color = color.add(calcDiffusive(kd, nl, lightIntensity),calcSpecular(ks, l, n, v,nl, nShiness, lightIntensity));
						 }
		     }
         }
		 }
		 if (level == 1)
			 return Color.BLACK;
		 double kr = point.geometry.get_material().get_kr();
		 double kkr = k * kr;
		 if (kkr > MIN_CALC_COLOR_K) 
		 {
		 Ray reflectedRay = calcReflectedRay(n, point.point, inRay);
		 GeoPoint reflectedPoint = findCLosestIntersection(reflectedRay);
		 if (reflectedPoint != null)
		    color = color.add(calcColor(reflectedPoint, reflectedRay,level-1, kkr).scale(kr));
		 }
		 double kt = point.geometry.get_material().get_kt();
		 
		 double kkt = k * kt;
		 if (kkt > MIN_CALC_COLOR_K) 
		 {
		 Ray refractedRay = calcRefractedRay(n,inRay,point.point) ;
		 GeoPoint refractedPoint = findCLosestIntersection(refractedRay);
		 if (refractedPoint != null)
		    color = color.add(calcColor(refractedPoint, refractedRay, level-1, kkt).scale(kt));
		 }
		 return color;
	 }
	
	 
	 private Color calcColor(GeoPoint gp, Ray ray) throws Exception
	 {
		 return calcColor(findCLosestIntersection(ray), ray, MAX_CALC_COLOR_LEVEL, 1.0).add(
				 _scene.get_ambientLight().get_intensity());
 
	 }

	 /*
	  * calculate the diffuse 
	  */
	 private Color calcDiffusive(double kd,double nl,Color lightIntensity)
	 {
		 if (nl < 0)
			 nl = -nl;
		 return lightIntensity.scale(nl*kd);
	 }
	 /*
	  * calculate the specular
	  */
	 private Color calcSpecular(double ks,Vector l,Vector n,Vector v,double nl,int shine, Color lightIntensity) throws Exception
	 {
		 double s=shine;
		 Vector R = l.add(n.scale(-2 * nl));
		    double minusVR = -util.alignZero(R.dotProduct(v));
	        if (minusVR <= 0) {
	            return Color.BLACK; // view from direction opposite to r vector
	        }
		 return lightIntensity.scale(ks * Math.pow(minusVR, s));
	}
	 
	 /*
	  * print the grid
	  */
	public void printGrid(int interval, java.awt.Color color)
	{
		for (int j = 0; j < _imagewriter.getNy(); j++) 
        {
            for (int i = 0; i < _imagewriter.getNx(); i++) 
            {
            	if(i%interval==0||j%interval==0)
            		_imagewriter.writePixel(i, j, color);	
            }
        }
		_imagewriter.writeToImage();
     }
	
	/*
	 * check if the point has shadow
	 */
	private boolean unshaded(LightSource light, Vector l, Vector n, GeoPoint gp) throws Exception
	{
		Vector lightDirection = l.scale(-1); // from point to light source
		Ray lightRay = new Ray(gp.point, lightDirection,n);
		List<GeoPoint> intersections = _scene.get_geometries().findIntsersections(lightRay);
		if (intersections.isEmpty()) 
			return true;
		double lightDistance = light.getDistance(gp.point);
		for (GeoPoint geo : intersections) {
		   if (util.alignZero(geo.point.distance(geo.point)-lightDistance) <= 0 && gp.geometry.get_material().get_kt() == 0)
		       return false;
		}
		return true;
	}
	/*
	 * unshaded refactoring for geometries with transparency
	 */
	private double transparency(LightSource ls, Vector l, Vector n, GeoPoint geopoint) throws Exception
	{
		Vector lightDirection = l.scale(-1); // from point to light source
		Ray lightRay = new Ray(geopoint.point, lightDirection, n);
		List<GeoPoint> intersections = _scene.get_geometries().findIntsersections(lightRay);
		if (intersections.isEmpty()) 
			return 1.0;
		double lightDistance = ls.getDistance(geopoint.point);
		double ktr = 1.0;
		for (GeoPoint gp : intersections) {
		if (util.alignZero(gp.point.distance(geopoint.point)- lightDistance) <= 0) {
		ktr *= gp.geometry.get_material().get_kt();
		if (ktr < MIN_CALC_COLOR_K) 
			return 0.0;
		}
		}
		return ktr;
	}
	/*
	 * calculate the reflected ray
	 */
	private Ray calcReflectedRay(Vector n,Point3D point,Ray ray) throws Exception
	{
		Vector v=new Vector(ray.getDirection());
		double vn=v.dotProduct(n);
		if(vn==0)
			return null;
		Vector r= v.substract(n.scale(2*vn));
		return new Ray(point, r,n);
	}
	/*
	 * calculate the refracted ray
	 */
	private Ray calcRefractedRay(Vector n,Ray ray, Point3D point) throws Exception
	{
		return new Ray(point, ray.getDirection(),n);
	}
	
	
	
	
	

}
