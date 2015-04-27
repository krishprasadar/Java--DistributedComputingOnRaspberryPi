/*
 * PiRMI.
 * Copyright (C) 2015 Gareth J Barnard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Also see: http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */
package pirmi;

import java.io.Serializable;

/**
 * Wrapper class the key-value pairs found in system properties.  Must be
 * 'Serializable' so that it can be sent over a stream - i.e. RMI call.
 * 
 * I could have used the 'java.util.Properties' class instead to contain many
 * properties but this class illustrates how you can create your own classes
 * for use with RMI.
 * 
 * @author G J Barnard
 */
public class PiPairInfo implements Serializable
{
    private final String key, value;

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
