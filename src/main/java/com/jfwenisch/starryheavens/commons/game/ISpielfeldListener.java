package com.jfwenisch.starryheavens.commons.game;

import java.io.Serializable;
import java.util.EventListener;

import com.jfwenisch.starryheavens.commons.infrastructure.enums.SpielmodusEnumeration;

/**
 * 
 * @author Lutz Kleiber
 * 
 */
public interface ISpielfeldListener extends Serializable, EventListener {

    void spielsteinChanged(Spielfeld spielfeld, int x, int y,
            Spielstein changedStein);

    void spielmodusChanged(Spielfeld spielfeld,
            SpielmodusEnumeration neuerSpielmodus);
}
