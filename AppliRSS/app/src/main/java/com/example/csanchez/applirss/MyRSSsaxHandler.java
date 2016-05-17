package com.example.csanchez.applirss;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xml.sax.Attributes;
import java.lang.String;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.LocatorImpl;

/**
 * Created by csanchez on 21/09/15.
 */
public class MyRSSsaxHandler extends DefaultHandler {
    private String url = null;    // l'URL du flux RSS à parser
    // Ensemble de drapeau permettant de savoir où en est le parseur dans le flux XML
    private boolean inTitle = false;
    private boolean inDescription = false;
    private boolean inItem = false;
    private boolean inDate = false;
    // L'image référencé par l'attribut url du tag <enclosure>
    private Bitmap image = null;
    private String imageURL = null;
    // Le titre, la description et la date extraits du flux RSS
    private StringBuffer title = new StringBuffer();
    private String description = new String();
    private StringBuffer date = new StringBuffer();
    private int numItem = 0;    // Le numéro de l'item à extraire du flux RSS
    private int numItemMax = -1;    // Le nombre total d’items dans le flux RSS
    private int i = 0;


    //Fixe l'URL à parser
    public void setUrl(String url) {
        this.url = url;
    }

    public void processFeed() {
        try {
            //numItem = 10;  // A modifier pour visualiser un autre item
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
            try {
                image = getBitmap(imageURL);
            } catch (Exception e) {
                Log.e("Bitmap", "Probleme " + e.getMessage());
            }
        } catch(Exception e) {
            Log.e("smb116rssview", "processFeed Exception " + e.getMessage());
        }
    }


    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("item") && i < numItem) i++;
        else if (qName.equals("item") && i == numItem) {
                inItem = true;
        } else if (qName.equals("title") && inItem) {
                inTitle = true;
        } else if (qName.equals(("description")) && inItem) {
                inDescription = true;
        } else if (qName.equals("pubDate") && inItem) {
                inDate = true;
        } else if (qName.equals("enclosure") && inItem) {
                imageURL = attributes.getValue(0);
        }

        else {}
    }

    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);
        if (inTitle) { title = new StringBuffer(chars); Log.v("Titre", "Oui " + chars); }
        else if (inDate) { date = new StringBuffer(chars); Log.v("Date", "Oui " + chars); }
        else if (inDescription) { description = description + chars; Log.v("Desc", "Oui " + chars); }
        else {}
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("item")) {
            if (inItem) {
                i++;
            }
            inItem = false;
            numItemMax++;
        }
        else if (qName.equals("title")) {
            inTitle = false;
        }
        else if (qName.equals(("description"))) {
            inDescription = false;
        }
        else if (qName.equals("pubDate")) {
            inDate = false;
        }
        else {}
    }

    private Bitmap getBitmap(String url) {
        try {
            URL url1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getTitle() { return title.toString(); }

    public String getDescription() { return description.toString(); }

    public String getDate() { return date.toString(); }

    public Bitmap getImage() { return image; }

    public int getNumber() { return numItem; }

    public void setNumber(int numero) { numItem = numero; }

    public void setNumItemMax(int numero) { numItemMax = numero; }

    public int getNumItemMax() { return numItemMax; }
}