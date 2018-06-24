package de.hohenheim.soprademo.iliasConnectors;

import de.hohenheim.soprademo.model.User;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

@Service
public class IliasUserService {

  public static final String URL_TO_ILIAS_SOAP_INTERFACEE = "https://ilias.uni-hohenheim.de:443/webservice/soap/server.php";

  private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Gibt die User_Id im ILIAS-System für einen Benutzer zurück.
   *
   * @param user User-Objekt.
   * @return ILIAS user_id.
   */
  public int getCurrentUserIdBySid (User user){
    int user_id;
    // get ilias user_id
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    SoapObject soapObject = new SoapObject("urn:ilUserAdministration", "getUserIdBySid");
    soapObject.addProperty("sid", user.getIliasSessionId());
    envelope.setOutputSoapObject(soapObject);
    HttpTransportSE transport = new HttpTransportSE(URL_TO_ILIAS_SOAP_INTERFACEE);
    transport.debug = true;
    try {
      logger.info("Sending getUserIdBySid request");
      transport.call("urn:ilUserAdministration#getUserIdBySid", envelope);
      user_id = (int) envelope.getResponse();
      logger.info("User_ID erhalten");
      return user_id;
    } catch (IOException e) {
      logger.info(e.getMessage() + " -- probably admin login.");
    } catch (XmlPullParserException e) {
      logger.info(e.getMessage());
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return 0;
  }
}
