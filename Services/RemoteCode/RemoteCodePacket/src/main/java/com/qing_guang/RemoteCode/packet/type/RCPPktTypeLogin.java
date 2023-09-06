package com.qing_guang.RemoteCode.packet.type;

import com.qing_guang.PacketBasedTCP.packet.PBPacketType;
import com.qing_guang.PacketBasedTCP.packet.format.anno.PacketTypeInfo;
import com.qing_guang.RemoteCode.packet.login.RCPPacketLoginREQ;
import com.qing_guang.RemoteCode.packet.login.RCPPacketLoginRESP;

/**
 * µÇÂ¼Ïà¹Ø
 */
@PacketTypeInfo(name = "RCP_LOGIN",includes = {RCPPacketLoginREQ.class, RCPPacketLoginRESP.class})
public class RCPPktTypeLogin extends PBPacketType {
}
