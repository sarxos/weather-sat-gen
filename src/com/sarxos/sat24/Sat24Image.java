package com.sarxos.sat24;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;


public class Sat24Image {

	// country pl, eu, de ...
	// time 201007171045
	private static final String SAT24_URL_VIS = "http://www.sat24.com/image.ashx?country=%s&type=&time=%s&index=3&sat=vis";
	private static final String SAT24_URL_IR = "http://www.sat24.com/image.ashx?country=%s&type=&time=%s&index=3&sat=ir";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmm");

	private static Proxy proxy = null;

	private String country = null;
	private String timestr = null;
	private Date date = null;
	private URL url = null;
	private BufferedImage image = null;
	private boolean initialized = false;


	/**
	 * Create Sat24 image.
	 * 
	 * @param date - must be in last 24 hours
	 * @param country - pl, eu, de
	 */
	public Sat24Image(Date date, String country) {

		this.date = date;
		this.country = country;
		this.timestr = DATE_FORMAT.format(date);
		
		Proxy proxy = null;
		try {
			this.url = new URL(String.format(SAT24_URL_IR, country, timestr));

			URLConnection connection = null;
			if ((proxy = getProxy()) != null) {
				connection = url.openConnection(proxy);
			} else {
				connection = url.openConnection();
			}

			InputStream is = connection.getInputStream();
			this.image = ImageIO.read(is);
			this.initialized = true;
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return proxy if it is used. In other case return null.
	 * 
	 * @return Proxy
	 */
	protected Proxy getProxy() {
		
		boolean enableProxy = "true".equals(System.getProperty("proxySet"));
		if (enableProxy) {
			if (proxy == null) {
				String host = System.getProperty("proxyHost");
				int port = Integer.parseInt(System.getProperty("proxyPort"));
				SocketAddress address = new InetSocketAddress(host, port);
				proxy = new Proxy(Proxy.Type.HTTP, address);
			}
			return proxy;
		}
		return null;
	}
	
	public String getCountry() {
		return country;
	}

	public String getTimestr() {
		return timestr;
	}

	public Date getDate() {
		return date;
	}

	public URL getURL() {
		return url;
	}

	public BufferedImage getImage() {
		return image;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void flush() {
		image.flush();
	}


	/**
	 * Only for test purpose.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2010);
		c.set(Calendar.MONTH, Calendar.SEPTEMBER);
		c.set(Calendar.DAY_OF_MONTH, 6);
		c.set(Calendar.HOUR_OF_DAY, 13);
		c.set(Calendar.MINUTE, 30);

		Sat24Image img = new Sat24Image(c.getTime(), "eu");

		try {
			ImageIO.write(img.getImage(), "png", new File(img.getTimestr() + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
