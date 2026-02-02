package com.mrcat.civilizations.lifecycle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import com.mrcat.civilizations.debug.Logging;

public class LifecycleEvents {
    
    private Logging logging = Logging.getInstance();
    public ArrayList<LifecycleEvents> tasks = new ArrayList<>();
        
    private Runnable task = () -> {
        try {
            Thread.sleep(1000);
            if (ThreadLocalRandom.current().nextInt(0, 181) == 0) {
                for (LifecycleEvents selTask : tasks) {
                    selTask.onRandomSec();
                }
            }
        } 
        catch (InterruptedException ex) {
            logging.addLog(ex.toString(), logging.logExists());
        }
    };
    
    public void startLifecycleEvents() {
        Thread thread = new Thread(task);
        thread.start();
    }
    
    public void onRandomSec() {}
}