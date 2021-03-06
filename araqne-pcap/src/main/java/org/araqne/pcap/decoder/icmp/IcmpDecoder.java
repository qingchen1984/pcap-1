/*
 * Copyright 2010 NCHOVY
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.araqne.pcap.decoder.icmp;

import java.util.HashSet;
import java.util.Set;

import org.araqne.pcap.decoder.ip.Ipv4Packet;
import org.araqne.pcap.decoder.ip.IpProcessor;
import org.araqne.pcap.util.Buffer;

/**
 * Decodes ICMP packet
 * 
 * @author xeraph
 */
public class IcmpDecoder implements IpProcessor {
	private Set<IcmpProcessor> callbacks = new HashSet<IcmpProcessor>();

	public void register(IcmpProcessor callback) {
		callbacks.add(callback);
	}

	public void unregister(IcmpProcessor callback) {
		callbacks.remove(callback);
	}

	@Override
	public void process(Ipv4Packet packet) {
		IcmpPacket p = new IcmpPacket();
		Buffer b = packet.getData();

		p.setIpPacket(packet);
		p.setSource(packet.getSourceAddress());
		p.setDestination(packet.getDestinationAddress());
		p.setType(b.get() & 0xff);
		p.setCode(b.get() & 0xff);
		p.setChecksum(b.getUnsignedShort());
		p.setId(b.getUnsignedShort());
		p.setSeq(b.getUnsignedShort());

		b.discardReadBytes();
		p.setData(b);

		/* manipulate icmp packet from outside */
		for (IcmpProcessor callback : callbacks) {
			try {
				callback.process(new IcmpPacket(p));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
