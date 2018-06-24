package de.hohenheim.soprademo.iliasConnectors;

import de.hohenheim.soprademo.model.User;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

@Service
public class IliasCourseService {

  public static final String URL_TO_ILIAS_SOAP_INTERFACEE = "https://ilias.uni-hohenheim.de:443/webservice/soap/server.php";
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * Sucht nach allen Kursen, in denen der User Mitglied ist.
   *
   * @param user User-Objekt.
   * @return XML-String, muss geparsed werden!
   */
  public String getCoursesForUser(User user) {
    String courses;
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    // TODO iliasUserId des Users muss über den Call getCurrentUserIdBySid im IliasUserService gesetzt und gespeichert werden
    int userId = user.getIliasUserId();
    SoapObject soapObject = new SoapObject("urn:ilUserAdministration", "getCoursesForUser");
    soapObject.addProperty("sid", user.getIliasSessionId());

    /**
     * Doku (siehe https://ilias.uni-hohenheim.de/webservice/soap/server.php#)
     *
     * $parameters has to contain a column user_id and a column status. Status is a logical AND combined value of
     * (MEMBER = 1, TUTOR = 2, ADMIN = 4, OWNER = 8) and determines which courses should be returned.
     *
     * Nützlicher Link für Aufbau des XML parameters:
     * https://www.ilias.de/docu/goto_docu_frm_1875_2979.html?lang=de
     */
    String paramXml =
        "<result>" +
            "<colspecs>" +
            "<colspec name=\"user_id\" idx=\"0\"></colspec>" +
            "<colspec name=\"status\" idx=\"1\"></colspec>" +
            "</colspecs>" +
            "<rows>" +
            "<row>" +
            "<column>" + userId + "</column>" +
            "<column>" + 7 + "</column>" +
            "</row>" +
            "</rows>" +
            "</result>";

    soapObject.addProperty("parameters", paramXml);
    envelope.setOutputSoapObject(soapObject);
    HttpTransportSE transport = new HttpTransportSE(URL_TO_ILIAS_SOAP_INTERFACEE);
    transport.debug = true;
    try {
      logger.info("Sending getCoursesForUser request");
      transport.call("urn:ilUserAdministration#getCoursesForUser", envelope);
      logger.info("transport Erfolgreich");
      // TODO parse response to Java-objects
      System.out.println(envelope.getResponse().toString());
      logger.info("ILIAS Kurse für " + user.getUsername());
      // gibt courses XML zurück
      return envelope.getResponse().toString();
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
