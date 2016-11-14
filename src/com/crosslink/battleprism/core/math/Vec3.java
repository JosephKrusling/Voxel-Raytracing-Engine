package com.crosslink.battleprism.core.math;

import java.nio.FloatBuffer;

import com.crosslink.battleprism.core.framework.BufferableData;


/**
 * Visit https://github.com/integeruser/containers for project info, updates and license terms.
 * 
 * @author integeruser
 */
public class Vec3 extends BufferableData<FloatBuffer> {
	public static final int SIZE = (3 * Float.SIZE) / Byte.SIZE;
	
	
	public float x, y, z;
	
	
	public Vec3() {
	}
	
	public Vec3(float f) {
		x = f;
		y = f;
		z = f;
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3(Vec3 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
	}
	
	public Vec3(Vec4 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;	
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	

	@Override
	public FloatBuffer fillBuffer(FloatBuffer buffer) {
		buffer.put(x);
		buffer.put(y);
		buffer.put(z);

		return buffer;
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	

	public Vec3 add(Vec3 rhs) {
		x += rhs.x;
		y += rhs.y;
		z += rhs.z;
		
		return this;
	}
	
	public Vec3 sub(Vec3 rhs) {
		x -= rhs.x;
		y -= rhs.y;
		z -= rhs.z;
		
		return this;
	}
	
	public Vec3 mul(Vec3 rhs) {
		x *= rhs.x;
		y *= rhs.y;
		z *= rhs.z;
		
		return this;
	}

    public Vec3 div(Vec3 rhs)  {
        x /= rhs.x;
        y /= rhs.y;
        z /= rhs.z;

        return this;
    }
	
	
	public Vec3 scale(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		
		return this;
	}
	

	public Vec3 negate() {
		x = -x;
		y = -y;
		z = -z;

		return this;
	}

    public Vec3 copy(Vec3 copyFrom){
        x = copyFrom.x;
        y = copyFrom.y;
        z = copyFrom.z;
        return this;
    }

	public String toString()
    {
        return String.valueOf(x) + " " + String.valueOf(y) + " " + String.valueOf(z);
    }

    public Vec3 normalize()
    {
        return scale(1/getLength());
    }

    public float getLength()
    {
        return (float)Math.sqrt((x*x + y*y + z*z));
    }
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	
	public static Vec3 add(Vec3 lhs, Vec3 rhs) {
		Vec3 res = new Vec3(lhs);
		
		return res.add(rhs);
	}
	
	public static Vec3 sub(Vec3 lhs, Vec3 rhs) {
		Vec3 res = new Vec3(lhs);

		return res.sub(rhs);
	}	
	
	public static Vec3 mul(Vec3 lhs, Vec3 rhs) {	
		Vec3 res = new Vec3(lhs);
		
		return res.mul(rhs);
	}
    public static Vec3 div(Vec3 lhs, Vec3 rhs) {
        Vec3 res = new Vec3(lhs);

        return res.div(rhs);
    }
	
	public static Vec3 scale(Vec3 vec, float scalar) {
		Vec3 res = new Vec3(vec);
		
		return res.scale(scalar);
	}
	

	public static Vec3 negate(Vec3 vec) {
		Vec3 res = new Vec3(vec);
		
		return res.negate();
	}

    public static Vec3 normalize(Vec3 vec) {
        Vec3 res = new Vec3(vec);

        return res.normalize();
    }

    public static float dot(Vec3 lhs, Vec3 rhs) {
        return lhs.x * rhs.x + lhs.y * rhs.y + lhs.z * rhs.z;
    }

    public static Vec3 cross(Vec3 lhs, Vec3 rhs) {
        Vec3 cross = new Vec3();

        cross.x = lhs.y * rhs.z - lhs.z * rhs.y;
        cross.y = lhs.z * rhs.x - lhs.x * rhs.z;
        cross.z = lhs.x * rhs.y - lhs.y * rhs.x;

        return cross;
    }
}