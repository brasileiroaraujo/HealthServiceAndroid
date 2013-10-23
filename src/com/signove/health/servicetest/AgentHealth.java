package com.signove.health.servicetest;

import java.util.Map;

import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

import com.signove.health.service.HealthAgentAPI;
import com.signove.health.service.HealthServiceAPI;

public class AgentHealth {
    private Handlers handler;
    private HealthServiceAPI api;
    
    
    public AgentHealth(TextView device, TextView data, TextView msg, Map<String, String> map, HealthServiceAPI api) {
        handler = new Handlers(device, data, msg, map);
        Log.w("APITESTE", "API: " + api);
        this.api = api;
    }

    private HealthAgentAPI.Stub agent = new HealthAgentAPI.Stub() {
        @Override
        public void Connected(String dev, String addr) {
            Log.w("HST", "Connected " + dev);
            Log.w("HST", "..." + addr);
            handler.handle_packet_connected(dev, addr);
        }

        @Override
        public void Associated(String dev, String xmldata) {
            final String idev = dev;
            Log.w("HST", "Associated " + dev);          
            Log.w("HST", "...." + xmldata);         
            handler.handle_packet_associated(dev, xmldata);

            Runnable req1 = new Runnable() {
                public void run() {
                    RequestConfig(idev);
                }
            };
            Runnable req2 = new Runnable() {
                public void run() {
                    RequestDeviceAttributes(idev);
                }
            };
            handler.getTm().postDelayed(req1, 1); 
            handler.getTm().postDelayed(req2, 500); 
        }
        @Override
        public void MeasurementData(String dev, String xmldata) {
            Log.w("HST", "MeasurementData " + dev);
            Log.w("HST", "....." + xmldata);
            handler.handle_packet_measurement(dev, xmldata);
        }
        @Override
        public void DeviceAttributes(String dev, String xmldata) {
            Log.w("HST", "DeviceAttributes " + dev);            
            Log.w("HST", ".." + xmldata);
            handler.handle_packet_description(dev, xmldata);
        }

        @Override
        public void Disassociated(String dev) {
            Log.w("HST", "Disassociated " + dev);                       
            handler.handle_packet_disassociated(dev);
        }

        @Override
        public void Disconnected(String dev) {
            Log.w("HST", "Disconnected " + dev);
            handler.handle_packet_disconnected(dev);
        }
    };
    
    private void RequestConfig(String dev)
    {   
        try {
            Log.w("HST", "Getting configuration ");
            String xmldata = api.GetConfiguration(dev);
            Log.w("HST", "Received configuration");
            Log.w("HST", ".." + xmldata);
        } catch (RemoteException e) {
            Log.w("HST", "Exception (RequestConfig)");
        }
    }

    private void RequestDeviceAttributes(String dev)
    {   
        try {
            Log.w("HST", "Requested device attributes");
            api.RequestDeviceAttributes(dev);
        } catch (RemoteException e) {
            Log.w("HST", "Exception (RequestDeviceAttributes)");
        }
    }

    public HealthAgentAPI.Stub getAgent() {
        return agent;
    }

    public void setAgent(HealthAgentAPI.Stub agent) {
        this.agent = agent;
    }
}
