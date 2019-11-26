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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.opennms.core.ipc.common.Empty;
import org.opennms.core.ipc.common.OnmsIpcGrpc;
import org.opennms.core.ipc.common.RpcMessage;
import org.opennms.core.ipc.common.SinkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class OnmsGrpcServer {

    private static final Logger LOG = LoggerFactory.getLogger(OnmsGrpcServer.class);
    private Server server;
    private Map<StreamObserver<RpcMessage>, String> responseObserverMap = new ConcurrentHashMap<>();
    private SetMultimap<String, StreamObserver<RpcMessage>> locationObserverMap = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    private Map<Set<StreamObserver<RpcMessage>>, Iterator<StreamObserver<RpcMessage>>> iteratorMap = new ConcurrentHashMap<>();


    public OnmsGrpcServer(int port) {
        this.server = ServerBuilder.forPort(port)
                .addService(new OnmsIpcService()).build();
    }

    public void start() throws IOException {
        this.server.start();
        LOG.info("OpenNMS server started");
    }

    public void shutdown() {
        this.server.shutdown();
        LOG.info("OpenNMS server stopped");
    }


    private class OnmsIpcService extends OnmsIpcGrpc.OnmsIpcImplBase {

        @Override
        public StreamObserver<RpcMessage> rpcStreaming(
                StreamObserver<RpcMessage> responseObserver) {

            return new StreamObserver<RpcMessage>() {

                @Override
                public void onNext(RpcMessage rpcMessage) {
                    // Register client when message is metadata.
                    LOG.debug("Received RPC message from module {}", rpcMessage.getModuleId());
                    if (isMetadata(rpcMessage)) {
                        String location = rpcMessage.getLocation();
                        String systemId = rpcMessage.getSystemId();
                        addResponseObserver(location, systemId, responseObserver);
                    }
                    handleResponse(rpcMessage);
                }

                @Override
                public void onError(Throwable throwable) {
                    removeResponseObserver(responseObserver);
                }

                @Override
                public void onCompleted() {
                    removeResponseObserver(responseObserver);
                }
            };
        }

        public io.grpc.stub.StreamObserver<SinkMessage> sinkStreaming(
                io.grpc.stub.StreamObserver<Empty> responseObserver) {

            return new StreamObserver<SinkMessage>() {
                @Override
                public void onNext(SinkMessage value) {
                  LOG.debug("Received sink message from module {}", value.getModuleId());
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {

                }
            };
        }
    }


    private void handleResponse(RpcMessage rpcMessage) {
        // Handle response from the Minion.
        LOG.debug("Received message from at location {} for module {}", rpcMessage.getLocation(), rpcMessage.getModuleId());
        LOG.debug("Content = {}", rpcMessage.getRpcContent().toStringUtf8());

    }

    public void sendRequest(RpcMessage rpcMessage) {
        StreamObserver<RpcMessage> responseObserver = getResponseObserver(rpcMessage.getLocation());
        if (responseObserver != null) {
            responseObserver.onNext(rpcMessage);
        } else {
            LOG.error("No minion found at location {}", rpcMessage.getLocation());
        }
    }

    private StreamObserver<RpcMessage> getResponseObserver(String location) {
        Set<StreamObserver<RpcMessage>> streamObservers = locationObserverMap.get(location);
        if (streamObservers.isEmpty()) {
            return null;
        }
        Iterator<StreamObserver<RpcMessage>> iterator;
        if (iteratorMap.get(streamObservers) == null) {
            iterator = Iterables.cycle(streamObservers).iterator();
            iteratorMap.put(streamObservers, iterator);
        } else {
            iterator = iteratorMap.get(streamObservers);
        }
        return iterator.next();
    }

    private void addResponseObserver(String location, String systemId, StreamObserver<RpcMessage> responseObserver) {
        if (responseObserverMap.get(responseObserver) == null) {
            LOG.info("Added minion from location : {} with systemId : {}", location, systemId);
            responseObserverMap.put(responseObserver, location + ":" + systemId);
            locationObserverMap.put(location, responseObserver);
        }
    }

    private void removeResponseObserver(StreamObserver<RpcMessage> responseObserver) {

        String systemIdWithLocation = responseObserverMap.remove(responseObserver);
        if (!Strings.isNullOrEmpty(systemIdWithLocation)) {
            String[] result = systemIdWithLocation.split(":");
            if (result.length == 2) {
                locationObserverMap.remove(result[0], responseObserver);
            }
        }
    }

    /**
     * Handle metadata from minion.
     */
    private boolean isMetadata(RpcMessage rpcMessage) {
        String systemId = rpcMessage.getSystemId();
        String rpcId = rpcMessage.getRpcId();
        return !Strings.isNullOrEmpty(systemId) &&
                !Strings.isNullOrEmpty(rpcId) &&
                systemId.equals(rpcId);
    }

    Server getServer() {
        return server;
    }
}
