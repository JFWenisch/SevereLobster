package com.jfwenisch.starryheavens.commons.game;


public class SH_R_SpielUeberpuefen implements Runnable
{

SternenSpielApplicationBackend oSpiel;
	public SH_R_SpielUeberpuefen(SternenSpielApplicationBackend sternenSpielApplicationBackend) {
	oSpiel=sternenSpielApplicationBackend;
	}

	public void run() 
	{
	
		try 

		{

			Thread.sleep(3000);
			if(oSpiel!=null)
			oSpiel.zurueckZumLetztenFehlerfreienSpielzug();
		} 
		catch (InterruptedException e) 
		{


		}	
	}

}
