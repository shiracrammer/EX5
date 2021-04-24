package scene;

import Primitives.Color;
import elements.AmbientLight;
import elements.Camera;
import geometries.Geometries;
import geometries.Intersectable;

public class Scene {

	private String _name;
	private Color _background;
	private AmbientLight _ambientLight;
	private Geometries _geometries;
	private Camera _camera;
	private double _distance;
	
	public Scene(String name) 
	{
		_geometries=new Geometries();
	}

	public String get_name() {
		return _name;
	}

	public Color get_background() {
		return _background;
	}

	public AmbientLight get_ambientLight() {
		return _ambientLight;
	}

	public Geometries get_geometries() {
		return _geometries;
	}

	public Camera get_camera() {
		return _camera;
	}

	public double get_distance() {
		return _distance;
	}

	public void set_background(Color _background) {
		this._background = _background;
	}

	public void set_ambientLight(AmbientLight _ambientLight) {
		this._ambientLight = _ambientLight;
	}

	public void set_camera(Camera _camera) {
		this._camera = _camera;
	}

	public void set_distance(double _distance) {
		this._distance = _distance;
	}
	/*
	 * add geometries to the list of the geometries
	 */
public void addGeometries(Intersectable...geometries)
{
	for(int i=0;i<geometries.length;i++)
		_geometries.add(geometries[i]);
}

}
