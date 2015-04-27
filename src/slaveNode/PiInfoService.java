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
package slaveNode;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import pirmi.PiInfo;
import pirmi.PiPairInfo;

/**
 * This is the class that implements the service served by the server for the
 * client.  It extends UnicastRemoteObject -
 * http://docs.oracle.com/javase/8/docs/api/java/rmi/server/UnicastRemoteObject.html
 * - so that will serve RMI requests and thus the 'rmic' tool is not required.
 */
public class PiInfoService extends UnicastRemoteObject implements PiInfo
{
    public PiInfoService(int port) throws RemoteException
    {
        super(port);
    }

    /*
     * Methods to get the following:
     * java.version
     * os.arch
     * os.name
     * os.version
     * sun.arch.data.model
     * sun.cpu.endian
     * user.name
     * user.home
     * user.dir
     */ 
    
    @Override
    public int[] sort(int[] numbers) throws RemoteException {
        System.out.println("Sort method called");
        for(int i = 0; i < numbers.length ;i++){
            for(int j = i+1 ; j < numbers.length; j++){

                if(numbers[i] > numbers[j]){
                    int tmp = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = tmp;
                }
            }
        }
        return numbers;
    }
}
