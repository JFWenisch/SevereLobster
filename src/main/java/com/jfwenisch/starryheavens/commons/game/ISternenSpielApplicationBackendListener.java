package com.jfwenisch.starryheavens.commons.game;

import java.util.EventListener;

import com.jfwenisch.starryheavens.commons.infrastructure.enums.SpielmodusEnumeration;

public interface ISternenSpielApplicationBackendListener extends EventListener {

    void spielmodusChanged(
            SternenSpielApplicationBackend sternenSpielApplicationBackend,
            Spiel spiel, SpielmodusEnumeration newSpielmodus);

    void spielsteinChanged(
            SternenSpielApplicationBackend sternenSpielApplicationBackend,
            Spiel spiel, ISpielfeldReadOnly spielfeld, int x, int y,
            Spielstein newStein);

    void spielfeldChanged(
            SternenSpielApplicationBackend sternenSpielApplicationBackend,
            Spiel spiel, ISpielfeldReadOnly newSpielfeld);

    void spielChanged(
            SternenSpielApplicationBackend sternenSpielApplicationBackend,
            Spiel spiel);

}
