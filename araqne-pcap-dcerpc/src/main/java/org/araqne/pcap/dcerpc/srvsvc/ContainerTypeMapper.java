/**
 * 
 */
package org.araqne.pcap.dcerpc.srvsvc;

import org.araqne.pcap.dcerpc.srvsvc.rr.OpCodes;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.ContainerInterface;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.FileInfo2Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.FileInfo3Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.SessionInfo0Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.SessionInfo10Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.SessionInfo1Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.SessionInfo2Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.SessionInfo502Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.ShareInfo0Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.ShareInfo1Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.ShareInfo2Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.ShareInfo501Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.ShareInfo503Container;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.infos.ConnectionInfo0;
import org.araqne.pcap.dcerpc.srvsvc.structure.containers.infos.ConnectionInfo1;

/**
 * @author tgnice@nchovy.com
 *
 */
public class ContainerTypeMapper {

	public ContainerInterface getStruct(OpCodes type , int level){
		switch(type){
		case NetrConnectionEnum :
			switch(level){
			case 0:
				return (ContainerInterface) new ConnectionInfo0();
			case 1:
				return (ContainerInterface) new ConnectionInfo1();
			default :
				new IllegalAccessException(this+" : invalid Level Type");
				return null;	
			}
		case NetrFileEnum :
			switch(level){
			case 2:
				return (ContainerInterface) new FileInfo2Container();
			case 3:
				return (ContainerInterface) new FileInfo3Container();
			default :
				new IllegalAccessException(this+" : invalid Level Type");
				return null;	
			}
		case NetfileGetinfo :
			return null;
		case NetFileClose :
			return null;
		case NetrSessionEnum :
			switch(level){
			case 0:
				return (ContainerInterface) new SessionInfo0Container();
			case 1:
				return (ContainerInterface) new SessionInfo1Container();
			case 2:
				return (ContainerInterface) new SessionInfo2Container();
			case 10:
				return (ContainerInterface) new SessionInfo10Container();
			case 502:
				return (ContainerInterface) new SessionInfo502Container();
			default :
				new IllegalAccessException(this+" : invalid Level Type");
				return null;	
			}
		case NetrSessionDel :
			return null;
		case NetrShareAdd :
			return null;
		case NetrShareEnum :
			switch(level){
			case 0:
				return (ContainerInterface) new ShareInfo0Container();
			case 1:
				return (ContainerInterface) new ShareInfo1Container();
			case 2:
				return (ContainerInterface) new ShareInfo2Container();
			case 501:
				return (ContainerInterface) new ShareInfo501Container();
			case 503:
				return (ContainerInterface) new ShareInfo503Container();
			default :
				new IllegalAccessException(this+" : invalid Level Type");
				return null;	
			}
		case NetrShareGetInfo :
			return null;
		case NetrShareSetInfo :
			return null;
		case NetrShareDel :
			return null;
		case NetrShareDelSticky :
			return null;
		case NetrShareCheck :
			return null;
		case NetrServerGetInfo :
			return null;
		case NetrServerSetInfo :
			return null;
		case NetServerDiskEnum :
			return null;
		case NetServerStatisticsGet :
			return null;
		case NetrServerTransportAdd :
			return null;
		case NetrServerTransportEnum :
			return null;
		case NetrServerTransportDel :
			return null;
		case NetrRemoteTOD :
			return null;
		case NetprPathType :
			return null;
		case NetprPathCanonicalize :
			return null;
		case NetprPathCompare :
			return null;
		case NetprNameValidate :
			return null;
		case NetprNameCanonicalize :
			return null;
		case NetprNameCompare :
			return null;
		case NetsShareEnumSticky :
			return null;
		case NetrShareDelstart :
			return null;
		case NetrShareDelCommit :
			return null;
		case NetrpGetFileSecurity :
			return null;
		case NetrpSetFileSecurity :
			return null;
		case NetrServerTransportAddEx :
			return null;
		case NetrDfsGetVersion :
			return null;
		case NetrDfsCreateLocalPartition :
			return null;
		case NetrDfsDeleteLocalPartition :
			return null;
		case NetrDfsSetLocalVolumeState :
			return null;
		case NetrDfsCreateExitPoint :
			return null;
		case NetrDfsDeleteExitPoint :
			return null;
		case NetrDfsModifyPrefix :
			return null;
		case NetDfsFixLocalVolume :
			return null;
		case NetrDfsManagerReportSiteInfo :
			return null;
		case NetrServerTransportDelEx :
			return null;
		case NetrServerAliasAdd :
			return null;
		case NetrSErverAliasEnum :
			return null;
		case NetrServerAliasDel :
			return null;
		default :
			new IllegalAccessException(this+" : invalid OpNumber Type");
			return null;
		}
	}
}
