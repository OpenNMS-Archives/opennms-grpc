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

public class MinionIpcClientRunner {

    public static void main(String[] args) throws InterruptedException {
        MinionGrpcClient minionIpcClient = new MinionGrpcClient("MINION", "1001", "localhost", 8981);
        MinionGrpcClient minionIpcClient1 = new MinionGrpcClient("MINION", "1002", "localhost", 8981);
        MinionGrpcClient minionIpcClient2 = new MinionGrpcClient("MINION", "1003", "localhost", 8981);
        minionIpcClient.start();
        minionIpcClient1.start();
        minionIpcClient2.start();
        Thread.sleep(5000);
        MinionGrpcClient minionIpcClient3 = new MinionGrpcClient("Apex", "1004", "localhost", 8981);
        MinionGrpcClient minionIpcClient4 = new MinionGrpcClient("Apex", "1005", "localhost", 8981);
        MinionGrpcClient minionIpcClient5 = new MinionGrpcClient("Apex", "1006", "localhost", 8981);
        minionIpcClient3.start();
        minionIpcClient4.start();
        minionIpcClient5.start();

        Thread.sleep(60000);
        minionIpcClient.shutdown();
        minionIpcClient1.shutdown();
        minionIpcClient2.shutdown();
        minionIpcClient3.shutdown();
        minionIpcClient4.shutdown();
        minionIpcClient5.shutdown();

    }
}
