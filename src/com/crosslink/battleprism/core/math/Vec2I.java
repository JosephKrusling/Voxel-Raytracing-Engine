package com.crosslink.battleprism.core.math;

import com.crosslink.battleprism.core.framework.BufferableData;

import java.nio.FloatBuffer;


/**
 * Visit https://github.com/integeruser/containers for project info, updates and license terms.
 * 
 * @author integeruser
 */

// TODO: the fuck?
public class Vec2I extends BufferableData<FloatBuffer> {
	public static final int SIZE = (2 * Float.SIZE) / Byte.SIZE;


	public int x, y;


	public Vec2I() {
	}

	public Vec2I(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vec2I(Vec2I vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	
	@Override
	public FloatBuffer fillBuffer(FloatBuffer buffer) {
		buffer.put(x);
		buffer.put(y);
		
		return buffer;
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	
	public Vec2I add(Vec2I rhs) {
		x += rhs.x;
		y += rhs.y;
		
		return this;
	}
	
	public Vec2I sub(Vec2I rhs) {
		x -= rhs.x;
		y -= rhs.y;
		
		return this;
	}
	
	public Vec2I mul(Vec2I rhs) {
		x *= rhs.x;
		y *= rhs.y;
		
		return this;
	}
	
	
	public Vec2I scale(float scalar) {
		x *= scalar;
		y *= scalar;
		
		return this;
	}
	

	public Vec2I negate() {
		x = -x;
		y = -y;

		return this;
	}
	public String toString() {
        return String.valueOf(x) + " : " + String.valueOf(y);
    }
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */	
	
	public static Vec2I add(Vec2I lhs, Vec2I rhs) {
		Vec2I res = new Vec2I(lhs);
		
		return res.add(rhs);
	}
	
	public static Vec2I sub(Vec2I lhs, Vec2I rhs) {
		Vec2I res = new Vec2I(lhs);

		return res.sub(rhs);
	}	
	
	public static Vec2I mul(Vec2I lhs, Vec2I rhs) {
		Vec2I res = new Vec2I(lhs);
		
		return res.mul(rhs);
	}
	
	
	public static Vec2I scale(Vec2I vec, float scalar) {
		Vec2I res = new Vec2I(vec);
		
		return res.scale(scalar);
	}
	

	public static Vec2I negate(Vec2I vec) {
		Vec2I res = new Vec2I(vec);
		
		return res.negate();
	}
}