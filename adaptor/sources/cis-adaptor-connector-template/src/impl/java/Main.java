import java.util.HashMap;

import com.frequentis.cis.connector.core.ConnectorCoreImpl;
import com.frequentis.cis.connector.core.CoreConstants;
import com.frequentis.cis.core.edxlde.parameters.DEParameters;
import com.frequentis.cis.exception.communication.CISCommunicationException;
import com.frequentis.cis.exception.config.CISConfigException;
import com.frequentis.cis.exception.invalid.CISInvalidObjectException;

public class Main {

	public static void main(String[] args) throws CISConfigException, CISInvalidObjectException, CISCommunicationException {
		ConnectorCoreImpl connector = new ConnectorCoreImpl();

		String msg = "<message from='KSTO@NWS.NOAA.GOV'\r\n" + "         to='weatherbot@jabber.org'\r\n"
				+ "         id='KSTO1055887203'>\r\n" + "  <alert xmlns='http://www.incident.com/cap/1.0'>\r\n"
				+ "    <identifier>KSTO1055887203</identifier>\r\n" + "    <sender>KSTO@NWS.NOAA.GOV</sender>\r\n"
				+ "    <sent>2003-06-17T14:57:00-07:00</sent>\r\n" + "    <status>Actual</status>\r\n"
				+ "    <msgType>Alert</msgType>\r\n" + "    <scope>Public</scope>\r\n" + "    <info>\r\n"
				+ "      <category>Met</category>\r\n" + "      <event>SEVERE THUNDERSTORM</event>\r\n"
				+ "      <urgency>Severe</urgency>\r\n" + "      <certainty>Likely</certainty>\r\n"
				+ "      <eventCode>same=SVR</eventCode>\r\n"
				+ "      <senderName>NATIONAL WEATHER SERVICE SACRAMENTO</senderName>\r\n"
				+ "      <headline>SEVERE THUNDERSTORM WARNING</headline>\r\n" + "      <description>\r\n"
				+ "        AT 254 PM PDT... NATIONAL WEATHER SERVICE DOPPLER RADAR\r\n"
				+ "        INDICATED A SEVERE THUNDERSTORM OVER SOUTH CENTRAL ALPINE\r\n"
				+ "        COUNTY... OR ABOUT 18 MILES SOUTHEAST OF KIRKWOOD...\r\n"
				+ "        MOVING SOUTHWEST AT 5 MPH. HAIL... INTENSE RAIN AND STRONG\r\n"
				+ "        DAMAGING WINDS ARE LIKELY WITH THIS STORM\r\n" + "      </description>\r\n"
				+ "      <instruction>\r\n" + "        TAKE COVER IN A SUBSTANTIAL SHELTER UNTIL THE STORM PASSES\r\n"
				+ "      </instruction>\r\n" + "      <contact>BARUFFALDI/JUSKIE</contact>\r\n" + "      <area>\r\n"
				+ "        <areaDesc>\r\n" + "          EXTREME NORTH CENTRAL TUOLUMNE COUNTY IN CALIFORNIA,\r\n"
				+ "          EXTREME NORTHEASTERN CALAVERAS COUNTY IN CALIFORNIA,\r\n"
				+ "          SOUTHWESTERN ALPINE COUNTY IN CALIFORNIA\r\n" + "        </areaDesc>\r\n"
				+ "        <polygon>\r\n" + "          38.47,-120.14 38.34,-119.95 38.52,-119.74\r\n"
				+ "          38.62,-119.89 38.47,-120.14\r\n" + "        </polygon>\r\n"
				+ "        <geocode>fips6=006109</geocode>\r\n" + "        <geocode>fips6=006109</geocode>\r\n"
				+ "        <geocode>fips6=006103</geocode>\r\n" + "      </area>\r\n" + "    </info>\r\n"
				+ "  </alert>\r\n" + "</message>";

		connector.notify(CoreConstants.MSGTYPE_CAP, msg, new HashMap<DEParameters, String>(), true);
	}

}
