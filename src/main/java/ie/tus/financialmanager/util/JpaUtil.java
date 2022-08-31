package ie.tus.financialmanager.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class JpaUtil {
    public static void copyNotNullProperties(Object src,Object target){
        BeanUtils.copyProperties(src,target,getNullPropertyNames(src));
    }

    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
            if ("password".equals(pd.getName()))  emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];

        System.out.println(result);

        return emptyNames.toArray(result);
    }
}
