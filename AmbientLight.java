package elements;

import Primitives.Color;

public class AmbientLight {
public Color _intensity; 

	public AmbientLight(Color color,double ka)
	{
		_intensity=color.scale(ka);
	}

	public Color get_intensity() 
	{
		return _intensity;
	}
	

}
