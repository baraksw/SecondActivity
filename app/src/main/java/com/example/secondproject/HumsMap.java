package com.example.secondproject;

import java.util.HashMap;

public class HumsMap {

    public HashMap<String,Hum> hums_db;

    public HumsMap(){hums_db = new HashMap<String, Hum>();};

    public HumsMap(HashMap<String, Hum> map)
    {
        this.hums_db = map;
    }

    public HashMap<String, Hum> getHums_db() {
        return hums_db;
    }

    public void setHums_db(HashMap<String, Hum> hums_db) {
        this.hums_db = hums_db;
    }

    public void add_hum(Hum hum){ hums_db.put(hum.getHum_id(),hum); }

    public void remove_Hum(Hum hum){hums_db.remove(hum.getHum_id());}

    public void clear(){
        hums_db.clear();
    }

}
