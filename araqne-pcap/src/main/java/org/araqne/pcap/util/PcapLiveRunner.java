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
package org.araqne.pcap.util;

import java.io.IOException;
import java.util.List;

import org.araqne.pcap.Protocol;
import org.araqne.pcap.decoder.arp.ArpDecoder;
import org.araqne.pcap.decoder.ethernet.EthernetDecoder;
import org.araqne.pcap.decoder.ethernet.EthernetType;
import org.araqne.pcap.decoder.ethernet.MacAddress;
import org.araqne.pcap.decoder.icmp.IcmpDecoder;
import org.araqne.pcap.decoder.icmpv6.Icmpv6Decoder;
import org.araqne.pcap.decoder.icmpv6.Icmpv6Processor;
import org.araqne.pcap.decoder.ip.InternetProtocol;
import org.araqne.pcap.decoder.ip.IpDecoder;
import org.araqne.pcap.decoder.ipv6.Ipv6Decoder;
import org.araqne.pcap.decoder.tcp.TcpDecoder;
import org.araqne.pcap.decoder.tcp.TcpPortProtocolMapper;
import org.araqne.pcap.decoder.tcp.TcpProcessor;
import org.araqne.pcap.decoder.tcp.TcpSegmentCallback;
import org.araqne.pcap.decoder.udp.UdpDecoder;
import org.araqne.pcap.decoder.udp.UdpPortProtocolMapper;
import org.araqne.pcap.decoder.udp.UdpProcessor;
import org.araqne.pcap.live.PcapDevice;
import org.araqne.pcap.live.PcapDeviceMetadata;
import org.araqne.pcap.packet.PcapPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author delmitz
 */
public class PcapLiveRunner implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(PcapLiveRunner.class.getName());
	private volatile boolean stop = false;
	private PcapDevice device;

	private EthernetDecoder eth;
	private ArpDecoder arp;
	private IpDecoder ip;
	private Ipv6Decoder ipv6;
	private IcmpDecoder icmp;
	private Icmpv6Decoder icmpv6;
	private TcpDecoder tcp;
	private UdpDecoder udp;

	public PcapLiveRunner(PcapDevice device) {
		this.device = device;
		eth = new EthernetDecoder();
		arp = new ArpDecoder();
		ip = new IpDecoder();
		ipv6 = new Ipv6Decoder();
		icmp = new IcmpDecoder();
		icmpv6 = new Icmpv6Decoder();
		tcp = new TcpDecoder(new TcpPortProtocolMapper());
		udp = new UdpDecoder(new UdpPortProtocolMapper());

		eth.register(EthernetType.IPV4, ip);
		eth.register(EthernetType.IPV6, ipv6);
		eth.register(EthernetType.ARP, arp);

		ip.register(InternetProtocol.ICMP, icmp);
		ip.register(InternetProtocol.UDP, udp);
		ip.register(InternetProtocol.TCP, tcp);

		ipv6.register(InternetProtocol.ICMPV6, icmpv6);
		ipv6.register(InternetProtocol.TCP, tcp);
		ipv6.register(InternetProtocol.UDP, udp);
	}

	public void run() {
		try {
			while (true) {
				try {
					if (stop)
						break;

					List<PcapPacket> list = device.getPackets();
					int numpkt = list.size();
					for (int i = 0; i < numpkt; i++)
						eth.decode(list.get(i));
				} catch (Exception e) {
					logger.warn("araqne-pcap: decode error", e);
				}
			}
		} catch (Exception e) {
			logger.trace("pcap live runner failed", e);
		} finally {
			PcapDeviceMetadata metadata = device.getMetadata();
			MacAddress macAddress = metadata.getMacAddress();
			String desc = metadata.getDescription();
			logger.trace("araqne-pcap: live runner mac={}, desc={} stopped", macAddress, desc);
		}
	}

	public void runOnce() throws IOException {
		for (PcapPacket packet : device.getPackets())
			eth.decode(packet);
	}

	public PcapDevice getDevice() {
		return device;
	}

	public void setTcpProcessor(Protocol protocol, TcpProcessor processor) {
		tcp.getProtocolMapper().register(protocol, processor);
	}

	public void unsetTcpProcessor(Protocol protocol, TcpProcessor processor) {
		tcp.getProtocolMapper().unregister(protocol, processor);
	}

	public void setUdpProcessor(Protocol protocol, UdpProcessor processor) {
		udp.getProtocolMapper().register(protocol, processor);
	}

	public void unsetUdpProcessor(Protocol protocol, UdpProcessor processor) {
		udp.getProtocolMapper().unregister(protocol, processor);
	}

	public void addTcpCallback(TcpSegmentCallback callback) {
		tcp.registerSegmentCallback(callback);
	}

	public void removeTcpCallback(TcpSegmentCallback callback) {
		tcp.unregisterSegmentCallback(callback);
	}

	public void addIcmpv6Processor(Icmpv6Processor processor) {
		icmpv6.register(processor);
	}

	public EthernetDecoder getEthernetDecoder() {
		return eth;
	}

	public ArpDecoder getArpDecoder() {
		return arp;
	}

	public IpDecoder getIpDecoder() {
		return ip;
	}

	public Ipv6Decoder getIpv6Decoder() {
		return ipv6;
	}

	public IcmpDecoder getIcmpDecoder() {
		return icmp;
	}

	public Icmpv6Decoder getIcmpv6Decoder() {
		return icmpv6;
	}

	public TcpDecoder getTcpDecoder() {
		return tcp;
	}

	public UdpDecoder getUdpDecoder() {
		return udp;
	}

	public void stop() {
		stop = true;
	}
}
