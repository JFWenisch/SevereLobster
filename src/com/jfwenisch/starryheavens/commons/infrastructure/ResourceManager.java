package com.jfwenisch.starryheavens.commons.infrastructure;


import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.jfwenisch.starryheavens.commons.GraphicUtils;


/**
 * Helperklasse zur Resourcenverwaltung
 *
 * @author Lars Schlegelmilch
 */
public class ResourceManager {

	private static final ResourceManager instance = new ResourceManager();

	private Properties propertyfile;
	private Properties userpropertyFile;
	private Locale language;

	private ResourceManager() {
		propertyfile = new Properties();
		try {
			propertyfile.load(getProperties());
		} catch (IOException e) {
			e.printStackTrace();
		}
		userpropertyFile = new Properties();
		try {
			userpropertyFile.load(getUserProperties());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gibt die Instanz des ResourceManager zurueck
	 *
	 * @return ResourceManager-Instanz
	 */
	public static ResourceManager get() {
		return instance;
	}
	/**
	 * Sucht den aktuellen Benutzer und initialisiert ihn ggf.
	 * @return <GlobaleKonstanten.USERNAME> initialisiert
	 * @author Jean
	 * @throws Exception 
	 */
	public  String getUser() throws Exception
	{
		String strUserName = null;
		try 
		{
			strUserName = instance.getUserProperty("user.name");

		} catch (Exception e) 
		{

			strUserName = JOptionPane.showInputDialog(null, "Choose Username...", "Welcome!",JOptionPane.QUESTION_MESSAGE);
		}
		//Neuen Benutzer erstellen
		if(strUserName.isEmpty() | strUserName==null)
			strUserName = JOptionPane.showInputDialog(null, "Choose Username...", "Welcome!",JOptionPane.QUESTION_MESSAGE);
		setUserProperty("user.name",strUserName);
		GlobaleKonstanten.USERNAME=strUserName;
		return GlobaleKonstanten.USERNAME;

	}
	public int getScore(String string)
	{
		int iLevelHighscore = 0;
		try 
		{
			iLevelHighscore = Integer.parseInt(instance.getUserProperty(string));

		} catch (Exception e) 
		{

			System.out.println("Score für "+string+" nicht verfügbar");
		}
		return iLevelHighscore;
	}

	/**
	 * Aender die Spracheinstellung des ResourceManagers, sodass auf Properties
	 * der jeweiligen Sprache zurueckgegriffen wird
	 *
	 * @param locale Sprache/Land
	 */
	public void setLanguage(Locale locale) {
		if (locale == null) {
			String localeString = (String) userpropertyFile.get("user.language");
			if (localeString.equals(Locale.GERMAN.toString())) {
				locale = Locale.GERMAN;
			} else if (localeString.equals(Locale.ENGLISH.toString())) {
				locale = Locale.ENGLISH;
			} else {
				locale = Locale.GERMAN;
			}
		}
		language = locale;
		try {
			propertyfile.load(getProperties(language));
		} catch (IOException e) {
			e.printStackTrace();
		}
		userpropertyFile.put("user.language", locale.toString());
		saveUserProperties();

	}
	public void mute(boolean bTrue)
	{
		if(bTrue)
		userpropertyFile.put("mute", "true");
		else
			userpropertyFile.put("mute", "false");
			
		saveUserProperties();
	}
	public boolean isMuted()
	{
		String strStatus = userpropertyFile.getProperty("mute");
		if(strStatus==null)
		{
			mute(false);
			return false;
		}
		if(strStatus.equals("true"))
			return true;
		else
			return false;
	}
	/**
	 * Sichert die Userproperties
	 */
	private void saveUserProperties() {
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(GlobaleKonstanten.USER_PROPERTIES);
			userpropertyFile.store(stream, "User.properties");
		} catch (IOException e) {
			System.out.println("Neue Benutzereinstellungen konnten nicht gesichert werden.");
		}
		finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				System.out.println("Neue Benutzereinstellungen konnten nicht gesichert werden.");
			}
		}
	}

	/**
	 * Getter fuer aktuelle Spracheinstellung
	 */
	public Locale getLanguage() {
		return language;
	}

	/**
	 * Gibt den Text aus der Propertiesdatei zurueck
	 *
	 * @param key Schluessel des Textes
	 * @return Text zum Schluessel
	 */
	public String getText(String key) {
		return propertyfile.getProperty(key);
	}

	public void setAvatar(URL value) {
		userpropertyFile.put("user.avatar", new File(value.getFile()).getName());
		saveUserProperties();
	}

	/**
	 * Liest eine beliebige Property aus den Userproperties
	 * @param strProperty
	 * @return
	 * @throws Exception
	 * @author Jean
	 */
	public String getUserProperty(String strProperty) throws Exception
	{
		String strResult=null;
		try
		{
			 strResult = userpropertyFile.getProperty(strProperty);
		}
		catch(Exception e)
		{
			throw new Exception("Nicht gefunden:"+ strProperty);
			
		}
		if(strResult==null)
			throw new Exception("Nicht gefunden:"+ strProperty);
		//setAvatar(this.getAvatarURL("spielinfo_m1_preview.jpg"));
		return strResult;
	}
	public  void setUserProperty(String strKey, String strValue) throws Exception
	{

		if(strKey==null | strValue==null | strKey.isEmpty() | strValue.isEmpty())
			throw new Exception("Nicht gefunden");
		instance.userpropertyFile.put(strKey,strValue);
		instance.saveUserProperties();
	}

	public String getUserAvatar() {
		try {
			return this.getUserProperty("user.avatar");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setAvatar(getAvatarURL("spielinfo_m1.jpg"));
		}
		return null;
	}

	/**
	 * Gibt die Properties-Datei(Standard: Deutsch) im Package
	 * src/main/resources/infrastructure/constants zurueck
	 *
	 * @return InputStream der Properties-Datei
	 */
	private InputStream getProperties() {

		return getClass().getResourceAsStream("text.properties");
	}

	/**
	 * Gibt die Properties-Datei zu einer bestimmten Sprache im Package
	 * src/main/resources/infrastructure/constants zurueck - (Standard: Deutsch)
	 *
	 * @param locale Sprache/Land
	 * @return Propertiesdatei als InputStream
	 */
	private InputStream getProperties(Locale locale) {
		if (locale.getLanguage().equals(Locale.GERMAN.getLanguage())) {
			return getClass().getResourceAsStream("text.properties");
		}
		if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
			return getClass().getResourceAsStream("text_en.properties");
		}
		// Default Deutsch
		return getClass().getResourceAsStream("text.properties");
	}

	/**
	 * Gibt die Userspezifischen Properties zurueck
	 *
	 * @return Userspezifische Properties
	 */
	private InputStream getUserProperties() {
		InputStream propertiesFile = null;
		try {
			propertiesFile = new FileInputStream
					(new File(GlobaleKonstanten.DEFAULT_CONF_SAVE_DIR, "user.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return propertiesFile;
	}

	/**
	 * Gibt die URL einer Grafik im Package
	 * src/main/resources/infrastructure/graphics zurueck
	 *
	 * @param graphicName Dateiname
	 * @return URL der Grafik
	 */
	public URL getGraphicURL(String graphicName) {
		return this.getClass().getResource("graphics/" + graphicName);
	}

	/**
	 * Gibt die URL einer Avatar-Grafik im Package
	 * src/main/resources/infrastructure/graphics/avatar zurueck
	 *
	 * @param avatarName Dateiname
	 * @return URL des Avatars
	 */
	public URL getAvatarURL(String avatarName) {
		return this.getClass().getResource("graphics/avatar/" + avatarName);
	}

	public ImageIcon getAvatarImage()
	{
		return getAvatarPreviewImageIcon(getUserAvatar());
	}
	/**
	 * Gibt das ImageIcon eines Avatar-Grafik-Previews im Package
	 * src/main/resources/infrastructure/graphics/avatar zurueck
	 *
	 * @param avatarName Dateiname
	 * @return ImageIcon des AvatarPreviews
	 */
	public ImageIcon getAvatarPreviewImageIcon(String avatarName) {
		avatarName = avatarName.replace(".", "_preview.");
		return new ImageIcon(this.getClass().getResource("graphics/avatar/" + avatarName));
	}

	/**
	 * Gibt die URL eines Icons im Package
	 * src/main/resources/infrastructure/graphics/icons zurueck
	 *
	 * @param iconName Dateiname
	 * @return URL des Icons
	 */
	public URL getIconURL(String iconName) {
		return getClass().getResource("graphics/icons/" + iconName);
	}
	public String getIconPath(String iconName)
	{
		return getIconURL(iconName).getPath();
	}
	public Font getMenuFont()
	{
		Font lcd = null;
		InputStream myStream=null;

		try {
			GraphicUtils oUtils = new GraphicUtils();
		myStream = new BufferedInputStream(oUtils.getClass().getResourceAsStream("orbitron-medium.otf"));
//			myStream = new BufferedInputStream(oUtils.getClass().getResourceAsStream("BD_Cartoon_Shout.ttf"));
			lcd = Font.createFont(Font.TRUETYPE_FONT, myStream);
			lcd = lcd.deriveFont(Font.PLAIN, 22);

			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(lcd);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("CODE2000 not loaded.");
		}
		finally {
			try {
				myStream.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block

			}
		}
		if(lcd==null)
			lcd = new Font("Impact", java.awt.Font.PLAIN, 22);
		GlobaleKonstanten.MENUFONT=lcd;
		return lcd;
	}
	public Font getGameFont()
	{
		Font lcd = null;
		InputStream myStream=null;

		try {
			GraphicUtils oUtils = new GraphicUtils();
			myStream = new BufferedInputStream(oUtils.getClass().getResourceAsStream("orbitron-medium.otf"));
			lcd = Font.createFont(Font.TRUETYPE_FONT, myStream);
			lcd = lcd.deriveFont(Font.PLAIN, 22);

			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(lcd);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("CODE2000 not loaded.");
		}
		finally {
			try {
				myStream.close();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block

			}
		}
		if(lcd==null)
			lcd = new Font("Impact", java.awt.Font.PLAIN, 22);
		GlobaleKonstanten.GAMEFONT=lcd;
		return lcd;
	}

	/**
	 * Gibt das ImageIcon anhand des iconNames aus dem Package
	 * src/main/resources/infrastructure/graphics/icons zurueck
	 *
	 * @param iconName Dateiname
	 * @return Icon als ImageIcon
	 */
	public ImageIcon getImageIcon(String iconName) {
		try {
		return new ImageIcon(getClass().getResource(
				"graphics/icons/" + iconName));
		}catch (Exception e) {
			System.out.println("Fehler beim laden von graphics//icons "+iconName);
			return null;
			}
	}

	/**
	 * Gibt das BufferedImage anhand des iconnamens aus dem Package
	 * src/main/resources/infrastructure/graphics/icons zurueck
	 *
	 * @param imageName Dateiname
	 * @return Icon Bild als BufferedImage
	 * @throws IOException Wenn die Datei nicht gefunden wird.
	 */
	public BufferedImage getIconAsBufferedImage(String imageName)
			 {
		try {
			return ImageIO.read(getClass().getResource(
					"graphics/icons/" + imageName));
		} catch (IOException e) {
		System.out.println("Fehler beim laden von graphics//icons "+imageName);
		return null;
		}
	}

	public InputStream getSoundstream(String strTitle)
	{
		return this.getClass().getResourceAsStream("sound/"+strTitle);
	}
	/**
	 * Gibt die URL eines Icons im Package
	 * src/main/resources/infrastructure/graphics/icons zurueck
	 *
	 * @param iconName Dateiname
	 * @return URL des Icons
	 */
	@Deprecated
	public URL getIcon(String iconName) {
		return getClass().getResource("graphics/icons/" + iconName);
	}

	/**
	 * Gibt die URL einer Grafik im Package
	 * src/main/resources/infrastructure/graphics zurueck
	 *
	 * @param graphicName Dateiname
	 * @return URL der Grafik
	 */
	@Deprecated
	public URL getGraphic(String graphicName) {
		return this.getClass().getResource("graphics/" + graphicName);
	}
	public URL getAnimation(String graphicName) {
		return this.getClass().getResource("animation/" + graphicName);
	}

}
