package elements;

import Primitives.*;


public class Camera
{
Point3D _location;
Vector _Vup;
Vector _Vto;
Vector _Vright;


	public Point3D get_location() {
	return _location;
}


public Vector get_Vup() {
	return _Vup;
}


public Vector get_Vto() {
	return _Vto;
}


public Vector get_Vright() {
	return _Vright;
}


	public Camera(Point3D p,Vector to,Vector up) throws Exception 
	{
		if(up.dotProduct(to)==0)
		{
		 _Vup=up.normalize();
		 _Vto=to.normalize();
		_Vright=(to.crossProduct(up)).normalize();
		 _location=p;
		}
		else
			 throw new IllegalArgumentException("The vectors arent orthogonal");
	}

	public Ray constructRayThroughPixel(int nX, int nY, int j, int i, double screenDistance, double screenWidth, double screenHeight) throws Exception
	{
		Point3D _pc=new Point3D(_location.add(_Vto.scale(screenDistance)));
		double Ry=screenHeight/nY;
		double Rx=screenWidth/nX;
		double Yi= (i - (nY-1)/2d)*Ry;
		double Xj= (j -(nX-1)/2d)*Rx ;
		Point3D Pij=_pc;
		if (Xj!= 0 ) 
		     Pij=Pij.add(_Vright.scale(Xj));
		if (Yi!= 0 ) 
		     Pij=Pij.add(_Vup.scale(-Yi));
       return new Ray(_location,Pij.substract(_location));
	}

}
