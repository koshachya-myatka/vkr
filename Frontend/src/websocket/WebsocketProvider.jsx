import { useEffect, useRef, useState } from 'react';
import { useQueryClient } from '@tanstack/react-query';
import Loader from '../components/general/Loader';
import { connectWebSocket, disconnectWebSocket } from './websocket';

export const WebSocketProvider = ({ children }) => {
    const queryClient = useQueryClient();
    const scadaBufferRef = useRef([]);
    const [isConnected, setIsConnected] = useState(false);

    useEffect(() => {
        const timeout = setTimeout(() => {
            if (!isConnected) {
                console.error("WS connection timeout");
            }
        }, 10000);

        connectWebSocket((type, msg) => {
            switch (type) {
                case 'notifications':
                    queryClient.setQueryData(['unviewedNotificationsCount'], (old) => { return old + 1; });
                    window.dispatchEvent(new Event("notifications-new"));
                    queryClient.invalidateQueries({ queryKey: ['notifications-active'] });
                    queryClient.invalidateQueries({ queryKey: ['notifications-stats'] });
                    break;
                case 'mes':
                    queryClient.invalidateQueries({ queryKey: ['dashboard-manag-last-statistics'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-batchData', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-prod-last-batches'] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-mes', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-metal-grid'] });
                    break;
                case 'lims':
                    queryClient.invalidateQueries({ queryKey: ['batch-page-prod-lims', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['batch-page-lab-lims', msg.batchId] });
                    queryClient.invalidateQueries({ queryKey: ['dashboard-lab-last-lims'] });
                    break;
                case 'scada':
                    scadaBufferRef.current.push(msg.batchId);
                    break;
            }
        },

            () => {
                setIsConnected(true);
                clearTimeout(timeout);
            },

            () => {
                setIsConnected(false);
            }
        );

        const intervalScada = setInterval(async () => {
            if (scadaBufferRef.current.length === 0) {
                return;
            }
            const uniqueBatchIds = [...new Set(scadaBufferRef.current)];
            scadaBufferRef.current = [];
            await Promise.all(
                uniqueBatchIds.map(batchId =>
                    Promise.all([
                        queryClient.invalidateQueries({ queryKey: ['batch-page-prod-scada', batchId] }),
                        queryClient.invalidateQueries({ queryKey: ['batch-page-manag-scada', batchId] }),
                    ])
                )
            )
        }, 1000);

        return () => {
            clearTimeout(timeout);
            clearInterval(intervalScada);
            disconnectWebSocket();
        };

    }, [queryClient, isConnected]);

    if (!isConnected) {
        return (
            <div className="page-loader">
                <Loader size="large" />
            </div>
        );
    }

    return children;
};