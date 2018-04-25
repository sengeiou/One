package com.ubt.en.alpha1e.action.util;

import java.util.Collection;
import java.util.List;

public class YHDCollectionUtils {  
      
     public static final Collection NULL_COLLECTION = new NullCollection();  
          
    public static final <T> Collection<T> nullCollection() {  
        return (List<T>) NULL_COLLECTION;  
    }  
}  
 
