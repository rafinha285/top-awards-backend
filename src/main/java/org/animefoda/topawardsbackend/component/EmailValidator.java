package org.animefoda.topawardsbackend.component;

import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.regex.Pattern;
import java.util.Hashtable;

@Component
public class EmailValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    public boolean isValid(String email) {
        if(email == null|| email.trim().isEmpty()) {
            return false;
        }

        if(!PATTERN.matcher(email).matches()) {
            return false;
        }

        String domain = email.substring(email.indexOf("@") + 1);
        return this.hasMxRecord(domain);
    }
    private boolean hasMxRecord(String domain) {
        try{
            Hashtable<String,String> env = new Hashtable<>();

            env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.dns.DnsContextFactory");

            env.put("com.sun.jndi.dns.timeout.initial", "2000");
            env.put("com.sun.jndi.dns.timeout.retries", "1");

            DirContext ictx = new InitialDirContext(env);

            Attributes attrs = ictx.getAttributes(domain, new String[]{"MX"});
            Attribute attr = attrs.get("MX");

            return attr != null && attr.size() > 0;
        } catch (NamingException e) {
            e.printStackTrace();
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
