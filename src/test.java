import org.libvirt.*;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Create the connection
		VirConnect conn=null;
		VirNetwork testNetwork=null;
		
		//Need this for the lookup method testing, it's absolutely horrible in java, but let's be complete
		int UUID[] = {Integer.decode("0x00"), Integer.decode("0x4b"), Integer.decode("0x96"), Integer.decode("0xe1"), 
				Integer.decode("0x2d"), Integer.decode("0x78"),
				Integer.decode("0xc3"), Integer.decode("0x0f"),
				Integer.decode("0x5a"), Integer.decode("0xa5"),
				Integer.decode("0xf0"), Integer.decode("0x3c"), Integer.decode("0x87"), Integer.decode("0xd2"), Integer.decode("0x1e"), Integer.decode("0x69")} ;
		
		try{
			conn = new VirConnect("test:///default", false);
		} catch (LibvirtException e){
			System.out.println("exception caught:"+e);
			System.out.println(e.getVirError());
		}
		try{	
			//Check nodeinfo
			VirNodeInfo nodeInfo=conn.virNodeInfo();
			System.out.println("virNodeInfo.model:" + nodeInfo.model);
			System.out.println("virNodeInfo.memory:" + nodeInfo.memory);
			System.out.println("virNodeInfo.cpus:" + nodeInfo.cpus);
			System.out.println("virNodeInfo.nodes:" + nodeInfo.nodes);
			System.out.println("virNodeInfo.sockets:" + nodeInfo.sockets);
			System.out.println("virNodeInfo.cores:" + nodeInfo.cores);
			System.out.println("virNodeInfo.threads:" + nodeInfo.threads);
			
			//Exercise the information getter methods
			System.out.println("getHostName:" + conn.getHostName());
			System.out.println("getCapabilities:" + conn.getCapabilities());
			System.out.println("getMaxVcpus:" + conn.getMaxVcpus("xen"));
			System.out.println("getType:" + conn.getType());
			System.out.println("getURI:" + conn.getURI());
			System.out.println("getVersion:" + conn.getVersion());
			
			//By default, there are 1 created and 0 defined networks
			
			//Create a new network to test the create method
			System.out.println("conn.virNetworkCreateXML:"+conn.virNetworkCreateXML("<network>" +
					"  <name>createst</name>"+
					"  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e68</uuid>"+
					"  <bridge name='createst'/>"+
					"  <forward dev='eth0'/>"+
					"  <ip address='192.168.66.1' netmask='255.255.255.0'>"+
					"    <dhcp>"+
					"      <range start='192.168.66.128' end='192.168.66.253'/>"+
					"    </dhcp>"+
					"  </ip>"+
					"</network>"));
					
			//Same for the define method
			System.out.println("conn.virNetworkDefineXML:"+conn.virNetworkDefineXML("<network>" +
					"  <name>deftest</name>"+
					"  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e67</uuid>"+
					"  <bridge name='deftest'/>"+
					"  <forward dev='eth0'/>"+
					"  <ip address='192.168.88.1' netmask='255.255.255.0'>"+
					"    <dhcp>"+
					"      <range start='192.168.88.128' end='192.168.88.253'/>"+
					"    </dhcp>"+
					"  </ip>"+
					"</network>"));
			
			//We should have 2:1 but it shows up 3:0 hopefully a bug in the test driver
			System.out.println("numOfDefinedNetworks:" + conn.numOfDefinedNetworks());
			System.out.println("listDefinedNetworks:" + conn.listDefinedNetworks());
			for(String c: conn.listDefinedNetworks())
				System.out.println("	"+c);
			System.out.println("numOfNetworks:" + conn.numOfNetworks());
			System.out.println("listNetworks:" + conn.listNetworks());
			for(String c: conn.listNetworks())
				System.out.println("	"+c);
	
			//Define a new Domain
			System.out.println("conn.virDomainDefineXML:"+conn.virDomainDefineXML("<domain type='test' id='2'>"+
					"  <name>deftest</name>"+
					"  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e70</uuid>"+
					"  <memory>8388608</memory>"+
					"  <vcpu>2</vcpu>"+
					"  <on_reboot>restart</on_reboot>"+
					"  <on_poweroff>destroy</on_poweroff>"+
					"  <on_crash>restart</on_crash>"+
					"</domain>"));
			
			System.out.println("conn.virDomainCreateLinux:"+conn.virDomainCreateLinux("<domain type='test' id='3'>"+
					"  <name>createst</name>"+
					"  <uuid>004b96e1-2d78-c30f-5aa5-f03c87d21e71</uuid>"+
					"  <memory>8388608</memory>"+
					"  <vcpu>2</vcpu>"+
					"  <on_reboot>restart</on_reboot>"+
					"  <on_poweroff>destroy</on_poweroff>"+
					"  <on_crash>restart</on_crash>"+
					"</domain>",0));
			
			//Domain enumeration stuff
			System.out.println("numOfDefinedDomains:" + conn.numOfDefinedDomains());
			System.out.println("listDefinedDomains:" + conn.listDefinedDomains());
			for(String c: conn.listDefinedDomains())
				System.out.println("	"+c);
			System.out.println("numOfDomains:" + conn.numOfDomains());
			System.out.println("listDomains:" + conn.listDomains());
			for(int c: conn.listDomains())
				System.out.println("	"+c);

			
		} catch (LibvirtException e){
			System.out.println("exception caught:"+e);
			System.out.println(e.getVirError());
		}
		
		//Network Object
		
		try{	
			//Choose one, they should have the exact same effect
			//VirNetwork testNetwork=conn.virNetworkLookupByName("default");
			//VirNetwork testNetwork=conn.virNetworkLookupByUUIDString("004b96e1-2d78-c30f-5aa5-f03c87d21e69");
			System.out.println("about to call virNetworkLookupByUUID");
			testNetwork=conn.virNetworkLookupByUUID(UUID);
			
			//Exercise the getter methods on the default network
			System.out.println("virNetworkGetXMLDesc:" + testNetwork.getXMLDesc(0));
			System.out.println("virNetworkLookupByName:" + testNetwork);
			System.out.println("virNetworkGetAutostart:" + testNetwork.getAutostart());
			System.out.println("virNetworkGetBridgeName:" + testNetwork.getBridgeName());
			System.out.println("virNetworkGetName:" + testNetwork.getName());
			System.out.println("virNetworkGetUUID:" + testNetwork.getUUID() + " ");
			for(int c: testNetwork.getUUID())
				System.out.print(Integer.toHexString(c));
			System.out.println();
			System.out.println("virNetworkGetName:" + testNetwork.getUUIDString());
			
			//Destroy and create the network
			System.out.println("virNetworkDestroy:"); testNetwork.destroy();
			System.out.println("virNetworkCreate:"); testNetwork.create();
		} catch (LibvirtException e){
			System.out.println("exception caught:"+e);
			System.out.println(e.getVirError());
		}
		//This should raise an excpetion
		try{
			System.out.println("virNetworkCreate:");  testNetwork.create();
		} catch (LibvirtException e){
			System.out.println("exception caught:"+e);
			System.out.println(e.getVirError());
		}
		
		//Domain stuff
		
		try{

			
			//Domain lookup
			//VirDomain testDomain=conn.virDomainLookupByID(1);
			//VirDomain testDomain=conn.virDomainLookupByName("test");
			//VirDomain testDomain=conn.virDomainLookupByUUIDString("004b96e1-2d78-c30f-5aa5-f03c87d21e69");
			VirDomain testDomain=conn.virDomainLookupByUUID(UUID);
			
			//Exercise the getter methods on the default domain
			System.out.println("virDomainGetXMLDesc:" + testDomain.getXMLDesc(0));
			System.out.println("virDomainGetAutostart:" + testDomain.getAutostart());
			System.out.println("virDomainGetConnect:" + testDomain.getConnect());
			System.out.println("virDomainGetIDt:" + testDomain.getID());
			System.out.println("virDomainGetInfo:" + testDomain.getInfo());
			System.out.println("virDomainGetMaxMemory:" + testDomain.getMaxMemory());
			//Should fail, test driver does not support it
			//System.out.println("virDomainGetMaxVcpus:" + testDomain.getMaxVcpus());
			System.out.println("virDomainGetName:" + testDomain.getName());
			System.out.println("virDomainGetOSType:" + testDomain.getOSType());
			System.out.println("virDomainGetSchedulerType:" + testDomain.getSchedulerType());
			System.out.println("virDomainGetSchedulerParameters:" + testDomain.getSchedulerParameters());
			//Iterate over the parameters the painful way
			for(VirSchedParameter c: testDomain.getSchedulerParameters()){
				if (c instanceof VirSchedIntParameter)
					System.out.println("Int:" + ((VirSchedIntParameter)c).field +":"+ ((VirSchedIntParameter)c).value);
				if (c instanceof VirSchedUintParameter)
					System.out.println("Uint:" + ((VirSchedUintParameter)c).field  +":"+  ((VirSchedUintParameter)c).value);
				if (c instanceof VirSchedLongParameter)
					System.out.println("Long:" + ((VirSchedLongParameter)c).field  +":"+  ((VirSchedLongParameter)c).value);
				if (c instanceof VirSchedUlongParameter)
					System.out.println("Ulong:" + ((VirSchedUlongParameter)c).field  +":"+  ((VirSchedUlongParameter)c).value);
				if (c instanceof VirSchedDoubleParameter)
					System.out.println("Double:" + ((VirSchedDoubleParameter)c).field  +":"+  ((VirSchedDoubleParameter)c).value);
				if (c instanceof VirSchedBooleanParameter)
					System.out.println("Boolean:" + ((VirSchedBooleanParameter)c).field  +":"+  ((VirSchedBooleanParameter)c).value);
			}
			//Iterate over the parameters the easy way
			for(VirSchedParameter c: testDomain.getSchedulerParameters()){
				System.out.println(c.getTypeAsString() +":"+ c.field +":"+  c.getValueAsString());
			}
			System.out.println("virDomainGetUUID:" + testDomain.getUUID());
			for(int c: testDomain.getUUID())
				System.out.print(Integer.toHexString(c));
			System.out.println();
			System.out.println("virDomainGetUUIDString:" + testDomain.getUUIDString());
			//Should fail, unimplemented in test driver		
			//System.out.println("virDomainGetVcpusInfo:" + testDomain.getVcpusInfo());
			//Same as above
			//System.out.println("virDomainGetVcpusCpuMap:" + testDomain.getVcpusCpuMaps());
			//Should test pinVcpu, when we test with real xen
			//Here
			//Attach default network to test domain
			//System.out.println("virDomainGetVcpusCpuMap:" + testDomain.getVcpusCpuMaps());
			
			//Should test interfacestats and blockstats with real xen
			
			//Close the connection
			
			conn.close();
		} catch (LibvirtException e){
			System.out.println("exception caught:"+e);
			System.out.println(e.getVirError());
		}
		
		
		
		try{
			//We should get an exception, not a crash
			System.out.println(conn.getHostName());
		}catch (LibvirtException e){
			System.out.println("exception caught:"+e);
			System.out.println(e.getVirError());
		}
		System.out.println();
	}

}