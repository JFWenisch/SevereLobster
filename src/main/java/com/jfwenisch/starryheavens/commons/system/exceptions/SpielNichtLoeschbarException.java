package com.jfwenisch.starryheavens.commons.system.exceptions;

import java.io.IOException;

import com.jfwenisch.starryheavens.commons.infrastructure.ResourceManager;

/**
 * Exception fuer ein nicht loeschbares Spiel
 * 
 * @author Lars Schlegelmilch
 */
public class SpielNichtLoeschbarException extends IOException {

    private static final ResourceManager resourceManager = ResourceManager
            .get();

    public SpielNichtLoeschbarException() {
        super(resourceManager.getText("exception.cant.delete.gamefile"));
    }
}
