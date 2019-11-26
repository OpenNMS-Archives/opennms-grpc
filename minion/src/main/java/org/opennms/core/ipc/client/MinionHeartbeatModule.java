/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2019 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2019 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.core.ipc.client;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opennms.core.ipc.api.IpcSinkClient;
import org.opennms.core.ipc.common.SinkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;

public class MinionHeartbeatModule {

    private static final String SINK_HEARTBEAT = "Sink-Heartbeat";
    private static final Logger LOG = LoggerFactory.getLogger(MinionIpcClient.class);
    private static final long TIME_INTERVAL_HEARTBEAT = 30;
    private IpcSinkClient ipcSinkClient;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public MinionHeartbeatModule(IpcSinkClient ipcSinkClient) {
        this.ipcSinkClient = ipcSinkClient;
    }

    public void start() {
        executor.scheduleAtFixedRate(this::sendSinkHeartBeat, 0, TIME_INTERVAL_HEARTBEAT, TimeUnit.SECONDS);
    }

    private void sendSinkHeartBeat() {
        StreamObserver<SinkMessage> sender = ipcSinkClient.getSinkMessageSender();
        if (sender != null) {
            String messageId = UUID.randomUUID().toString();
            long currentTime = System.currentTimeMillis();
            SinkMessage.Builder sinkMessageBuilder = SinkMessage.newBuilder()
                    .setMessageId(messageId)
                    .setLocation("MINION")
                    .setModuleId(SINK_HEARTBEAT)
                    .setContent(ByteString.copyFromUtf8(String.valueOf(currentTime)));
            sender.onNext(sinkMessageBuilder.build());
            LOG.debug("Sending Sink Heartbeat to OpenNMS");
        }
    }
}
