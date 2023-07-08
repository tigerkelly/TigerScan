# TigerScan

An IP scanner using JavaFX.

This IP Scanner GUI is a little faster than most scanners because it uses N threads to scan addresses on a subnet instead of just one.

![TigerScan image](src/images/tigerscan.png?raw=true)

Program features:

- Find all interfaces on you system automatically.
- Finds all subnets on each interface automatically.
- Scans the subnet using multiple threads.  The number of thread is defined the number of cores, but can be changed.
- Can scan a single address just by clicking on it.
- A list of MAC vendor IDs defined by the user.
- If a user's MAC ID is found it changes the label to a yellow text with a green background and if you hover your mouse over label it gives you the vendor name you defined.
	* This allows me to find all my Raspberry Pi systems quickly.
- Can scan more than one subnet at a time.
- Can run on both windows and linux.

![Scanning image](src/images/tigerscan_scanning.png?raw=true)

The install programs are too large for github, so if you want the install for windows or linux x64 email me at rkwiles@twc.com
I will send you a link.