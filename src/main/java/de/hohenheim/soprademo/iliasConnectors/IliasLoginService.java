package de.hohenheim.soprademo.iliasConnectors;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Sendet einen SOAP-Request an die Hohenheimer ILIAS SOAP-Schnittstelle zur Authentifizierung eines Users.
 * War die Authentifizierung erfolgreich, wird eine Session-ID von ILIAS zurückgegeben.
 *
 * Doku:
 * https://www.ilias.de/test52/webservice/soap/server.php
 */
@Service
public class IliasLoginService {

  public static final String URL_TO_ILIAS_SOAP_INTERFACEE = "https://ilias.uni-hohenheim.de:443/webservice/soap/server.php";
  public static final String NAME_OF_THE_ILIAS_CLIENT = "UHOH";

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Versuche User in ILIAS einzuloggen.
   *
   * @param username username
   * @param password Passwort
   * @return gültige Session-ID, andernfalls null.
   */
  public String login(String username, String password) {
    logger.info("ILIAS Login via SOAP für User: " + username);
    String session = null;

    // send soap login request to ilias and recieve a session ID if successful
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    SoapObject soapObject = new SoapObject("urn:ilUserAdministration", "login");
    soapObject.addProperty("client", NAME_OF_THE_ILIAS_CLIENT);
    soapObject.addProperty("username", username);
    soapObject.addProperty("password", password);
    envelope.setOutputSoapObject(soapObject);
    HttpTransportSE transport = new HttpTransportSE(URL_TO_ILIAS_SOAP_INTERFACEE);
    transport.debug = true;
    try {
      logger.info("Sending login request");
      transport.call("urn:ilUserAdministration#login", envelope);
      session = envelope.getResponse().toString();
      // gib Session-ID zurück
      return session;
    } catch (IOException e) {
      logger.info(e.getMessage() + " -- probably admin login.");
    } catch (XmlPullParserException e) {
      logger.info(e.getMessage());
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return null;
  }
}
