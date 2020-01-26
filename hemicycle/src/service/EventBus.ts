import { domUid } from '../utils';

type Callback = (eventDetails: any) => void;
export type Unsuscriber = () => void;

export default class EventBus<T extends string> {
    private eventListenersMap: Record<
        string,
        Record<string, Callback> | undefined
    > = {};

    public subscribe(event: T, listener: Callback): Unsuscriber {
        let eventListeners = this.eventListenersMap[event];
        if (!eventListeners) {
            eventListeners = {};
            this.eventListenersMap[event] = eventListeners;
        }
        const listenerId = domUid();
        eventListeners[listenerId] = listener;
        return () =>
            delete (eventListeners as Record<string, Callback>)[listenerId];
    }

    public publish(event: T, eventDetails?: any) {
        const eventListeners = this.eventListenersMap[event];
        if (!eventListeners) {
            return;
        }
        Object.keys(eventListeners as Record<string, Callback>).forEach(
            (k: string) => {
                const func = eventListeners[k];
                if (typeof func === 'function') {
                    func(eventDetails);
                }
            }
        );
    }
}
