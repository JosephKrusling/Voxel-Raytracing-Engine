package com.crosslink.battleprism.server.session.group;

import com.crosslink.battleprism.server.session.Session;

import java.util.ArrayList;

/**
 * Created by Joseph on 5/23/2014.
 */
public class SessionGroup {

    public static SessionGroup allSessions = new SessionGroup();

    public ArrayList<Session> sessions = new ArrayList<Session>();

}
