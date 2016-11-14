package com.crosslink.battleprism.core.framework;

/**
 * Created with IntelliJ IDEA.
 * User: Joseph
 * Date: 1/25/14
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GameState {

    void init();
    void update(float lastFrameDuration);
    void display(float lastFrameDuration);
    void reshapeWindow();
}
