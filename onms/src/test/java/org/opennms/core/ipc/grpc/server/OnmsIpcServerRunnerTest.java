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
import java.util.concurrent.TimeUnit;

import org.opennms.core.ipc.common.RpcMessage;

import com.google.protobuf.ByteString;

public class OnmsIpcServerRunnerTest {

    public static void main(String[] args) {

        OnmsGrpcServer server = new OnmsGrpcServer(8981);
        try {
            server.start();
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> sendRequest(server), 10, 3, TimeUnit.SECONDS);
            server.getServer().awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(OnmsGrpcServer server) {
        String message = "Current time " + System.currentTimeMillis();
        String rpcId = UUID.randomUUID().toString();

        RpcMessage.Builder builder = RpcMessage.newBuilder()
                .setRpcId(rpcId)
                .setLocation("MINION")
                .setRpcContent(ByteString.copyFromUtf8(message + "  MINION"));

        RpcMessage.Builder builder1 = RpcMessage.newBuilder()
                .setRpcId(rpcId)
                .setLocation("Apex")
                .setRpcContent(ByteString.copyFromUtf8(message + "  Apex"));
       try {
           server.sendRequest(builder.build());
           server.sendRequest(builder1.build());
       } catch (Exception e) {
           System.out.println("Failed to send request \n ");
           e.printStackTrace();
       }

        System.out.println("Sending message with Id = " + message);
    }
}
