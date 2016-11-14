#version 330

layout(std140) uniform;

layout(location = 0) in vec3 position;
layout(location = 5) in vec2 texCoord;

out vec2 colorCoord;

uniform Projection
{
	mat4 view;
	mat4 projection;
	mat4 invViewProjection;
	ivec4 viewPort;
};

void main()
{
	gl_Position = vec4(position, 1);
	colorCoord = texCoord;
}
