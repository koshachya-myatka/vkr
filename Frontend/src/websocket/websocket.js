import { Client } from '@stomp/stompjs';

let client = null;

export const connectWebSocket = (onEvent, onConnected, onError) => {
    client = new Client({
        brokerURL: 'ws://localhost:8081/ws',
        reconnectDelay: 5000,
        debug: () => { },
    });

    client.onConnect = () => {
        console.log('WS connected');
        onConnected?.();
        client.subscribe('/topic/notifications', msg => {
            onEvent('notifications', JSON.parse(msg.body));
        });
        client.subscribe('/topic/mes', msg => {
            onEvent('mes', JSON.parse(msg.body));
        });
        client.subscribe('/topic/lims', msg => {
            onEvent('lims', JSON.parse(msg.body));
        });
        client.subscribe('/topic/scada', msg => {
            onEvent('scada', JSON.parse(msg.body));
        });
    };
    
    client.onStompError = () => {
        onError?.();
    };

    client.onWebSocketError = () => {
        onError?.();
    };

    client.activate();
};

export const disconnectWebSocket = () => {
    if (client) client.deactivate();
};