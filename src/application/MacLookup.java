package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class MacLookup {
	
	private TsGlobal tg = TsGlobal.getInstance();
	
	public MacLookup() {
		
	}
	
	public String getMacAddrHost(String host) throws IOException, InterruptedException {
        
        InetAddress address = InetAddress.getByName(host);
        String ip = address.getHostAddress();
        return runArp(ip);
    }

    private String runArp(String ip) throws IOException {
    	String mac = null;
    	String param = "arp -a " + ip;
        Process p = Runtime.getRuntime().exec(param.split("\\s"));
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
        	line = line.trim();
        	if (line == null || line.length() <= 0)
        		continue;
        	
    		if (line.contains(ip) == true) {
    			mac = extractMacAddr(line);
    			break;
    		}
        }
        return mac;
    }

    private String extractMacAddr(String str) {
    	String mac = null;
    	
//    	System.out.println(str);
    	
    	String[] arr = str.split("\\s+");
    	
//    	for (String s : arr)
//    		System.out.println(s);
    	
    	if (tg.osType == 1) {
    		mac = arr[1];
    	} else {
    		mac = arr[3];
    	}
        return mac;
    }
}
