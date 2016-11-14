package com.crosslink.battleprism.core.math;

import java.nio.FloatBuffer;

import com.crosslink.battleprism.core.framework.BufferableData;


/**
 * Visit https://github.com/integeruser/containers for project info, updates and license terms.
 * 
 * @author integeruser
 */
public class Vec4 extends BufferableData<FloatBuffer> {
	public static final int SIZE = (4 * Float.SIZE) / Byte.SIZE;
	
	
	public float x, y, z, w;
	
	
	public Vec4() {
	}
	
	public Vec4(float f) {
		x = f;
		y = f;
		z = f;
		w = f;
	}
	
	public Vec4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vec4(Vec3 vec, float w) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		this.w = w;
	}
	
	public Vec4(Vec4 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
	}

    public String toString()
    {
        return String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z) + " " + String.valueOf(w);
    }
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	
	@Override
	public FloatBuffer fillBuffer(FloatBuffer buffer) {
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);
		buffer.put(w);

		return buffer;
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	public Vec4 add(Vec4 rhs) {
		x += rhs.x;
		y += rhs.y;
		z += rhs.z;
		w += rhs.w;

		return this;
	}
	
	public Vec4 sub(Vec4 rhs) {
		x -= rhs.x;
		y -= rhs.y;
		z -= rhs.z;
		w -= rhs.w;

		return this;
	}
	
	public Vec4 mul(Vec4 rhs) {	   
		x *= rhs.x;
		y *= rhs.y;
		z *= rhs.z;
		w *= rhs.w;
		
		return this;
	}
	
	
	public Vec4 scale(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;

		return this;
	}

    public Vec4 normalize()
    {
        return scale(1/getLength());
    }

    public float getLength()
    {
        return (float)Math.sqrt((x*x + y*y + z*z + w*w));
    }

	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	public static Vec4 add(Vec4 lhs, Vec4 rhs) {
		Vec4 res = new Vec4(lhs);
		
		return res.add(rhs);
	}
	
	public static Vec4 sub(Vec4 lhs, Vec4 rhs) {
		Vec4 res = new Vec4(lhs);

		return res.sub(rhs);
	}
	
	public static Vec4 mul(Vec4 lhs, Vec4 rhs) {
		Vec4 res = new Vec4(lhs);

		return res.mul(rhs);
	}
	
	
	public static Vec4 scale(Vec4 vec, float scalar) {
		Vec4 res = new Vec4(vec);

		return res.scale(scalar);
	}
}