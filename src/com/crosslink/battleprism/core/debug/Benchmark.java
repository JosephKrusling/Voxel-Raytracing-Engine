package com.crosslink.battleprism.core.debug;

import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.glBeginQuery;
import static org.lwjgl.opengl.GL15.glEndQuery;
import static org.lwjgl.opengl.GL15.glGenQueries;
import static org.lwjgl.opengl.GL15.GL_QUERY_RESULT;
import static org.lwjgl.opengl.GL33.*;
//import static org.lwjgl.opengl.GL15.glGetQueryObject;

/**
 * Created by Thomas on 5/16/14.
 */
public class Benchmark {
    static long starttime;
    static ArrayList<Long> timeList = new ArrayList<Long>();
    public static void CPUandGPUstart() {
        if(!Keyboard.isKeyDown(Keyboard.KEY_G)) return;
        glFinish();
        starttime = System.nanoTime();
    }

    public static void CPUandGPUstop() {

        if(!Keyboard.isKeyDown(Keyboard.KEY_G)) return;
        glFinish();
        timeList.add(System.nanoTime() - starttime);
        if(timeList.size() > 10) {
            Long total = (long) 0 ;
            for(Long time : timeList) {
                total += time;
            }
            System.out.println(((float)total / timeList.size())/1000000 ); //try{Thread.sleep(1000);} catch(Exception ex) {}
            timeList.clear();
        }

        //throw new RuntimeException(String.valueOf(System.currentTimeMillis() - starttime));
    }
    static int query = -1;
 public static void GPUstart() {
        if(!Keyboard.isKeyDown(Keyboard.KEY_G)) return;
        if(query == -1) query = glGenQueries();

        timeList.add(glGetQueryObjectui64(query, GL_QUERY_RESULT));
        if(timeList.size() > 100) {
            Long total = (long) 0 ;
            for(Long time : timeList) {
                total += time;
            }
            System.out.println(((float)total / timeList.size())/1000000 ); //try{Thread.sleep(1000);} catch(Exception ex) {}
            timeList.clear();
        }

     glBeginQuery(GL_TIME_ELAPSED,query);

 }


    public static void GPUstop() {
        if(!Keyboard.isKeyDown(Keyboard.KEY_G)) return;

        glEndQuery(GL_TIME_ELAPSED);
    }




}