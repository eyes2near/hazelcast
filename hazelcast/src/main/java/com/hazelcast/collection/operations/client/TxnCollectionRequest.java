/*
 * Copyright (c) 2008-2012, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.collection.operations.client;

import com.hazelcast.client.CallableClientRequest;
import com.hazelcast.collection.CollectionPortableHook;
import com.hazelcast.collection.CollectionService;
import com.hazelcast.nio.IOUtil;
import com.hazelcast.nio.serialization.Data;
import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableReader;
import com.hazelcast.nio.serialization.PortableWriter;

import java.io.IOException;

/**
 * @author ali 6/11/13
 */
public abstract class TxnCollectionRequest extends CallableClientRequest implements Portable {

    String name;
    Data data;

    public TxnCollectionRequest() {
    }

    public TxnCollectionRequest(String name) {
        this.name = name;
    }

    public TxnCollectionRequest(String name, Data data) {
        this(name);
        this.data = data;
    }

    public String getServiceName() {
        return CollectionService.SERVICE_NAME;
    }

    public int getFactoryId() {
        return CollectionPortableHook.F_ID;
    }

    public void writePortable(PortableWriter writer) throws IOException {
        writer.writeUTF("n",name);
        IOUtil.writeNullableData(writer.getRawDataOutput(), data);
    }

    public void readPortable(PortableReader reader) throws IOException {
        name = reader.readUTF("n");
        data = IOUtil.readNullableData(reader.getRawDataInput());
    }
}
