package com.qing_guang.AccountWarehouse.packet.server;

import com.qing_guang.PacketBasedTCP.packet.format.anno.ContainerOnly;
import com.qing_guang.PacketBasedTCP.packet.format.anno.DataField;

import java.util.UUID;

@ContainerOnly
public class ServiceInfo {

    @DataField(fieldID = 0)
    private String name;

    @DataField(fieldID = 0)
    private UUID uid;

}
