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
        String mac = runArp(ip);
        if (mac == null)
        	mac = runIpConfig(ip);
        return mac;
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
    
    public String runIpConfig(String ip) throws IOException {
//    	System.out.println("ip: " + ip);
    	String mac = null;
    	String t = null;
    	String param = "ipconfig /all";
        Process p = Runtime.getRuntime().exec(param.split("\\s"));
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
        	line = line.trim();
        	if (line == null || line.length() <= 0)
        		continue;
        	
    		if (line.startsWith("Physical Address") == true) {
    			t = extractMacAddrFromIpconfig(line);
    		}
    		
    		if (line.startsWith("IPv4 Address") == true) {
    			if (line.contains(ip) == true) {
    				mac = t;
    				break;
    			}
    				
    		}
        }
        return mac;
    }
    
    public String runIpAddr(String ip) throws IOException {
//    	System.out.println("ip: " + ip);
    	String mac = null;
    	String t = null;
    	String param = "ip addr";
        Process p = Runtime.getRuntime().exec(param.split("\\s"));
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
        	line = line.trim();
        	if (line == null || line.length() <= 0)
        		continue;
        	
    		if (line.startsWith("link/ether") == true) {
    			t = extractMacAddrFromIfconfig(line);
    		}
    		
    		if (line.startsWith("inet ") == true) {
    			if (line.contains(ip) == true) {
    				mac = t;
    				break;
    			}
    				
    		}
        }
        return mac;
    }
    
    private String extractMacAddrFromIfconfig(String str) {
    	String mac = null;
    	
    	String[] arr = str.split("\\s+");
    	
    	mac = arr[1];
    	
    	return mac;
    }
    
    private String extractMacAddrFromIpconfig(String str) {
    	String mac = null;
    	
    	String[] arr = str.split(":");
    	
    	mac = arr[1];
    	
    	return mac;
    }

    private String extractMacAddr(String str) {
    	String mac = null;
    	
    	String[] arr = str.split("\\s+");
    	
    	if (tg.osType == 1) {
    		mac = arr[1];
    	} else {
    		mac = arr[3];
    	}
        return mac;
    }
}
