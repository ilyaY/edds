package runetsoft;

import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TestClient {
  public static void main(String[] args) throws Exception {
    String[] messages = {
        "#L#2.0;355217044956192;NA;E335\r\n",
        "#B#231117;042345;5730.9064;N;05306.8488;E;0;239;192;18;NA;NA;NA;;NA;tamper_1:1:1|231117;042346;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:0|231117;042540;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:1|231117;042543;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:0|231117;042545;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:1|231117;042547;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042550;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:1|231117;042552;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042553;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:1|231117;042554;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042555;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:1|231117;042556;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042605;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:1|231117;042606;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042612;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:1|231117;042614;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042627;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:1|231117;042628;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:0|231117;042636;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;uptime:1:682048,mess_count_1:1:1,mess_count_2:1:2,mess_count_3:1:18,hdop:2:0.6100,gps_inview:1:14,glonass_inview:1:10,gps_odometer:2:17792.6504,trip_counter:1:425,ign:1:0,int_temp:2:23.2196,ext_voltage:2:12.8050,acc_voltage:2:4.0857,motohours:2:345.4134,gsm_sig_level:1:22,eng_uptime:1:0,eng_uptime_p:1:20797,can_odo_km:2:0.0000,can_odo_p:2:17299.0801,cons_fuel_l:2:0.0000,cons_fuel_p:2:2184.9001,fuel_lev_p:1:0,fuel_lev_l:1:0,eng_rpm:1:0,eng_temp:2:75.0000,can_speed:1:0,accelerator:1:0|231117;042643;5730.9064;N;05306.8488;E;0;239;192;20;NA;NA;NA;;NA;tamper_1:1:1|231117;042647;5730.9064;N;05306.8488;E;0;239;192;19;NA;NA;NA;;NA;tamper_1:1:0|9F55\r\n",
        "#B#231117;214323;5730.9016;N;05306.8536;E;0;174;182;18;NA;NA;NA;;NA;tamper_1:1:0|B16A\r\n",
        "#P#\r\n"
    };

    Socket client = new Socket("192.168.0.54", 8089);
    for (String msg : messages) {
      client.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
      client.getOutputStream().flush();
      Thread.sleep(1000);
    }
    client.close();
  }
}
