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

package org.opennms.core.ipc.grpc.server;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opennms.core.ipc.common.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;

/**
 * Test Client.
 * Echo module which sends messages to minion every 30 secs.
 */
public class EchoClient {

    private static final Logger LOG = LoggerFactory.getLogger(EchoClient.class);
    private static final long ECHO_INTERVAL = 5;
    private OnmsGrpcServer onmsGrpcServer;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public EchoClient(OnmsGrpcServer onmsGrpcServer) {
        this.onmsGrpcServer = onmsGrpcServer;
    }

    public void start() throws IOException {
        executorService.scheduleAtFixedRate(() -> sendRequest(this.onmsGrpcServer), 10, ECHO_INTERVAL, TimeUnit.SECONDS);
    }

    private static void sendRequest(OnmsGrpcServer server) {
        String message = "Current time at Server " + System.currentTimeMillis();
        String rpcId = UUID.randomUUID().toString();

        RpcMessage.Builder builder = RpcMessage.newBuilder()
                .setRpcId(rpcId)
                .setLocation("MINION")
                .setModuleId("Echo")
                .setRpcContent(ByteString.copyFromUtf8(message));
        try {
            server.sendRequest(builder.build());
            LOG.debug("sending echo request to minion", message);
        } catch (Exception e) {
            LOG.error("Failed to send request \n ");
            e.printStackTrace();
        }

    }
}
