/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author racheldhc
 */
public class Cache
{
    HashMap<String, String> cacheMap = new HashMap();

    
    public void addToCache(String clientRequestInJson, String movieInJson)
    {
        this.cacheMap.put(clientRequestInJson, movieInJson);
        System.out.println("added to cache");
    }
    
    public void displayCache(){
        for(Map.Entry<String,String> entry : cacheMap.entrySet())
        {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        } 
    }

    public boolean checkCache(String key)
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
    
    public String returnFromCache(String clientRequestInJson)
    {
        return cacheMap.get(clientRequestInJson);
    }
    
    public void clearCache()
    {
        cacheMap.clear();
    }
}