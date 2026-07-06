package com.pfe.prep.core.net;

import com.pfe.prep.core.mvc.Dispatcher;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SelectorsPool
 */
public class SelectorsPool {

    private static final int WORKERS_NUMBER = 10;
    private int last = 0;
    private Selector[] pool = new Selector[WORKERS_NUMBER];
    private ExecutorService es = Executors.newFixedThreadPool(WORKERS_NUMBER);
    private Dispatcher dispatcher = new Dispatcher();

    public SelectorsPool() {
        try {
            for (int i = 0; i < WORKERS_NUMBER; i++) {
                pool[i] = Selector.open();
                final Selector selecterWorker = pool[i];
                es.execute(() -> {
                    try {
                        while (true) {
                            selecterWorker.select();
                            Set<SelectionKey> keys =
                                selecterWorker.selectedKeys();
                            Iterator<SelectionKey> iter = keys.iterator();
                            while (iter.hasNext()) {
                                SelectionKey key = iter.next();
                                if (key.isReadable()) {
                                    SocketChannel client =
                                        (SocketChannel) key.channel();
                                    new NioWorker(client, dispatcher).run();
                                }
                                iter.remove();
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        System.out.println(
                            "error in the method get in class SelectorsPool: " +
                                e
                        );
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("error in the selectors pool: " + e);
            // TODO: handle exception
        }
    }

    public void execute(SocketChannel sc) {
        try {
            final Selector selecterWorker = pool[last++ % WORKERS_NUMBER];

            selecterWorker.wakeup();
            sc.register(selecterWorker, SelectionKey.OP_READ);
        } catch (Exception e) {
            System.out.println("Error registering client: " + e);
        }
    }
}
