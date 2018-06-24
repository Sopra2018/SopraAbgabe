package de.hohenheim.soprademo.security;

import de.hohenheim.soprademo.model.User;
import de.hohenheim.soprademo.service.UserService;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Versendet Logout-Request an die ILIAS SOAP-Schnittstelle und loggt den User aus der eigenen Applikation aus.
 *
 * Doku:
 * https://www.ilias.de/test52/webservice/soap/server.php
 */
@Component
public class IliasLogoutSuccessHandler implements LogoutSuccessHandler {

  public static final String URL_TO_ILIAS_INTERFACE= "https://ilias.uni-hohenheim.de:443/webservice/soap/server.php";

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private UserService userService;

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    //variable to check if logout in ilias was successful. Unclear if needed in further implementation
    String success;

    //get the user
    User user = userService.getUserByUsername(authentication.getName());

    //if it is a ilias user
    if (Objects.nonNull(user.getIliasSessionId())) {
      //Log out of Ilias - SoapRequest
      SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
      SoapObject soapObject = new SoapObject("urn:ilUserAdministration", "logout");
      //adds the sessionID to the soap Request
      soapObject.addProperty("sid", user.getIliasSessionId());
      envelope.setOutputSoapObject(soapObject);

      //get transport ready
      HttpTransportSE transportSE = new HttpTransportSE(URL_TO_ILIAS_INTERFACE);
      transportSE.debug = true;

      logger.info("sending logout request");

      try {
        transportSE.call("urn:ilUserAdministration#logout", envelope);
        success = envelope.getResponse().toString();

        logger.info("Ilias antwortete mit: " + success);
        user.setIliasSessionId(null);
        userService.saveUser(user);

      } catch (IOException e) {
        logger.info("IOException, connection established ?");
        e.printStackTrace();
      } catch (XmlPullParserException e) {
        logger.info("parsing Error, is there a response at all ?");
        e.printStackTrace();
      }
    }


    // redirect zur Login-Seite und setze Status auf OK
    response.setStatus(HttpServletResponse.SC_OK);

    response.sendRedirect("/login?logout");
  }
}
