package com.jfwenisch.starryheavens.commons.game;

/**
 *  Die Klasse stellt einen einzelnen Schritt zur Lösung eines Spielfeldes dar.
 *
 *  @author Christian Lobach
 */
public interface SolvingStep {
    public Spielfeld execute(Spielfeld input);


}
