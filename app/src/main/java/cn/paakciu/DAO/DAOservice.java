package cn.paakciu.DAO;

import java.util.HashMap;

public interface DAOservice {
    public HashMap<String,Object>[] get();
    public boolean put(Object obj);
    public boolean delete(Object obj);
}
