/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.client;

import com.hazelcast.client.impl.InstanceListenerManager;
import com.hazelcast.core.*;
import com.hazelcast.instance.MemberImpl;
import com.hazelcast.nio.Address;
import com.hazelcast.nio.Protocol;
import com.hazelcast.nio.protocol.Command;

import java.net.UnknownHostException;
import java.util.*;

import static com.hazelcast.client.ProxyHelper.check;

public class ClusterClientProxy implements Cluster {
    //    final PacketProxyHelper proxyHelper;
    final ProxyHelper proxyHelper;
    final private HazelcastClient client;

    public ClusterClientProxy(HazelcastClient client) {
        this.client = client;
//        proxyHelper = new PacketProxyHelper("", client);
        proxyHelper = new ProxyHelper("", client);
    }

    public Collection<DistributedObject> getInstances() {
//        Keys instances = (Keys) proxyHelper.doOp(ClusterOperation.GET_INSTANCES, null, null);
        List<DistributedObject> list = new ArrayList<DistributedObject>();
//        if (instances != null) {
//            for (Data data : instances) {
//                Object o = IOUtil.toObject(data.buffer);
//                if (o instanceof FactoryImpl.ProxyKey) {
//                    FactoryImpl.ProxyKey proxyKey = (FactoryImpl.ProxyKey) o;
//                    list.add((Instance) client.getClientProxy(proxyKey.getKey()));
//                } else {
//                    list.add((Instance) client.getClientProxy(o));
//                }
//            }
//        }
        return list;
    }

    public void addMembershipListener(MembershipListener listener) {
        check(listener);
        client.getListenerManager().getMembershipListenerManager().registerListener(listener);
    }

    public void removeMembershipListener(MembershipListener listener) {
        client.getListenerManager().getMembershipListenerManager().removeListener(listener);
    }

    public Set<Member> getMembers() {
        Protocol protocol = proxyHelper.doCommand(Command.MEMBERS, (String[]) null, null);
        Set<Member> members = new HashSet<Member>();
        for (String arg : protocol.args) {
            String[] address = arg.split(":");
            try {
                Member member = new MemberImpl(new Address(address[0], Integer.valueOf(address[1])), false);
                members.add(member);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
//
//
//        Keys cw = (Keys) proxyHelper.doOp(ClusterOperation.GET_MEMBERS, null, null);
//        Collection<Data> datas = cw.getKeys();
//        Set<Member> set = new LinkedHashSet<Member>();
//        for (Data d : datas) {
//            set.add((Member) IOUtil.toObject(d.buffer));
//        }
        return members;
    }

    public Member getMember(Address address) {
        throw new UnsupportedOperationException();
    }

    public Member getLocalMember() {
        throw new UnsupportedOperationException();
    }

    public long getClusterTime() {
//        return (Long) proxyHelper.doOp(ClusterOperation.GET_CLUSTER_TIME, null, null);
        return 0;
    }

    public void addInstanceListener(DistributedObjectListener listener) {
//        check(listener);
//        if (instanceListenerManager().noListenerRegistered()) {
//            Call c = instanceListenerManager().createNewAddListenerCall(proxyHelper);
//            proxyHelper.doCall(c);
//        }
//        instanceListenerManager().registerListener(listener);
    }

    public void removeInstanceListener(DistributedObjectListener distributedObjectListener) {
        check(distributedObjectListener);
        instanceListenerManager().removeListener(distributedObjectListener);
    }

    private InstanceListenerManager instanceListenerManager() {
        return client.getListenerManager().getInstanceListenerManager();
    }

    @Override
    public String toString() {
        Set<Member> members = getMembers();
        StringBuffer sb = new StringBuffer("Cluster [");
        if (members != null) {
            sb.append(members.size());
            sb.append("] {");
            for (Member member : members) {
                sb.append("\n\t").append(member);
            }
        }
        sb.append("\n}\n");
        return sb.toString();
    }
}
