/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.HashMap;
import java.util.Map;

public class Cache<K,V>
{
    HashMap<K, V> cacheMap = new HashMap();

    
    public synchronized void addToCache(K clientRequestInJson, V movieInJson)
    {
        this.cacheMap.put(clientRequestInJson, movieInJson);
        System.out.println("added to cache");
    }
    
    public synchronized void displayCache(){
        for(Map.Entry<K,V> entry : cacheMap.entrySet())
        {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        } 
    }

    public synchronized boolean checkCache(String key)
    {
        if (cacheMap.isEmpty())
        {
            System.out.println("cache empty");
            return false;
        }
        else if (cacheMap.containsKey(key))
        {
            System.out.println("in cache");
            return true;
        }
        return false;
    }
    
    public synchronized V returnFromCache(K clientRequestInJson)
    {
        return cacheMap.get(clientRequestInJson);
    }
    
    public void clearCache()
    {
        cacheMap.clear();
    }
}