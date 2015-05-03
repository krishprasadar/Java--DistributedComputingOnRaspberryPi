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
package components;

import sharedResources.Job;
import sharedResources.JobInterface;
import sharedResources.JobStatus;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This interface defines the methods you want your server to provide as a
 * service to the client.
 *
 * The client will use it as it's RMI connection 'class' and the server will
 * implement it.
 *
 * @author G J Barnard
 */
public interface Service extends Remote
{
    public static final String SORT_SERVICE = "SortService";
    public static final String AVG_SERVICE = "AverageService";

    public static final int RMIRegistryPort = 2024;
    public static final int ServicePort = 2025;

    public void push(JobInterface jobIntf, List<Integer> list) throws RemoteException;
    public List<Integer> pull(JobInterface job) throws RemoteException;
    public List<JobInterface> getCompletedJobs() throws RemoteException;

}
