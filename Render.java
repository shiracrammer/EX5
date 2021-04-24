package renderer;

import java.util.List;

import Primitives.Color;
import Primitives.Ray;
import Primitives.Point3D;

import elements.Camera;
import geometries.Intersectable;
import scene.Scene;

public class Render {
	public ImageWriter _imagewriter;
	public Scene _scene;
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
        Point3D closestPoint=new Point3D(0,0,1);
        for (int j = 0; j < nY; j++) 
        {
            for (int i = 0; i < nX; i++) 
            {
            	Ray ray = camera.constructRayThroughPixel(nX, nY, j, i, distance, width, height);
            	List<Point3D> intersectionPoints = geometries.findIntsersections(ray);
                if(intersectionPoints.isEmpty())
                	_imagewriter.writePixel(i, j, background.getColor());
                else
                {
                	 closestPoint =  getClosestPoint(intersectionPoints);
                     _imagewriter.writePixel(i, j, calcColor(closestPoint).getColor());
                }
            }
		}
	}
	/*
	 * return the closest intersection point
	 */
	 private Point3D getClosestPoint(List<Point3D> intersectionPoints) 
	{
		
	    double minimum=_scene.get_camera().get_location().distance(intersectionPoints.get(0));
	    double minDistance;
	    Point3D Pmin=new Point3D(intersectionPoints.get(0));
		for (int i = 1; i < intersectionPoints.size(); i++)
		{
			minDistance=_scene.get_camera().get_location().distance(intersectionPoints.get(i));
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
	 private Color calcColor(Point3D point)
	 {
		 return _scene.get_ambientLight().get_intensity();
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


}
