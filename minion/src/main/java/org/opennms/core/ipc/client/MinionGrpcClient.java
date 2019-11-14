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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opennms.core.ipc.common.OnmsIpcGrpc;
import org.opennms.core.ipc.common.RpcMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class MinionGrpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(MinionGrpcClient.class);
    private static final String HEARTBEAT_MODULE_ID = "RPC-Heartbeat";
    private static final long TIME_INTERVAL_HEARTBEAT = 5;
    private final ManagedChannel channel;
    private final OnmsIpcGrpc.OnmsIpcStub asyncStub;
    private String location;
    private String systemId;
    private StreamObserver<RpcMessage> requestHandler;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();


    public MinionGrpcClient(String location, String systemId, String host, int port) {

        this(ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext());
        this.location = location;
        this.systemId = systemId;
    }

    public MinionGrpcClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        asyncStub = OnmsIpcGrpc.newStub(channel);
    }

    public void start() {
        requestHandler = asyncStub.rpcStreaming(new RpcMessageHandler());
        LOG.info("Minion at location {} with systemId {} started", location, systemId);
        // Start a thread that keeps sending heartbeat to OpenNMS
        executor.scheduleAtFixedRate(this::sendHeartBeat, 0, TIME_INTERVAL_HEARTBEAT, TimeUnit.SECONDS);
    }


    private class RpcMessageHandler implements StreamObserver<RpcMessage> {

        @Override
        public void onNext(RpcMessage rpcMessage) {

            // Run the RPC module execution asynchronously.
            //Construct response using the same rpcId;
            RpcMessage.Builder rpcMessageBuilder = RpcMessage.newBuilder()
                    .setRpcId(rpcMessage.getRpcId())
                    .setSystemId(systemId)
                    .setLocation(rpcMessage.getLocation())
                    .setModuleId(rpcMessage.getModuleId())
                    .setRpcContent(rpcMessage.getRpcContent());
            requestHandler.onNext(rpcMessageBuilder.build());
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {

        }
    }

    public void shutdown() {
        executor.shutdown();
        requestHandler.onCompleted();
        channel.shutdown();
        LOG.info("Minion at location {} with systemId {} stopped", location, systemId);
    }

    private void sendHeartBeat() {
        RpcMessage.Builder rpcMessageBuilder = RpcMessage.newBuilder()
                .setLocation(location)
                .setSystemId(systemId)
                .setModuleId(HEARTBEAT_MODULE_ID)
                .setRpcId(systemId);
        requestHandler.onNext(rpcMessageBuilder.build());
        LOG.debug("Sending heartbeat to OpenNMS");
    }
}
