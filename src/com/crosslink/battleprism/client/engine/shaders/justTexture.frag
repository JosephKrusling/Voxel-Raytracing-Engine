#version 330

in vec2 colorCoord;

out vec4 outputColor;

uniform sampler2D diffuseColorTex;

uniform Projection
{
	mat4 view;
	mat4 projection;
	mat4 invViewProjection;
	ivec4 viewPort;
};


void main()
{

   // float f=100.0;
    //float n = 1;
    //float z = (2 * n) / (f + n - texture(diffuseColorTex, colorCoord).y * (f - n));

	//outputColor =  vec4(z,z,z,1);
    //outputColor = vec4(1,0,0,1);
	outputColor =  texture(diffuseColorTex, colorCoord);

    //if(texture(diffuseColorTex, colorCoord).x > 0.60 && texture(diffuseColorTex, colorCoord).x < .70) outputColor = vec4(0.2,1,0.2,1);
	//outputColor =  vec4(1,1,1,1);
}
