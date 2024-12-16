package org.brapi.test.BrAPITestServer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.*;

@Entity
@Table(name = "additional_info")
public class AdditionalInfoEntity extends BrAPIBaseEntity {
    @Column
    private String key;
    @Column
    private byte[] value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        ByteArrayInputStream bais = new ByteArrayInputStream(value);
        Object obj = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public void setValue(Object value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.value = baos.toByteArray();
    }


}
