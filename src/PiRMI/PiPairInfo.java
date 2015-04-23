/*
 * Pi RMI.
 * © G J Barnard 2013 - Attribution-NonCommercial-ShareAlike 3.0 Unported - http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB.
 */
package PiRMI;

import java.io.Serializable;

/**
 * Wrapper class the key-value pairs found in system properties.  Must be
 * 'Serializable' so that it can be sent over a stream - i.e. RMI call.
 * 
 * @author G J Barnard
 */
public class PiPairInfo implements Serializable
{

    private String key, value;

    public PiPairInfo(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }
}