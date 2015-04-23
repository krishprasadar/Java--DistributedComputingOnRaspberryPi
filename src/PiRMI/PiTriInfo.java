/*
 * Pi RMI.
 * © G J Barnard 2013 - Attribution-NonCommercial-ShareAlike 3.0 Unported - http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB.
 */
package PiRMI;

import java.io.Serializable;

/**
 * Wrapper class for three PiPairInfo's.  Must be 'Serializable' so that it can
 * be sent over a stream - i.e. RMI call.
 * 
 * @author G J Barnard
 */
public class PiTriInfo implements Serializable
{
    private PiPairInfo one, two, three;

    public PiTriInfo(PiPairInfo one, PiPairInfo two, PiPairInfo three)
    {
        this.one = one;
        this.two = two;
        this.three = three;
    }
    
    public PiPairInfo getOne()
    {
        return one;
    }

    public PiPairInfo getTwo()
    {
        return two;
    }

    public PiPairInfo getThree()
    {
        return three;
    }
}