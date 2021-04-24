/**
 * 
 */
package unittests;


import static org.junit.Assert.*;

import org.junit.Test;

import geometries.*;
import Primitives.*;

/**
 * Testing Polygons
 * @author SHIRA
 *
 */
public class PolygonTests {

    /**
     * Test method for
     * {@link geometries.Polygon#Polygon(primitives.Point3D, primitives.Point3D, primitives.Point3D, primitives.Point3D)}.
     * @throws Exception 
     */
    @Test
    public void testConstructor() throws Exception {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(-1, 1, 1));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // TC02: Wrong vertices order
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(0, 1, 0),
                    new Point3D(1, 0, 0), new Point3D(-1, 1, 1));
            fail("Constructed a polygon with wrong order of vertices");
        } catch (IllegalArgumentException e) {}

        // TC03: Not in the same plane
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 2, 2));
            fail("Constructed a polygon with vertices that are not in the same plane");
        } catch (IllegalArgumentException e) {}

        // TC04: Concave quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0.5, 0.25, 0.5));
            fail("Constructed a concave polygon");
        } catch (IllegalArgumentException e) {}

        // =============== Boundary Values Tests ==================

        // TC10: Vertix on a side of a quadrangular
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0.5, 0.5));
            fail("Constructed a polygon with vertix on a side");
        } catch (IllegalArgumentException e) {}

        // TC11: Last point = first point
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 0, 1));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

        // TC12: Collocated points
        try {
            new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0),
                    new Point3D(0, 1, 0), new Point3D(0, 1, 0));
            fail("Constructed a polygon with vertice on a side");
        } catch (IllegalArgumentException e) {}

    }

    /**
     * Test method for {@link geometries.Polygon#getNormal(primitives.Point3D)}.
     * @throws Exception 
     */
    @Test
    public void testGetNormal() throws Exception {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here
        Polygon pl = new Polygon(new Point3D(0, 0, 1), new Point3D(1, 0, 0), new Point3D(0, 1, 0),new Point3D(-1, 1, 1));
        double sqrt3 = Math.sqrt(1d / 3);
        assertEquals("Bad normal to trinagle", new Vector(sqrt3, sqrt3, sqrt3), pl.getNormal(new Point3D(0, 0, 1)));
    }
    
    @Test
  
    public void testfindIntersections() throws Exception 
    {
    	//TC01:Inside polygon
    	Polygon tr=new Polygon(new Point3D(0, -2, 0),new Point3D(0, 0, 2),new Point3D(0, 2, 0));
    	assertEquals("Inside polygon",new Point3D(0, 0, 0.6),tr.findIntsersections(new Ray(new Point3D(3,0,0),new Vector(-5,0,1))).get(0));
        //TC02:Outside the polygon
    	assertEquals("Outside polygon",null,tr.findIntsersections(new Ray(new Point3D(3,0,0),new Vector(0,0,1))));
        //TC03:On edge's continuation
    	 tr=new Polygon(new Point3D(0, 0, 0),new Point3D(0, 0, 2),new Point3D(0, 2, 0));
     	assertEquals("On edge's continuation",null,tr.findIntsersections(new Ray(new Point3D(-1,0,3),new Vector(1,0,0))));
     	//TC04:On The edge
     	assertEquals("On The edge",new Point3D(0, 0,1),tr.findIntsersections(new Ray(new Point3D(-1,0,1),new Vector(1,0,0))).get(0));
     	//TC05:On The vertex
     	assertEquals("On The vertex",new Point3D(0, 0,2),tr.findIntsersections(new Ray(new Point3D(-1,0,2),new Vector(1,0,0))).get(0));


        
    }
   
}
