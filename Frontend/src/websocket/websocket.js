import { Client } from '@stomp/stompjs';

let client = null;

export const connectWebSocket = (onEvent) => {
    client = new Client({
        brokerURL: 'ws://localhost:8081/ws',
        reconnectDelay: 5000,
        debug: () => {},
    });

    client.onConnect = () => {
        console.log('WS connected');
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

    client.activate();
};

export const disconnectWebSocket = () => {
    if (client) client.deactivate();
};