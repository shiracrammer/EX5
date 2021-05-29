package unittests;
import renderer.ImageWriter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import elements.*;
import geometries.*;
import geometries.Intersectable.GeoPoint;
import Primitives.*;
import renderer.ImageWriter;
import renderer.Render;
import scene.Scene;

/**
 * Test rendering abasic image
 * 
 * @author Dan
 */
public class RenderTests 
{

    /**
     * Produce a scene with basic 3D model and render it into a jpeg image with a
     * grid
     * @throws Exception 
     */
	
	
	
    @Test
    public void basicRenderTwoColorTest() throws Exception {
        Scene scene = new Scene("Test scene");
        scene.set_camera(new Camera(Point3D.zero, new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.set_distance(100);
        scene.set_background(new Color(75, 127, 90));
        scene.set_ambientLight(new AmbientLight(new Color(255, 191, 191), 1));

        scene.addGeometries(new Sphere(new Point3D(0, 0, 100),50 ));

        scene.addGeometries(
                new Triangle(new Point3D(100, 0, 100), new Point3D(0, 100, 100), new Point3D(100, 100, 100)),
                new Triangle(new Point3D(100, 0, 100), new Point3D(0, -100, 100), new Point3D(100, -100, 100)),
                new Triangle(new Point3D(-100, 0, 100), new Point3D(0, 100, 100), new Point3D(-100, 100, 100)),
                new Triangle(new Point3D(-100, 0, 100), new Point3D(0, -100, 100), new Point3D(-100, -100, 100)));

        ImageWriter imageWriter = new ImageWriter("base render test", 500, 500, 500, 500);
        Render render = new Render(imageWriter, scene);

        render.renderImage();
        render.printGrid(50, java.awt.Color.YELLOW);
        render._imagewriter.writeToImage();
    }

	 
    @Test
   
    public void basicRenderMultiColorTest() throws Exception 
    {
        Scene scene = new Scene("Test scene");
        scene.set_camera(new Camera(Point3D.zero, new Vector(0, 0, 1), new Vector(0, -1, 0)));
        scene.set_distance(100);
        scene.set_background(Color.BLACK);
        scene.set_ambientLight(new AmbientLight(new Color(java.awt.Color.WHITE), 0.2));

        scene.addGeometries(new Sphere(new Point3D(0, 0, 100),50 ));

        scene.addGeometries(
                        new Triangle(new Color(java.awt.Color.BLUE),
                        new Point3D(100, 0, 100), new Point3D(0, 100, 100), new Point3D(100, 100, 100)),      // lower right
                new Triangle(
                        new Point3D(100, 0, 100), new Point3D(0, -100, 100), new Point3D(100, -100, 100)),    // upper right
                new Triangle(new Color(java.awt.Color.RED),
                        new Point3D(-100, 0, 100), new Point3D(0, 100, 100), new Point3D(-100, 100, 100)),    // lower left
                new Triangle(new Color(java.awt.Color.GREEN),
                        new Point3D(-100, 0, 100), new Point3D(0, -100, 100), new Point3D(-100, -100, 100))); // upper left
       
        
        ImageWriter imageWriter = new ImageWriter("color render test", 500, 500, 500, 500);
        Render render = new Render(imageWriter, scene);
        render.renderImage();
        render.printGrid(50, java.awt.Color.WHITE);
        render._imagewriter.writeToImage();


    }
    
	 @Test
	 
	public void getClosestPoint() throws Exception
	{
        Scene scene = new Scene("Test scene");

        ImageWriter imageWriter = new ImageWriter("color render test", 500, 500, 500, 500);
        Render render = new Render(imageWriter, scene);
		Sphere sphere=new Sphere(new Point3D(1,0,0),1);
		GeoPoint p=new GeoPoint(sphere,new Point3D(0,0,0));
		assertEquals("check closestPoint",p.point,render.getClosestPoint(sphere.findIntsersections(new Ray(new Point3D(-1,0,0),new Vector(1,0,0)))).point);
	}
}



